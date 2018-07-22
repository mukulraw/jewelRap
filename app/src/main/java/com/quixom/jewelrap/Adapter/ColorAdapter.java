package com.quixom.jewelrap.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quixom.jewelrap.Adapter.Support.SelectableAdapterColor;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.Utils.Display;
import com.quixom.jewelrap.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22-Oct-15.
 */
public class ColorAdapter extends SelectableAdapterColor<ColorAdapter.ViewHolder> {

    static String TAG = "JEWELRAP";
    static ArrayList<JewelRapItem> listColor = new ArrayList<>();
    static Context mContext;
    private ViewHolder.ClickListener clickListener;
    static Display display = new Display();

    public ColorAdapter(ViewHolder.ClickListener clickListener, ArrayList<JewelRapItem> listColor, Context context) {
        this.mContext = context;
        this.clickListener = clickListener;
        this.listColor = listColor;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_adapter, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final JewelRapItem jewelRapItem = listColor.get(position);
        holder.TVColorName.setText(jewelRapItem.getItemName());
        if(!jewelRapItem.isClicked()) {
            holder.TVColorName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.TVColorName.setTextColor(mContext.getResources().getColor(R.color.text_color_default));

        } else {
            if(Utils.isKitKatOrLater()) {
                holder.TVColorName.setBackground(mContext.getResources().getDrawable(R.drawable.color_textview_background));
            }else{
                holder.TVColorName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.color_textview_background));
            }
            holder.TVColorName.setTextColor(mContext.getResources().getColor(R.color.white));

        }

        holder.TVColorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onItemClickedColor(position);

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

    @Override
    public int getItemCount() {
        return listColor.size();
    }

    public List<JewelRapItem> getSelectedItems() {
        List<Integer> list = getSelectedItemPositionsColor();
        List<JewelRapItem> items = new ArrayList<>();
        JewelRapItem selectedItem;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                selectedItem = new JewelRapItem();
                int j = list.get(i);
                selectedItem.setId(listColor.get(j).getId());
                selectedItem.setItemName(listColor.get(j).getItemName());
                items.add(selectedItem);
            }
        }
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView TVColorName;
        private ClickListener itemClickListener;

        public ViewHolder(View itemView, ClickListener itemClickListener) {
            super(itemView);
            TVColorName = (TextView) itemView.findViewById(R.id.tv_color_name);
            this.itemClickListener = itemClickListener;
        }

        public interface ClickListener {
            public void onItemClickedColor(int position);
        }
    }
}
