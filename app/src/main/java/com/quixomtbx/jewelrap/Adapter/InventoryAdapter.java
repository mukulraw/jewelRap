package com.quixomtbx.jewelrap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.InvetoryDetail;
import com.quixomtbx.jewelrap.Model.Inventory;
import com.quixomtbx.jewelrap.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Bhuma on 11/05/17.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    ArrayList<Inventory> inventoryArrayList;
    Context mContext;


    public InventoryAdapter(Context context, ArrayList<Inventory> arrayList) {
        this.mContext = context;
        this.inventoryArrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Inventory inventory = inventoryArrayList.get(position);
        holder.stockInfo.setText(inventory.getStockInfo());
        holder.cut.setText(inventory.getCut());
        holder.fluor.setText(inventory.getFluor());
        holder.pol.setText(inventory.getPolish());
        holder.meas.setText(inventory.getMeas());
        holder.sym.setText(inventory.getSymmetry());
        holder.cert.setText(inventory.getCertNumber());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InvetoryDetail.class);
                intent.putExtra("stock_id", inventory.getId());
                mContext.startActivity(intent);
            }
        });

        Log.d("TAG", "url : " + Global_variable.base_url + inventory.getImage());
        Glide.with(mContext)
                .load(Global_variable.base_url + File.separator + inventory.getImage())
                .asBitmap()
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(holder.diamondImage);


    }

    @Override
    public int getItemCount() {
        return inventoryArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stockInfo, cut, pol, sym, fluor, meas, cert;
        ImageView diamondImage;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardInvetoryStock);
            diamondImage = (ImageView) itemView.findViewById(R.id.invetoryImage);
            stockInfo = (TextView) itemView.findViewById(R.id.stockInfo);
            cut = (TextView) itemView.findViewById(R.id.invetoryCut);
            fluor = (TextView) itemView.findViewById(R.id.invetoryFlour);
            pol = (TextView) itemView.findViewById(R.id.invetoryPol);
            meas = (TextView) itemView.findViewById(R.id.invetoryMeas);
            sym = (TextView) itemView.findViewById(R.id.invetorySym);
            cert = (TextView) itemView.findViewById(R.id.invetoryCert);
        }
    }
}
