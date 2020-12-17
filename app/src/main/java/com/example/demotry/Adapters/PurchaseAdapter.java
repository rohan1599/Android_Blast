package com.example.demotry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getPurchaseInvoiceData.InvoiceData;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseVH> {
    private List<InvoiceData> data;
    private Context context;

    public PurchaseAdapter(List<InvoiceData> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public PurchaseAdapter.PurchaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.purchasereport_bg,parent,false);
        return new PurchaseVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.PurchaseVH holder, int position) {
            InvoiceData invoiceData = data.get(position);
            holder.invoice_no.setText(invoiceData.getInvoiceNo());
            holder.vendor_name.setText(invoiceData.getVendName());
            holder.vendor_no.setText(invoiceData.getVENDOR_NO());
            holder.invoice_amt.setText(invoiceData.getINVOICE_AMOUNT());
            holder.invoice_date.setText(invoiceData.getInvoiceDate());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setInvoiceData(List<InvoiceData> dataItem) {
        this.data = dataItem;
        notifyDataSetChanged();

    }

    public void filterList(ArrayList<InvoiceData> filterdNames) {
        this.data = filterdNames;
        notifyDataSetChanged();
    }

    public class PurchaseVH extends RecyclerView.ViewHolder {
        TextView invoice_no,vendor_name,invoice_date,invoice_amt,vendor_no;
        public PurchaseVH(@NonNull View itemView) {
            super(itemView);

            invoice_no = itemView.findViewById(R.id.doc_no);
            vendor_name = itemView.findViewById(R.id.vendorname);
            vendor_no = itemView.findViewById(R.id.vendor_no);
            invoice_date = itemView.findViewById(R.id.doc_date);
            invoice_amt = itemView.findViewById(R.id.docamt);

        }
    }
}
