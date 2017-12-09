package com.quixom.jewelrap.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quixom.jewelrap.Adapter.Support.SelectableAdapterShade;
import com.quixom.jewelrap.Adapter.Support.SelectableAdapterSize;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.Utils.Display;
import com.quixom.jewelrap.Utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ShadeAdapter extends SelectableAdapterShade<ShadeAdapter.ViewHolder> {

    static String TAG = "JEWELRAP";
    static ArrayList<JewelRapItem> listshade = new ArrayList<>();
    static Context mContext;
    private ViewHolder.ClickListener clickListener;
    static Display display = new Display();
    public ShadeAdapter(ViewHolder.ClickListener clickListener, ArrayList<JewelRapItem> listShade, Context context) {
        this.listshade = listShade;
        this.clickListener = clickListener;
        this.mContext = context;
        for (int i = 0; i < listShade.size(); i++) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shade_adapter, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ShadeAdapter.ViewHolder holder, final int position) {
        final JewelRapItem jewelRapItem = listshade.get(position);
        String url = Global_variable.base_url + jewelRapItem.getCategoryImage();
        holder.TVshadeName.setText(jewelRapItem.getItemName());

        if(!jewelRapItem.isClicked()) {
            holder.TVshadeName.setTextColor(mContext.getResources().getColor(R.color.text_color_default));
            holder.TVshadeName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            if(Utils.isKitKatOrLater()) {
                holder.TVshadeName.setBackground(mContext.getResources().getDrawable(R.drawable.textview_background));
            }else{
                holder.TVshadeName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.textview_background));
            }
            holder.TVshadeName.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        holder.TVshadeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickListener != null)
                    clickListener.onItemClickedShade(position);

                if(isSelected(position))
                {
                    jewelRapItem.setClicked(true);

                }
                else
                {
                    jewelRapItem.setClicked(false);
                }
                notifyItemChanged(position);
            }
        });

    }

    public List<JewelRapItem> getSelectedItems() {
        List<Integer> list = getSelectedItemPositionsSize();
        List<JewelRapItem> items = new ArrayList<>();
        JewelRapItem selectedItem;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                selectedItem = new JewelRapItem();
                int j = list.get(i);
                selectedItem.setId(listshade.get(j).getId());
                selectedItem.setItemName(listshade.get(j).getItemName());
                items.add(selectedItem);
            }
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return listshade.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView TVshadeName;

        private ClickListener itemClickListener;

        public ViewHolder(View itemView, ClickListener itemClickListener) {
            super(itemView);
            TVshadeName = (TextView) itemView.findViewById(R.id.tv_shade_name);

            this.itemClickListener = itemClickListener;
        }

        public interface ClickListener {
            public void onItemClickedShade(int position);
        }
    }
}