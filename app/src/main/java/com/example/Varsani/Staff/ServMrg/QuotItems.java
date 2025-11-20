package com.example.Varsani.Staff.ServMrg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.Varsani.utils.Urls.URL_ASSIGN_TECH;
import static com.example.Varsani.utils.Urls.URL_GET_GROOMERS;
import static com.example.Varsani.utils.Urls.URL_GET_TRAINERS;
import static com.example.Varsani.utils.Urls.URL_GET_VET;

public class QuotItems extends AppCompatActivity {

    private TextView txv_name, txv_orderID, txv_orderStatus, txv_orderDate,
            txv_serviceFee, txv_address, txv_town,
            txv_county, txv_mpesaCode;
    private TextView txv_serviceName, txv_serviceDate, txv_petName;
    private Button btn_ship;
    private ArrayList<String> staffList;
    private EditText edt_vet;

    String orderID;
    String orderStatus;
    String serviceName; // service type passed via intent
    String selectedStaffApiUrl; // chosen endpoint

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quot_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // views
        txv_address = findViewById(R.id.txv_address);
        txv_town = findViewById(R.id.txv_town);
        txv_county = findViewById(R.id.txv_county);
        txv_name = findViewById(R.id.txv_name);
        txv_mpesaCode = findViewById(R.id.txv_mpesaCode);
        txv_orderStatus = findViewById(R.id.txv_orderStatus);
        txv_serviceFee = findViewById(R.id.txv_serviceFee);
        txv_serviceName = findViewById(R.id.txv_serviceName);
        txv_petName = findViewById(R.id.txv_petName);
        txv_serviceDate = findViewById(R.id.txv_serviceDate);
        txv_orderID = findViewById(R.id.txv_orderID);
        txv_orderDate = findViewById(R.id.txv_orderDate);

        btn_ship = findViewById(R.id.btn_submit);
        edt_vet = findViewById(R.id.edt_staff);

        // receive intent data
        Intent intent = getIntent();

        orderID = intent.getStringExtra("orderID");
        String orderDate = intent.getStringExtra("orderDate");
        orderStatus = intent.getStringExtra("orderStatus");
        String orderCost = intent.getStringExtra("orderCost");
        String shippingCost = intent.getStringExtra("shippingCost");
        String itemCost = intent.getStringExtra("itemCost");
        String mpesaCode = intent.getStringExtra("mpesaCode");
        String clientName = intent.getStringExtra("clientName");
        String address = intent.getStringExtra("address");
        String town = intent.getStringExtra("town");
        String county = intent.getStringExtra("county");

        serviceName = intent.getStringExtra("serviceName"); // IMPORTANT
        String serviceFee = intent.getStringExtra("serviceFee");
        String pet = intent.getStringExtra("pet");
        String serviceDate = intent.getStringExtra("serviceDate");

        // populate UI
        txv_orderDate.setText("Date : " + orderDate);
        txv_orderStatus.setText("Status: " + orderStatus);
        txv_mpesaCode.setText("Payment Code : " + mpesaCode);
        txv_serviceFee.setText("Service Fee: ksh " + serviceFee);
        txv_name.setText("Client: " + clientName);
        txv_town.setText("Town: " + town);
        txv_county.setText("County: " + county);
        txv_address.setText("Address: " + address);
        txv_orderID.setText("#Booking ID: " + orderID);

        txv_serviceName.setText("Service: " + serviceName);
        txv_serviceDate.setText("Service Date: " + serviceDate);
        txv_petName.setText("Pet: " + pet);

        // make edt non-editable (picker style)
        edt_vet.setFocusable(false);

        // init list
        staffList = new ArrayList<>();

        // pick staff when click on the EditText
        edt_vet.setOnClickListener(v -> showStaffPicker());

        // assign button
        btn_ship.setOnClickListener(v -> getAlertShip(v));

        // select which staff API to use based on service
        selectStaffCategory(serviceName);

        // fetch staff for the selected category
        getStaffByServiceType();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Choose API URL based on service name.
     * Expects serviceName to be "Vaccination", "Grooming" or "Training"
     */
    private void selectStaffCategory(String serviceName) {
        if (serviceName == null) {
            // fallback to vets
            selectedStaffApiUrl = URL_GET_VET;
            return;
        }

        String s = serviceName.trim().toLowerCase();
        if (s.contains("Vaccination") || s.equalsIgnoreCase("Vaccination")) {
            selectedStaffApiUrl = URL_GET_VET;
        } else if (s.contains("grooming")) {
            selectedStaffApiUrl = URL_GET_GROOMERS;
        } else if (s.contains("training")) {
            selectedStaffApiUrl = URL_GET_TRAINERS;
        } else {
            // default fallback
            selectedStaffApiUrl = URL_GET_VET;
        }
    }

    /**
     * Fetch staff list (vets/groomers/trainers) from server
     */
    public void getStaffByServiceType() {
        if (selectedStaffApiUrl == null || selectedStaffApiUrl.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No staff endpoint configured", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, selectedStaffApiUrl,
                response -> {
                    try {
                        Log.e("RESPONSE", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("details");
                            staffList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsn = jsonArray.getJSONObject(i);
                                String username = jsn.getString("username");
                                staffList.add(username);
                            }
                        } else {
                            // no staff or message
                            String msg = jsonObject.optString("message", "No staff available");
                            Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();
                    }
                }, error -> {
            error.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Show staff picker dialog (single choice)
     */
    public void showStaffPicker() {
        if (staffList.isEmpty()) {
            // If not yet loaded, fetch and notify user
            Toast toast = Toast.makeText(getApplicationContext(), "Loading staff, please wait...", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            getStaffByServiceType();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] array = staffList.toArray(new String[0]);

        builder.setTitle("Select Staff");
        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_vet.setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Confirmation dialog before assigning
     */
    public void getAlertShip(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Assign Services?");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                shipOrder();
            }
        });
        builder.show();
    }

    /**
     * Perform assignment to selected staff (POST to server)
     */
    public void shipOrder() {

        final String username = edt_vet.getText().toString().trim();

        if (username.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a Technician", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ASSIGN_TECH,
                response -> {
                    try {
                        Log.e("RESPONSE", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();

                        if (status.equals("1")) {
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();
                    }
                }, error -> {
            error.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderID", orderID);
                params.put("username", username);
                params.put("service", serviceName != null ? serviceName : "");
                Log.e("PARAMS", "" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
