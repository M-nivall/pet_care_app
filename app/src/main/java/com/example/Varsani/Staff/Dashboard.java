package com.example.Varsani.Staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.MainActivity;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Driver.*;
import com.example.Varsani.Staff.Finance.*;
import com.example.Varsani.Staff.ServMrg.*;
import com.example.Varsani.Staff.ShippingMrg.*;
import com.example.Varsani.Staff.Store_mrg.*;
import com.example.Varsani.Staff.Technician.*;
import com.example.Varsani.utils.SessionHandler;
import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private SessionHandler session;
    private UserModel user;
    private View contextView;
    private int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_bar);
        setSupportActionBar(toolbar);

        contextView = findViewById(android.R.id.content);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        drawer.closeDrawers();

        check();
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        } else if (id == R.id.nav_new_orders) {
            startActivity(new Intent(getApplicationContext(), NewOrders.class));
        } else if (id == R.id.nav_approvedOrders) {
            startActivity(new Intent(getApplicationContext(), ApprovedOrders.class));
        } else if (id == R.id.nav_new_serv_payments) {
            startActivity(new Intent(getApplicationContext(), NewServicePayments.class));
        } else if (id == R.id.nav_new_booking_payments) {
            startActivity(new Intent(getApplicationContext(), NewBookingPayments.class));
        }
        else if (id == R.id.nav_approved_serv_payments) {
            startActivity(new Intent(getApplicationContext(), ApprovedServPayments.class));
        } else if (id == R.id.nav_supplier_payments) {
            startActivity(new Intent(getApplicationContext(), SupplyPayments.class));
        } else if (id == R.id.nav_orders_to_shipp) {
            startActivity(new Intent(getApplicationContext(), OrdersToShip.class));
        } else if (id == R.id.nav_shipping_orders) {
            startActivity(new Intent(getApplicationContext(), ShippingOrders.class));
        } else if (id == R.id.nav_assigned_orders) {
            startActivity(new Intent(getApplicationContext(), AssignedOrders.class));
        } else if (id == R.id.nav_arrived_orders) {
            startActivity(new Intent(getApplicationContext(), ArrivedOrders.class));
        } else if (id == R.id.nav_delivered_orders) {
            startActivity(new Intent(getApplicationContext(), DeliveredOrders.class));
        } else if (id == R.id.nav_stock) {
            startActivity(new Intent(getApplicationContext(), ViewStock.class));
        } else if (id == R.id.nav_tools) {
            startActivity(new Intent(getApplicationContext(), ViewTools.class));
        } else if (id == R.id.nav_quot_requests) {
            startActivity(new Intent(getApplicationContext(), QuotationRequests.class));
        } else if (id == R.id.nav_service_completed) {
            startActivity(new Intent(getApplicationContext(), ServiceCompleted.class));
        } else if (id == R.id.nav_quot_visit) {
            startActivity(new Intent(getApplicationContext(), AssignedVisits.class));
        } else if (id == R.id.nav_assigned_services) {
            startActivity(new Intent(getApplicationContext(), AssignedServices.class));
        } else if (id == R.id.nav_materials) {
            startActivity(new Intent(getApplicationContext(), RequestedMaterials.class));
        } else if (id == R.id.nav_staff_feedback) {
            startActivity(new Intent(getApplicationContext(), Stafffeedback.class));
        } else if (id == R.id.nav_supplies) {
            startActivity(new Intent(getApplicationContext(), RequestItems.class));
        } else if (id == R.id.nav_logout) {
            alertLogout();
        }

        drawer.closeDrawer(GravityCompat.START, true);
        return false;
    }

    private void alertLogout() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setCancelable(false);
        alertDialog.setButton("Yes logout", (dialog, which) -> {
            session.logoutUser();
            Toast toast = Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
        alertDialog.setButton2("No", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressedExitApp();
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    @Override
    protected void onStart() {
        super.onStart();
        k = 0;
    }

    private void onBackPressedExitApp() {
        k++;
        if (k == 1) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Exit App");
            alertDialog.setIcon(R.drawable.ic_notifications);
            alertDialog.setMessage("Do you really want to exit?");
            alertDialog.setCancelable(false);
            alertDialog.setButton("Yes", (dialog, which) -> {
                finish();
                Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
                homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
                homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeScreenIntent);
            });
            alertDialog.setButton2("No", (dialog, which) -> {
                dialog.cancel();
                k = 0;
            });
            alertDialog.show();
        }
    }

    private void check() {
        navigationView.getMenu().findItem(R.id.nav_new_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_approvedOrders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_new_serv_payments).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_approved_serv_payments).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_orders_to_shipp).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_shipping_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_assigned_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_arrived_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_delivered_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_stock).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_supplies).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_quot_requests).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_quot_visit).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_assigned_services).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_staff_feedback).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_supplier_payments).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_materials).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_tools).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_service_completed).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_new_booking_payments).setVisible(false);

        if (session.isLoggedIn()) {
            String userType = user.getUser_type();

            if (userType.equals("Finance")) {
                navigationView.getMenu().findItem(R.id.nav_new_orders).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_approvedOrders).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_new_serv_payments).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_approved_serv_payments).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_supplier_payments).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_new_booking_payments).setVisible(true);

            } else if (userType.equals("Shipping Manager")) {
                navigationView.getMenu().findItem(R.id.nav_orders_to_shipp).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_shipping_orders).setVisible(true);

            } else if (userType.equals("Driver")) {
                navigationView.getMenu().findItem(R.id.nav_assigned_orders).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_arrived_orders).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_delivered_orders).setVisible(true);

            } else if (userType.equals("Stock manager")) {
                navigationView.getMenu().findItem(R.id.nav_stock).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_supplies).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_materials).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_tools).setVisible(true);

            } else if (userType.equals("Service manager")) {
                navigationView.getMenu().findItem(R.id.nav_quot_requests).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_service_completed).setVisible(true);

            } else if (userType.equals("Technician")) {
                navigationView.getMenu().findItem(R.id.nav_quot_visit).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_assigned_services).setVisible(true);
            }
        }
    }
}
