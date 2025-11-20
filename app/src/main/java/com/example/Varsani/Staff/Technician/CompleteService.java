package com.example.Varsani.Staff.Technician;

import static com.example.Varsani.utils.Urls.URL_CONFIRM_COMPLETION;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompleteService extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView txv_name,txv_orderID,txv_orderStatus,txv_orderDate,
            txv_serviceFee,txv_address,txv_town,
            txv_county,txv_mpesaCode;
    private TextView txv_serviceName, txv_serviceDate, txv_petName;
    private EditText et_remarks;
    private Button btn_complete;

    String orderID;
    String orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_service);


        getSupportActionBar().setSubtitle("Proceed Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progressBar);
        txv_address=findViewById(R.id.txv_address);
        txv_town=findViewById(R.id.txv_town);
        txv_county=findViewById(R.id.txv_county);
        txv_name=findViewById(R.id.txv_name);
        txv_mpesaCode=findViewById(R.id.txv_mpesaCode);
        txv_orderStatus=findViewById(R.id.txv_orderStatus);
        txv_serviceFee=findViewById(R.id.txv_serviceFee);
        txv_serviceName=findViewById(R.id.txv_serviceName);
        txv_petName=findViewById(R.id.txv_petName);
        txv_serviceDate=findViewById(R.id.txv_serviceDate);
        txv_orderID=findViewById(R.id.txv_orderID);
        txv_orderDate=findViewById(R.id.txv_orderDate);
        et_remarks = findViewById(R.id.et_remarks);
        btn_complete=findViewById(R.id.btn_complete);
        // btn_reject=findViewById(R.id.btn_reject);
        Intent intent=getIntent();

        orderID=intent.getStringExtra("orderID");
        String orderDate=intent.getStringExtra("orderDate");
        orderStatus=intent.getStringExtra("orderStatus");
        String orderCost=intent.getStringExtra("orderCost");
        String shippingCost=intent.getStringExtra("shippingCost");
        String itemCost=intent.getStringExtra("itemCost");
        String mpesaCode=intent.getStringExtra("mpesaCode");
        String clientName=intent.getStringExtra("clientName");
        String address=intent.getStringExtra("address");
        String town=intent.getStringExtra("town");
        String county=intent.getStringExtra("county");

        String serviceName=intent.getStringExtra("serviceName");
        String serviceFee=intent.getStringExtra("serviceFee");
        String pet=intent.getStringExtra("pet");
        String serviceDate=intent.getStringExtra("serviceDate");

        txv_orderDate.setText("Date :" + orderDate);
        txv_orderStatus.setText("Status: " + orderStatus );
        txv_mpesaCode.setText("Payment Code :" + mpesaCode);
        txv_serviceFee.setText("Service Fee: ksh " + serviceFee);
        txv_name.setText("Client: " + clientName );
        txv_town.setText("Town: " +town );
        txv_county.setText("County:" + county);
        txv_address.setText("Address: " +address );
        txv_orderID.setText("#Booking ID: " +orderID );

        txv_serviceName.setText("Service: " + serviceName );
        txv_serviceDate.setText("Service Date: " + serviceDate );
        txv_petName.setText("Pet: " + pet );

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlert(v);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void markOrder(){
        progressBar.setVisibility(View.VISIBLE);
        btn_complete.setVisibility(View.GONE);
         final String comments = et_remarks.getText().toString().trim();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_CONFIRM_COMPLETION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                                finish();
                            }
                            else{

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar.setVisibility(View.GONE);
                                btn_complete.setVisibility(View.VISIBLE);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar.setVisibility(View.GONE);
                            btn_complete.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                progressBar.setVisibility(View.GONE);
                btn_complete.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("comments",comments);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void getAlert(View v){
        final String remarks = et_remarks.getText().toString();
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Completion  Now!!!");
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if(TextUtils.isEmpty(remarks)){
                    Toast.makeText(getApplicationContext()," Please Include some comments",Toast.LENGTH_SHORT).show();
                }else{
                    markOrder();
                }

                return;
            }
        });
        builder.show();

    }
}