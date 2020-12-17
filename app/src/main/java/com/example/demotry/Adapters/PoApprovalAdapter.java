package com.example.demotry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.OrderDetails;
import com.example.demotry.PoViewDoc;
import com.example.demotry.R;
import com.example.demotry.getPurchaseData.PurchaseData;

import java.util.ArrayList;
import java.util.List;

public class PoApprovalAdapter extends RecyclerView.Adapter<PoApprovalAdapter.PoApprovalVH> {
    private List<PurchaseData> purchaseData;
    private Context context;

    public PoApprovalAdapter(List<PurchaseData> purchaseData, Context context) {
        this.purchaseData = purchaseData;
        this.context = context;
    }



    public void removeItem(int adapterPosition) {
        purchaseData.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);

    }

    @NonNull
    @Override
    public PoApprovalAdapter.PoApprovalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.purchaselist_bg,parent,false);
        return new PoApprovalVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PoApprovalAdapter.PoApprovalVH holder, int position) {
       final PurchaseData Data = purchaseData.get(position);
        holder.doc_no.setText(Data.getDocNo());
        holder.doc_date.setText(Data.getDocDate());
        holder.vendor_name.setText(Data.getVendorName());
        holder.doc_amt.setText(Data.getAmt());

        holder.doc_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("Tag","docnum");
                Intent intent =  new Intent(context, OrderDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("doc_no",Data.getDocNo());
                context.startActivity(intent);
            }
        });

        holder.viewdocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(context, PoViewDoc.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("doc_no",Data.getDocNo());
                context.startActivity(intent);
            }
        });


    }

    public void setPurchaseData(List<PurchaseData> purchaseData)
    {
        this.purchaseData = purchaseData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return purchaseData == null ? 0 : purchaseData.size();
    }

    public void filterList(ArrayList<PurchaseData> filterdNames) {
        this.purchaseData = filterdNames;
        notifyDataSetChanged();
    }

    public void acceptedItem(int pos) {
            purchaseData.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, purchaseData.size());

    }


    public class PoApprovalVH extends RecyclerView.ViewHolder {
        TextView doc_no,doc_date,vendor_name,doc_amt,viewdocs;
        public PoApprovalVH(@NonNull View itemView) {
            super(itemView);

            doc_no = itemView.findViewById(R.id.doc_no);
            doc_date = itemView.findViewById(R.id.doc_date);
            vendor_name = itemView.findViewById(R.id.vendorname);
            doc_amt = itemView.findViewById(R.id.docamt);
            viewdocs = itemView.findViewById(R.id.viewdoc);
        }
    }
}
