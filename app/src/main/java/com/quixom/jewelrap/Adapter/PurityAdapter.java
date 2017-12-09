package com.quixom.jewelrap.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quixom.jewelrap.Adapter.Support.SelectableAdapterPurity;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.Utils.Display;
import com.quixom.jewelrap.Utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class PurityAdapter extends SelectableAdapterPurity<PurityAdapter.ViewHolder> {

    static Context mContext;
    static Display display = new Display();
    ArrayList<JewelRapItem> listPurity = new ArrayList<>();
    String TAG = "JEWELRAP";
    ViewHolder.ClickListener clickListener;

    public PurityAdapter(ViewHolder.ClickListener clickListener, ArrayList<JewelRapItem> listPurity, Context context) {
        this.mContext = context;
        this.listPurity = listPurity;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purity_adapter, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(PurityAdapter.ViewHolder holder, final int position) {
        final JewelRapItem item = listPurity.get(position);
        //Log.e("JEWELRAP"," purity list:: "+item.getItemName());
        holder.TVPurityName.setText(item.getItemName());
        if(!item.isClicked()) {
            holder.TVPurityName.setTextColor(mContext.getResources().getColor(R.color.text_color_default));
            holder.TVPurityName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            if(Utils.isKitKatOrLater()) {
                holder.TVPurityName.setBackground(mContext.getResources().getDrawable(R.drawable.textview_background));
            }else {
                holder.TVPurityName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.textview_background));
            }
            holder.TVPurityName.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        holder.TVPurityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onItemClickedPurity(position);

                if(isSelected(position))
                {
                    item.setClicked(true);

                }
                else
                {
                    item.setClicked(false);
                }
                notifyItemChanged(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listPurity.size();
    }

    public List<JewelRapItem> getSelectedItems() {
        List<Integer> list = getSelectedItemPositionsPurity();
        List<JewelRapItem> items = new ArrayList<>();
        JewelRapItem selectedItem;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                selectedItem = new JewelRapItem();
                int j = list.get(i);
                selectedItem.setId(listPurity.get(j).getId());
                selectedItem.setItemName(listPurity.get(j).getItemName());
                items.add(selectedItem);
            }
        }
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView TVPurityName;
        private ClickListener itemClickListener;

        public ViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            TVPurityName = (TextView) itemView.findViewById(R.id.tv_purity_name);
            this.itemClickListener = clickListener;
        }

        public interface ClickListener {
            public void onItemClickedPurity(int position);
        }
    }
}
