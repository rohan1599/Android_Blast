package com.example.demotry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getCustomerInvoiceData.CustomerInvoiceData;

import java.util.ArrayList;
import java.util.List;

public class ReceivableAdapter extends RecyclerView.Adapter<ReceivableAdapter.ReceivableVH>{
    private List<CustomerInvoiceData> dataItem ;
    private Context context;

    public ReceivableAdapter(List<CustomerInvoiceData> dataItem, Context context) {
        this.dataItem = dataItem;
        this.context = context;
    }

    public  void filterList(ArrayList<CustomerInvoiceData> filterdNames) {
        this.dataItem = filterdNames;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ReceivableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.receivablelist_bg,parent,false);
        return new ReceivableVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivableVH holder, int position) {

        CustomerInvoiceData customerInvoiceData = dataItem.get(position);
       holder.doc_no.setText(customerInvoiceData.getDoc_NO());
       holder.cust.setText(customerInvoiceData.getCustName());
       holder.date.setText(customerInvoiceData.getDoc_Date());
       holder.base_amt.setText(customerInvoiceData.getBASE_AMT());
       holder.bal_amt.setText(customerInvoiceData.getBalanceAmt());
    }

    @Override
    public int getItemCount() {
        return dataItem == null ? 0 : dataItem.size();
    }

    public void setDataInvoice(List<CustomerInvoiceData> customerInvoiceData)
    {
        this.dataItem = customerInvoiceData;
        notifyDataSetChanged();
    }


    public class ReceivableVH extends RecyclerView.ViewHolder {
        TextView doc_no,cust,date,base_amt,bal_amt;
        public ReceivableVH(@NonNull View itemView) {
            super(itemView);

           doc_no = itemView.findViewById(R.id.doc_no);
           cust = itemView.findViewById(R.id.vendorname);
           date = itemView.findViewById(R.id.doc_date);
           base_amt = itemView.findViewById(R.id.docamt);
           bal_amt = itemView.findViewById(R.id.btp);
        }
    }
}
