package com.example.Varsani.Clients;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.MainActivity;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckOut2 extends AppCompatActivity {

    private SessionHandler session;
    private UserModel user;

    private RelativeLayout layout_card;
    private TextView txv_name, txv_phoneNo, txv_email, txtBookingAmount;
    private EditText edt_county, edt_town, edt_address, edt_date, edtPaymentRef;
    private ProgressBar progressBar;
    private Button btn_checkout;
    private AutoCompleteTextView spinner_pet;
    private ArrayAdapter<String> petAdapter;
    private ArrayList<String> petList;

    // Service details
    private TextView txv_service_name, txv_service_price, txv_service_desc;
    private String serviceName, servicePrice, serviceDescription, serviceID;

    // Counties & Towns
    private ArrayList<String> arrayCounties;
    private ArrayList<String> arrayTowns;
    private String countyName, countyID, townName;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog datePicker;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out2);

        getSupportActionBar().setTitle("Complete Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ===== Initialize Views =====
        //layout_checkout = findViewById(R.id.layout_checkout);
        layout_card = findViewById(R.id.layout_card);
        progressBar = findViewById(R.id.progressBar);
        // progressCheckout = findViewById(R.id.progressCheckout);

        btn_checkout = findViewById(R.id.btn_order);
        txv_email = findViewById(R.id.txv_email);
        txv_name = findViewById(R.id.txv_name);
        txv_phoneNo = findViewById(R.id.txv_phoneNo);
        txtBookingAmount = findViewById(R.id.txtBookingAmount);

        edt_county = findViewById(R.id.edt_county);
        edt_town = findViewById(R.id.edt_town);
        edt_address = findViewById(R.id.edt_address);
        edt_date = findViewById(R.id.edt_date);
        edtPaymentRef = findViewById(R.id.edtPaymentRef);


        spinner_pet = findViewById(R.id.spinner_pet);

        spinner_pet.setFocusable(false);
        spinner_pet.setClickable(true);
        spinner_pet.setCursorVisible(false);
        spinner_pet.setKeyListener(null);
        spinner_pet.setLongClickable(false);


        petList = new ArrayList<>();

        spinner_pet.setOnClickListener(v -> {
            if (petList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No pets found", Toast.LENGTH_SHORT).show();
            } else {
                getAlertPets(v);
            }
        });


        // Service Details Views
        txv_service_name = findViewById(R.id.txv_service_name);
        txv_service_price = findViewById(R.id.txv_service_price);
        txv_service_desc = findViewById(R.id.txv_service_desc);


        // ===== Calendar & Date =====
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        edt_date.setText(dateFormat.format(calendar.getTime()));

        // ===== User session =====
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        txv_name.setText(user.getFirstname() + " " + user.getLastname());
        txv_email.setText(user.getEmail());
        txv_phoneNo.setText(user.getPhoneNo());

        edt_county.setFocusable(false);
        edt_town.setFocusable(false);

        arrayCounties = new ArrayList<>();
        arrayTowns = new ArrayList<>();

        edtPaymentRef.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //layout_bottom.setVisibility(View.GONE);
        //progressBarTown.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        //progressCheckout.setVisibility(View.VISIBLE);
        //layout_checkout.setVisibility(View.GONE);

        // ===== Receive Passed Service Details =====
        Intent intent = getIntent();
        serviceID = intent.getStringExtra("serviceID");
        serviceName = intent.getStringExtra("serviceName");
        servicePrice = intent.getStringExtra("servicePrice");
        serviceDescription = intent.getStringExtra("serviceDescription");

        txv_service_name.setText( serviceName != null ? "Service: " +  serviceName : "Service Name");
        txv_service_price.setText("Price" + servicePrice != null ? "Price: KSH " + servicePrice : "Price: KSh 0");
        txv_service_desc.setText(serviceDescription != null ? serviceDescription : "");

        txtBookingAmount.setText("Amount: Ksh " + servicePrice);

        // ===== Listeners =====
        edt_county.setOnClickListener(v -> {
            //progressBarTown.setVisibility(View.VISIBLE);
            edt_town.setVisibility(View.GONE);
            edt_town.setText("");
            getAlertCounties(v);
        });

        edt_town.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edt_county.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Enter county to continue", Toast.LENGTH_SHORT).show();
            } else {
                getAlertTowns(v);
            }
        });

        edt_date.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(CheckOut2.this, (view, year, month, dayOfMonth) -> {
                edt_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePicker.show();
        });

        btn_checkout.setOnClickListener(v -> alertOrderNow(v));

        // ===== Load Data =====
        getDlvyDetails();
        getCounties();
        fetchPets();
    }

    // ===== Fetch Pets =====
    private void fetchPets() {
        StringRequest request = new StringRequest(Request.Method.POST, Urls.URL_GET_PETS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("1")) {
                            JSONArray petsArray = obj.getJSONArray("pets");
                            petList.clear();
                            for (int i = 0; i < petsArray.length(); i++) {
                                JSONObject petObj = petsArray.getJSONObject(i);
                                String petName = petObj.getString("petName");
                                petList.add(petName);
                            }
                            petAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "No pets found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error fetching pets", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getClientID());
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    // ===== Order Submission =====
    public void orderNow() {
        progressBar.setVisibility(View.VISIBLE);
        btn_checkout.setVisibility(View.GONE);

        String county = edt_county.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String town = edt_town.getText().toString().trim();
        String paymentRef = edtPaymentRef.getText().toString().trim();
        String serviceDate = edt_date.getText().toString().trim();
        String petName = spinner_pet.getText().toString().trim();

        // ===== Validation =====
        if (TextUtils.isEmpty(county)) { showToastEnable("Please enter county"); return; }
        if (TextUtils.isEmpty(serviceDate)) { showToastEnable("Please select service date"); return; }
        if (TextUtils.isEmpty(town)) { showToastEnable("Please enter town"); return; }
        if (TextUtils.isEmpty(address)) { showToastEnable("Please enter your Apartment/Plot"); return; }
        if (TextUtils.isEmpty(paymentRef)) { showToastEnable("Please enter Payment Reference Code"); return; }
        if (!paymentRef.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            showToastEnable("Mpesa code should contain characters and digits"); return;
        }
        if (paymentRef.length() != 10) { showToastEnable("Mpesa code should be 10 characters"); return; }
        if (TextUtils.isEmpty(petName)) { showToastEnable("Please select your pet"); return; }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SUBMIT_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (status.equals("1")) {
                            progressBar.setVisibility(View.GONE);
                            layout_card.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(CheckOut2.this);
                            builder.setTitle("SUCCESS");
                            builder.setMessage(msg)
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                        dialog.dismiss();
                                    });
                            builder.setCancelable(false).create().show();
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            btn_checkout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        btn_checkout.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            error.printStackTrace();
            progressBar.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.VISIBLE);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getClientID());
                params.put("countyID", countyID);
                params.put("townName", town);
                params.put("address", address);
                params.put("paymentRef", paymentRef);
                params.put("serviceName", serviceName);
                params.put("servicePrice", servicePrice);
                params.put("serviceID", serviceID);
                params.put("petName", petName);
                params.put("serviceDate", serviceDate);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void showToastEnable(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        btn_checkout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    // ===== Other existing methods like getCounties(), getTowns(), getDlvyDetails(), orderCost() remain unchanged =====
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    // ===== Alert dialogs for counties & towns =====
    public void getAlertCounties(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("County ");
        final String[] array = arrayCounties.toArray(new String[0]);
        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, (dialogInterface, i) -> {
            edt_county.setText(array[i]);
            dialogInterface.dismiss();
            countyName = array[i];
            getTowns();
        });
        builder.show();
    }

    public void getAlertPets(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select Pet");

        final String[] array = petList.toArray(new String[0]);

        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, (dialog, i) -> {
            spinner_pet.setText(array[i]);
            dialog.dismiss();
        });

        builder.show();
    }


    public void getAlertTowns(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Town");
        final String[] array = arrayTowns.toArray(new String[0]);
        builder.setSingleChoiceItems(array, -1, (dialogInterface, i) -> {
            edt_town.setText(array[i]);
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    public void alertOrderNow(final View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Are you sure to submit this booking!");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            orderNow();
            dialog.dismiss();
        });
        builder.create().show();
    }

    public void getDlvyDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_DELIVERY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Response",""+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("1")){
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String county_ID = jsn.getString("county_ID");
                                    String county = jsn.getString("county");
                                    String town = jsn.getString("town");
                                    String ship_address = jsn.getString("ship_address");

                                    edt_county.setText(county);
                                    edt_town.setText(town);
                                    edt_address.setText(ship_address);
                                    countyID = county_ID;
                                }

                            } else if (status.equals("0")){
                                String msg = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("clientID", user.getClientID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void getCounties(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_COUNTIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Response"," "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("1")){
                                JSONArray jsonArray = jsonObject.getJSONArray("counties");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String county = jsn.getString("countyName");
                                    arrayCounties.add(county);
                                }
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void getTowns(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_TOWNS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.e("Response",""+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")){
                                JSONArray jsonArray = jsonObject.getJSONArray("towns");
                                arrayTowns.clear();
                                //progressBarTown.setVisibility(View.GONE);
                                edt_town.setVisibility(View.VISIBLE);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String towns = jsn.getString("townName");
                                    String ID = jsn.getString("countyID");
                                    arrayTowns.add(towns);

                                    countyID = ID;
                                }
                            } else {
                                String msg = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("countyName", countyName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
