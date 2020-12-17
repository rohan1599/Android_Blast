package com.example.demotry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getPoOrderDetails.DataItem;
import com.example.demotry.getPoOrderDetails.OrderDetailResponse;

import java.util.List;

public class Po_OrderDetailsAdapter extends RecyclerView.Adapter<Po_OrderDetailsAdapter.Po_OrderDetailsAdapterVH> {
    private List<DataItem> orderDetails;
    private Context context;

    public Po_OrderDetailsAdapter(List<DataItem> orderDetails, Context context) {
        this.orderDetails = orderDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public Po_OrderDetailsAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.orderdetail_bg,parent,false);
        return new Po_OrderDetailsAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Po_OrderDetailsAdapterVH holder, int position) {
         DataItem Data = orderDetails.get(position);
        holder.order_no.setText(Data.getDocNo());
        holder.order_date.setText(Data.getDocDate());
        holder.vendor_Name.setText(Data.getVendorName());
        holder.po_material.setText(Data.getMaterial());
        holder.po_quantity.setText(Data.getQty());
        holder.po_amount.setText(Data.getAmount());
        holder.po_price.setText(Data.getPrice());
        holder.materialdesc.setText(Data.getMatDesc());
    }

    public void getOrderDetails(List<DataItem> orderDetails)
    {
        this.orderDetails = orderDetails;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return orderDetails == null ? 0 : orderDetails.size();
    }

    public class Po_OrderDetailsAdapterVH extends RecyclerView.ViewHolder {
        TextView  order_no,order_date,vendor_Name,po_price,po_material,materialdesc,po_quantity,po_amount;
        public Po_OrderDetailsAdapterVH(@NonNull View itemView) {
            super(itemView);
            order_no = itemView.findViewById(R.id.order_no);
            order_date = itemView.findViewById(R.id.order_date);
            vendor_Name = itemView.findViewById(R.id.vendor_Name);
            po_material = itemView.findViewById(R.id.po_material);
            po_price = itemView.findViewById(R.id.po_price);
            materialdesc = itemView.findViewById(R.id.materialdesc);
            po_quantity = itemView.findViewById(R.id.po_quantity);
            po_amount = itemView.findViewById(R.id.po_amount);
        }
    }
}
