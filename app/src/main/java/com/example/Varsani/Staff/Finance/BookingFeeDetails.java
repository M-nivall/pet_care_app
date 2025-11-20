package com.example.Varsani.Staff.Finance;

import static com.example.Varsani.utils.Urls.URL_APPROVE_BOOKING_PAYMENTS;
import static com.example.Varsani.utils.Urls.URL_APPROVE_SERV_PAYMENTS;
import static com.example.Varsani.utils.Urls.URL_QUOTATION_ITEMS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Adapters.AdapterQuotItems;
import com.example.Varsani.Staff.Finance.Adapters.AdapterBookItems;
import com.example.Varsani.Staff.Models.ClientItemsModal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingFeeDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView txv_name,txv_orderID,txv_orderStatus,txv_orderDate,
            txv_serviceFee,txv_address,txv_town,
            txv_county,txv_mpesaCode;
    private TextView txv_serviceName, txv_serviceDate, txv_petName;
    private List<ClientItemsModal> list;
    private AdapterBookItems adapterBookItems;
    private RelativeLayout layout_bottom;
    private Button btn_appprove,btn_reject;

    String orderID;
    String orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_fee_details);

        getSupportActionBar().setSubtitle("Booking Fee Payment Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout_bottom=findViewById(R.id.layout_bottom);
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
        layout_bottom.setVisibility(View.GONE);
        btn_appprove=findViewById(R.id.btn_submit);
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
        txv_serviceFee.setText("Service Fee " + serviceFee);
        txv_name.setText("Client: " + clientName );
        txv_town.setText("Town " +town );
        txv_county.setText(county +"-"+town);
        txv_address.setText("Address " +address );
        txv_orderID.setText("#Booking ID: " +orderID );

        txv_serviceName.setText("Service: " + serviceName );
        txv_serviceDate.setText("Serial No: " + serviceDate );
        txv_petName.setText("Serial No: " + pet );


        recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);

        btn_appprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertApprove();
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


    public void approveOrder(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_APPROVE_BOOKING_PAYMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(BookingFeeDetails.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(BookingFeeDetails.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(BookingFeeDetails.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(BookingFeeDetails.this, error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void alertApprove(){
        android.app.AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Confirm");
        alertDialog.setCancelable(false);
        alertDialog.setButton2("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                return;
            }
        });
        alertDialog.setButton("Approve ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                approveOrder();
                return;
            }
        });
        alertDialog.show();
    }
}