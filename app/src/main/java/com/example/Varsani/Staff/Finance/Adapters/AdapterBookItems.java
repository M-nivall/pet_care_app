package com.example.Varsani.Staff.Finance.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.ProductModal;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Adapters.AdapterQuotItems;
import com.example.Varsani.Staff.Models.ClientItemsModal;

import java.util.List;

public class AdapterBookItems extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ClientItemsModal> items;

    private Context ctx;
    ProgressDialog progressDialog;
    private AdapterBookItems.OnItemClickListener mOnItemClickListener;
    private AdapterBookItems.OnMoreButtonClickListener onMoreButtonClickListener;

    //


    private String orderID = "";

    public static final String TAG = "Orders adapter";

    public void setOnItemClickListener(final AdapterBookItems.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final AdapterBookItems.OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterBookItems(Context context, List<ClientItemsModal> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_itemName, txv_price,txv_quantity;
        public TextView txv_subTotal;


        public OriginalViewHolder(View v) {
            super(v);

            txv_itemName =v.findViewById(R.id.txv_itemName);
            txv_price =v.findViewById(R.id.txv_price);
            txv_quantity = v.findViewById(R.id.txv_quantity);
            txv_subTotal = v.findViewById(R.id.txv_subtotal);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_quot_items, parent, false);
        vh = new AdapterBookItems.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterBookItems.OriginalViewHolder) {
            final AdapterBookItems.OriginalViewHolder view = (AdapterBookItems.OriginalViewHolder) holder;

            final ClientItemsModal oi= items.get(position);

            view.txv_itemName.setText( "Service: "+oi.getItemName());
            view.txv_price.setText("KES "+ oi.getItemPrice());
            view.txv_quantity.setText("X "+ oi.getQuantity());
            view.txv_subTotal.setText("Subtotal KES "+ oi.getSubTotal());

            view.txv_price.setVisibility(View.GONE);
            view.txv_quantity.setVisibility(View.GONE);
            view.txv_subTotal.setVisibility(View.GONE);


        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ProductModal obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ProductModal obj, MenuItem item);
    }
}
