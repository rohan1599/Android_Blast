package com.example.demotry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getPayableData.InvoiceData;

import java.util.ArrayList;
import java.util.List;

public class PayableAdapter extends RecyclerView.Adapter<PayableAdapter.PayableVH> {
    private List<InvoiceData> data;
    private Context context;

    public PayableAdapter(List<InvoiceData> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public PayableAdapter.PayableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.payablelist_bg,parent,false);
        return new PayableVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayableAdapter.PayableVH holder, int position) {
        InvoiceData invoiceData = data.get(position);
        holder.doc_no.setText(invoiceData.getDoc_NO());
        holder.cust.setText(invoiceData.getCustName());
        holder.date.setText(invoiceData.getDoc_Date());
        holder.base_amt.setText(invoiceData.getBASE_AMT());
        holder.bal_amt.setText(invoiceData.getBalanceAmt());

    }

    @Override
    public int getItemCount() {
        return  data == null ? 0 : data.size();
    }

    public void filterList(ArrayList<InvoiceData> filterdNames) {
        this.data = filterdNames;
        notifyDataSetChanged();
    }

    public void setDataInvoice(List<InvoiceData> invoiceData)
    {
        this.data = invoiceData;
        notifyDataSetChanged();
    }

    public  class PayableVH extends RecyclerView.ViewHolder {
        TextView doc_no,cust,date,base_amt,bal_amt;
        public PayableVH(@NonNull View itemView) {
            super(itemView);
            doc_no = itemView.findViewById(R.id.doc_no);
            cust = itemView.findViewById(R.id.vendorname);
            date = itemView.findViewById(R.id.doc_date);
            base_amt = itemView.findViewById(R.id.docamt);
            bal_amt = itemView.findViewById(R.id.btp);
        }
    }
}
