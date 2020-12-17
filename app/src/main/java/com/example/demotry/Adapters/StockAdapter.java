package com.example.demotry.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotry.R;
import com.example.demotry.getProductData.DataItem;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockAdapterVH> {
    private List<DataItem> dataItem ;
    private Context context;
    DataItem dataItem1;

    public StockAdapter(List<DataItem> dataItem, Context context) {
        this.dataItem =  dataItem;
         this.context = context;
    }

    @NonNull
    @Override
    public StockAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("RECYCLEVIEW","oncreate" + getItemCount());
         LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.stocklist_bg,parent,false);
         return new StockAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapterVH holder, int position) {

       // ProductDataResponse productDataResponse = dataItem.get(position);
      //  dataItem1 = (DataItem) productDataResponse.getData();
         dataItem1= (DataItem) dataItem.get(position);
        holder.matcode.setText(dataItem1.getMat_code());
        holder.matdesc.setText( dataItem1.getDesc());
        holder.qty.setText(String.valueOf(dataItem1.getQty()));
        holder.amt.setText(String.valueOf(dataItem1.getAmt()));




    }

    public void setDataItem(List<DataItem> dataItem) {
        this.dataItem = dataItem;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataItem == null ? 0 : dataItem.size();
    }

    public void filterList(ArrayList<DataItem> filterdNames) {
        this.dataItem = filterdNames;
        notifyDataSetChanged();
    }


    public class StockAdapterVH extends RecyclerView.ViewHolder {
        TextView matcode,matdesc,qty,amt;

        public StockAdapterVH(@NonNull View itemView) {
            super(itemView);
            matcode=itemView.findViewById(R.id.doc_date);
            matdesc = itemView.findViewById(R.id.doc_no);
            qty = itemView.findViewById(R.id.docamt);
            amt = itemView.findViewById(R.id.btp);

        }
    }
}
