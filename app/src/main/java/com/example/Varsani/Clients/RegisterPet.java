package com.example.Varsani.Clients;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterPet extends AppCompatActivity {

    private EditText edtPetName, edtBreed, edtWeight, edtDob;
    private Spinner spnSpecies, spnGender;
    private Button btnSubmitPet;
    private ProgressBar progressBar;
    private SessionHandler session;
    private UserModel user;

    private String selectedDob = ""; // store date selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ----------------------------
        // Initialize Views
        // ----------------------------
        edtPetName = findViewById(R.id.edtPetName);
        edtBreed = findViewById(R.id.edtBreed);
        edtWeight = findViewById(R.id.edtWeight);
        edtDob = findViewById(R.id.edtDob);

        spnSpecies = findViewById(R.id.spnSpecies);
        spnGender = findViewById(R.id.spnGender);

        btnSubmitPet = findViewById(R.id.btnSubmitPet);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        // ----------------------------
        // Populate Species Spinner
        // ----------------------------
        String[] speciesList = {"Dog", "Cat", "Bird", "Rabbit", "Reptile", "Other"};
        ArrayAdapter<String> speciesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, speciesList);
        spnSpecies.setAdapter(speciesAdapter);

        // ----------------------------
        // Populate Gender Spinner
        // ----------------------------
        String[] genderList = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderList);
        spnGender.setAdapter(genderAdapter);

        // ----------------------------
        // DATE PICKER FOR DOB
        // ----------------------------
        edtDob.setOnClickListener(v -> showDatePicker());

        btnSubmitPet.setOnClickListener(v -> registerPet());
    }

    // Back button handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // ----------------------------
    // SHOW DATE PICKER DIALOG
    // ----------------------------
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                RegisterPet.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    selectedMonth = selectedMonth + 1;

                    selectedDob = selectedYear + "-" +
                            (selectedMonth < 10 ? "0" + selectedMonth : selectedMonth) + "-" +
                            (selectedDay < 10 ? "0" + selectedDay : selectedDay);

                    edtDob.setText(selectedDob);
                }, year, month, day);

        dialog.show();
    }

    // ----------------------------
    // REGISTER PET FUNCTION
    // ----------------------------
    public void registerPet() {
        btnSubmitPet.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final String pet_name = edtPetName.getText().toString().trim();
        final String breed = edtBreed.getText().toString().trim();
        final String weight = edtWeight.getText().toString().trim();
        final String species = spnSpecies.getSelectedItem().toString();
        final String gender = spnGender.getSelectedItem().toString();
        final String dob = edtDob.getText().toString().trim();

    // VALIDATIONS
        if (TextUtils.isEmpty(pet_name)) {
            showError("Enter pet name");
            return;
        }

        if (TextUtils.isEmpty(breed)) {
            showError("Enter breed");
            return;
        }

        if (TextUtils.isEmpty(weight)) {
            showError("Enter weight");
            return;
        }

        if (TextUtils.isEmpty(dob)) {
            showError("Select date of birth");
            return;
        }


        // VOLLEY POST REQUEST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_REG_PET,
                response -> {
                    Log.e("Response", "is " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        if (status.equals("1")) {
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    btnSubmitPet.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }, error -> {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            btnSubmitPet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("pet_name", pet_name);
                params.put("species", species);
                params.put("breed", breed);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("weight", weight);
                params.put("client_id",user.getClientID());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        btnSubmitPet.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
