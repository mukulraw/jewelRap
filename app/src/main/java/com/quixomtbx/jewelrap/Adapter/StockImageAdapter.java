package com.quixomtbx.jewelrap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quixomtbx.jewelrap.R;
import com.quixomtbx.jewelrap.StockImageZoom;

import java.util.ArrayList;

/**
 * Created by Quixom on 12/05/17.
 */

public class StockImageAdapter extends RecyclerView.Adapter<StockImageAdapter.ViewHolder> {

    ArrayList<String> imageList;
    Context mContext;

    public StockImageAdapter(Context context, ArrayList<String> arrayList) {
        this.imageList = arrayList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String imagePath = imageList.get(position);
        Glide.with(mContext)
                .load(imagePath)
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StockImageZoom.class);
                intent.putExtra("image_url", imagePath);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.stockimage);
        }
    }
}
