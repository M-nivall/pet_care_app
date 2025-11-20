package com.example.Varsani.Staff.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Staff.Technician.CompleteService;
import com.example.Varsani.Staff.Technician.Models.AssignedBookingModel;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;

import java.util.List;

public class AdapterAssignedServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AssignedBookingModel> items;
    private Context ctx;

    public AdapterAssignedServices(Context context, List<AssignedBookingModel> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txv_orderID, txv_name, txv_orderStatus, txv_serviceName;

        public OriginalViewHolder(View v) {
            super(v);
            txv_name = v.findViewById(R.id.txv_name);
            txv_serviceName = v.findViewById(R.id.txv_serviceName);
            txv_orderStatus = v.findViewById(R.id.txv_orderStatus);
            txv_orderID = v.findViewById(R.id.txv_orderID);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_assigned, parent, false);
        return new OriginalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {

            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final AssignedBookingModel o = items.get(position);

            view.txv_orderID.setText("#Booking ID: " + o.getOrderID());
            view.txv_orderStatus.setText("Status: " + o.getOrderStatus());
            view.txv_serviceName.setText("Service: " + o.getServiceName());
            view.txv_name.setText("Client: " + o.getClientName());

            view.itemView.setOnClickListener(v -> {

                Intent in = new Intent(ctx, CompleteService.class);

                in.putExtra("orderID", o.getOrderID());
                in.putExtra("orderCost", o.getOrderCost());
                in.putExtra("clientName", o.getClientName());
                in.putExtra("mpesaCode", o.getMpesaCode());
                in.putExtra("orderDate", o.getOrderDate());
                in.putExtra("orderStatus", o.getOrderStatus());
                in.putExtra("itemCost", o.getOrderCost());
                in.putExtra("shippingCost", o.getShippingCost());
                in.putExtra("county", o.getCounty());
                in.putExtra("town", o.getTown());
                in.putExtra("address", o.getAddress());
                in.putExtra("serviceName", o.getServiceName());
                in.putExtra("serviceFee", o.getServiceFee());
                in.putExtra("pet", o.getPetName());
                in.putExtra("serviceDate", o.getServiceDate());

                // FIX: Required when starting activity from adapter
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                ctx.startActivity(in);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
