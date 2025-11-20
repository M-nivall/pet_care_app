package com.example.Varsani.Clients.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.CompletedServiceItems;
import com.example.Varsani.Clients.InvoiceItems;
import com.example.Varsani.Clients.OrderItems;
import com.example.Varsani.Staff.Models.ServiceBookingModel;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.Clients.Models.OrdersModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;

import java.util.List;

public class AdapterCompletedServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceBookingModel> items;

    private Context ctx;
    ProgressDialog progressDialog;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "Orders adapter";

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }
//
//    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
//        this.onMoreButtonClickListener = onMoreButtonClickListener;
//    }

    public AdapterCompletedServices(Context context, List<ServiceBookingModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_orderID, txv_amount,txv_orderDate;
        public TextView txv_service,txv_orderStatus;


        public OriginalViewHolder(View v) {
            super(v);

            txv_orderID =v.findViewById(R.id.txv_orderID);
            txv_service = v.findViewById(R.id.txv_service);
            txv_orderStatus = v.findViewById(R.id.txv_orderStatus);
            txv_orderDate = v.findViewById(R.id.txv_orderDate);
            txv_amount = v.findViewById(R.id.txv_amount);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_booking, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final ServiceBookingModel o= items.get(position);

            view.txv_orderID.setText("#Booking ID: "+o.getOrderID());
            view.txv_orderStatus.setText("Status: " + o.getOrderStatus());
            view.txv_amount.setText("Amount: "+o.getServiceFee());
            view.txv_service.setText("Service: "+o.getServiceName());
            view.txv_orderDate.setText("Booking Date: "+o.getOrderDate());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx,CompletedServiceItems.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("orderID", o.getOrderID());
                    in.putExtra("orderCost",o.getOrderCost());
                    in.putExtra("clientName",o.getClientName());
                    in.putExtra("mpesaCode",o.getMpesaCode());
                    in.putExtra("orderDate",o.getOrderDate());
                    in.putExtra("orderStatus",o.getOrderStatus());
                    in.putExtra("itemCost",o.getOrderCost());
                    in.putExtra("shippingCost",o.getShippingCost());
                    in.putExtra("county",o.getCounty());
                    in.putExtra("town",o.getTown());
                    in.putExtra("address",o.getAddress());

                    in.putExtra("serviceName",o.getServiceName());
                    in.putExtra("serviceFee",o.getServiceFee());
                    in.putExtra("pet",o.getPetName());
                    in.putExtra("serviceDate",o.getServiceDate());
                    ctx.startActivity(in);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, ProductModal obj, int pos);
//    }
//
//    public interface OnMoreButtonClickListener {
//        void onItemClick(View view, ProductModal obj, MenuItem item);
//    }


}