package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.Urls;
import com.squareup.picasso.Picasso;

public class SingleService extends AppCompatActivity {

    private SessionHandler session;
    private UserModel user;

    private String productID;
    private String productName;
    private String stock;
    private String desc;
    private String imagename;
    private String price;

    private Button btn_book_service;
    private LinearLayout layout_single;

    private TextView txv_price, txv_product_name, txv_description, txv_instock;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_service);

        // Receive data from previous activity
        Intent intent = getIntent();
        productID = intent.getStringExtra("proID");
        productName = intent.getStringExtra("proName");
        stock = intent.getStringExtra("stock");
        desc = intent.getStringExtra("desc");
        imagename = intent.getStringExtra("image");
        price = intent.getStringExtra("price");

        getSupportActionBar().setSubtitle(productName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize session and user
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        // Initialize views
        txv_product_name = findViewById(R.id.txv_product_name);
        txv_price = findViewById(R.id.txv_price);
        txv_description = findViewById(R.id.txv_ad_description);
        txv_instock = findViewById(R.id.txv_instock);
        imageView = findViewById(R.id.image_product);
        progressBar = findViewById(R.id.progressBar);
        btn_book_service = findViewById(R.id.btn_book_service);
        layout_single = findViewById(R.id.layout_single);

        progressBar.setVisibility(View.GONE);

        txv_product_name.setText(productName);
        txv_description.setText(desc);
        txv_price.setText("Price: " + price);

        // Load image using Picasso
        String url = Urls.ROOT_URL_IMAGES;
        Picasso.get().load(url + imagename)
                .fit()
                .centerCrop()
                .into(imageView);

        // Button click: directly navigate to Checkout
        btn_book_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    navigateToCheckout();
                } else {
                    Toast.makeText(getApplicationContext(), "You must login to book a service", Toast.LENGTH_SHORT).show();
                }
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

    /**
     * Navigate to Checkout Activity directly
     */
    private void navigateToCheckout() {
        Intent intent = new Intent(SingleService.this, CheckOut2.class);

        // Pass service/product details
        intent.putExtra("serviceID", productID);
        intent.putExtra("serviceName", productName);
        intent.putExtra("desc", desc);
        intent.putExtra("servicePrice", price);
        intent.putExtra("image", imagename);

        // Pass user info
        intent.putExtra("clientID", user.getClientID());

        startActivity(intent);
    }
}
