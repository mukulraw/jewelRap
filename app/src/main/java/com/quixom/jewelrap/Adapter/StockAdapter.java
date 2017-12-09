package com.quixom.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quixom.jewelrap.InventoryActivity;
import com.quixom.jewelrap.Model.Inventory;
import com.quixom.jewelrap.R;

import java.util.ArrayList;


/**
 * Created by Quixom on 10/05/17.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    ArrayList<Inventory> arrayList;
    Context mContext;
    Activity activity;

    public StockAdapter(Context context, ArrayList<Inventory> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Inventory stockInventory = arrayList.get(position);
        holder.textView.setText(stockInventory.getStockInfo());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockInventory.getId() == String.valueOf(0)) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity, R.style.DesignDemo);
                    dlgAlert.setMessage(stockInventory.getStockInfo());
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.setCancelable(false);
                    dlgAlert.create().show();
                } else {
                    Intent newIntent = new Intent(mContext, InventoryActivity.class);
                    newIntent.putExtra("stock_id", stockInventory.getId());
                    newIntent.putExtra("stock_position", position);
                    mContext.startActivity(newIntent);
                    activity.overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.adapter_stock_text);
        }
    }
}
