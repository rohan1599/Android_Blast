package com.example.demotry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getPrmDetails.DataItem;


import java.util.ArrayList;
import java.util.List;

public class PRAdapter extends RecyclerView.Adapter<PRAdapter.PRadapterVH> {
    private List<DataItem> prData;
    private Context context;

    public PRAdapter(List<DataItem> prData, Context context) {
        this.prData = prData;
        this.context = context;
    }

    public void removeItem(int adapterPosition) {
        prData.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);

    }

    public void acceptedItem(int pos) {
        prData.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, prData.size());

    }

    @NonNull
    @Override
    public PRAdapter.PRadapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.pr_bg,parent,false);
        return new PRadapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PRAdapter.PRadapterVH holder, int position) {
        final DataItem Data = prData.get(position);
        holder.pr_no.setText(Data.getDoc_no());
        holder.pr_name.setText(Data.getREQUISITIONAR());
        holder.prod_name.setText(Data.getMAT_DESC());
        holder.item_no.setText(Data.getDOC_ITEM_NO());
        holder.Qty.setText(Data.getQTY());
      /*  holder.pr_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag","docnum");
               /* Intent intent =  new Intent(context, .class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("doc_no",Data.getDocNo());
                context.startActivity(intent);
            }
        });

        holder.viewdocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent =  new Intent(context, PoViewDoc.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("doc_no",Data.getDocNo());
                context.startActivity(intent);
            }
        });*/


    }

    public void setPRData(List<DataItem> prData)
    {
        this.prData = prData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return prData == null ? 0 : prData.size();
    }

    public void filterList(ArrayList<DataItem> filterdNames) {
        this.prData = filterdNames;
        notifyDataSetChanged();
    }

    public class PRadapterVH extends RecyclerView.ViewHolder {
        TextView pr_no,prod_name,Qty,pr_name,item_no,viewdocs;
        public PRadapterVH(@NonNull View itemView) {
            super(itemView);

            pr_no = itemView.findViewById(R.id.pr_no);
            prod_name = itemView.findViewById(R.id.prod_name);
            Qty = itemView.findViewById(R.id.docamt);
            pr_name = itemView.findViewById(R.id.pr_name);
            item_no = itemView.findViewById(R.id.item_no);
           // viewdocs = itemView.findViewById(R.id.viewdoc);
        }
    }
}
