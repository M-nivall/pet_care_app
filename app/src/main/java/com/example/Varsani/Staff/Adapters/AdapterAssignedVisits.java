package com.example.Varsani.Staff.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Staff.Groomer.GroomingDetails;
import com.example.Varsani.Staff.Technician.AssignedDetails;
import com.example.Varsani.Staff.Technician.Models.AssignedBookingModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Trainer.TrainingActivity;

import java.util.List;

public class AdapterAssignedVisits extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AssignedBookingModel> items;
    private Context ctx;
    ProgressDialog progressDialog;

    public AdapterAssignedVisits(Context context, List<AssignedBookingModel> items) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_assigned, parent, false);
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

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in;
                    switch (o.getServiceName()) {
                        case "Vaccination":
                            in = new Intent(ctx, AssignedDetails.class);
                            break;
                        case "Grooming":
                            in = new Intent(ctx, GroomingDetails.class);
                            break;
                        case "Training":
                            in = new Intent(ctx, TrainingActivity.class);
                            break;
                        default:
                            Toast.makeText(ctx, "Unknown service type", Toast.LENGTH_SHORT).show();
                            return;
                    }

                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Pass all extras
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

                    ctx.startActivity(in);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
