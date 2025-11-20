package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.utils.Urls;
import com.example.Varsani.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private Button registerBtn;
    private ProgressBar progressBar;
    private EditText edt_firstname, edt_lastname, edt_username, edt_phone,
            edt_email, edt_password, edt_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // MATCHED EXACTLY WITH YOUR XML
        edt_username = findViewById(R.id.edt_username);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_lastname = findViewById(R.id.edt_lastname);
        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);

        progressBar = findViewById(R.id.progressBar);
        registerBtn = findViewById(R.id.register_btn);

        progressBar.setVisibility(View.GONE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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

    public void register() {
        registerBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final String firstname = edt_firstname.getText().toString().trim();
        final String lastname = edt_lastname.getText().toString().trim();
        final String username = edt_username.getText().toString().trim();
        final String phone = edt_phone.getText().toString().trim();
        final String email = edt_email.getText().toString().trim();
        final String password = edt_password.getText().toString().trim();
        final String password_c = edt_confirm_password.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // VALIDATIONS
        if (TextUtils.isEmpty(firstname)) {
            showError("Enter first name");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            showError("Enter username");
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            showError("Enter last name");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            showError("Enter phone number");
            return;
        }
        if (phone.length() != 10) {
            showError("Phone number should contain 10 digits");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showError("Enter email address");
            return;
        }
        if (!email.matches(emailPattern)) {
            showError("Invalid email address");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showError("Password is required");
            return;
        }
        if (!password.equals(password_c)) {
            showError("Password mismatch");
            return;
        }

        // VOLLEY POST REQUEST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "is " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                            if (status.equals("1")) {
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }

                            registerBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            registerBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                registerBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // ONLY MATCHING FIELDS IN YOUR XML
                Map<String, String> params = new HashMap<>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("phoneNo", phone);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        registerBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
