package com.example.demotry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getSalesInvoiceData.InvoiceData;

import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesVH> {
    private List<InvoiceData> dataList;
    private Context context;

    public SalesAdapter(List<InvoiceData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesAdapter.SalesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.saleslist_bg,parent,false);
        return new SalesVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.SalesVH holder, int position) {
            InvoiceData data = dataList.get(position);
            holder.invoice_no.setText(data.getInvoiceNo());
            holder.invoice_date.setText(data.getInvoiceDate());
            holder.custname.setText(data.getCustName());
            holder.invoice_amt.setText(data.getINVOICE_AMOUNT());
            holder.btp.setText(data.getBILLTOPARTY());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setInvoiceData(List<InvoiceData> dataItem) {
        this.dataList = dataItem;
        notifyDataSetChanged();

    }

    public void filterList(ArrayList<InvoiceData> filterdNames) {
        this.dataList = filterdNames;
        notifyDataSetChanged();
    }


    public class SalesVH extends RecyclerView.ViewHolder {
        TextView invoice_no,custname,invoice_date,invoice_amt,btp;
        public SalesVH(@NonNull View itemView) {
            super(itemView);

            invoice_no = itemView.findViewById(R.id.doc_no);
            invoice_date = itemView.findViewById(R.id.doc_date);
            invoice_amt = itemView.findViewById(R.id.docamt);
            custname = itemView.findViewById(R.id.vendorname);
            btp = itemView.findViewById(R.id.btp);
        }
    }
}
