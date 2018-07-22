package com.quixom.jewelrap.Adapter;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quixom.jewelrap.Adapter.Support.SelectableAdapterCertficate;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.Utils.Display;
import com.quixom.jewelrap.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 21-Oct-15.
 */
public class CertificateAdapter extends SelectableAdapterCertficate<CertificateAdapter.ViewHolder> {

    static Context mContext;
    static Display display = new Display();
    String TAG = "JewelRap";
    ArrayList<JewelRapItem> listCertificate = new ArrayList<>();
    private ViewHolder.ClickListener clickListener;

    public CertificateAdapter(ViewHolder.ClickListener clickListener, ArrayList<JewelRapItem> listCertificate, Context context) {
        this.mContext = context;
        this.clickListener = clickListener;
        this.listCertificate = listCertificate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.certificate_adapter, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final JewelRapItem jewelRapItem = listCertificate.get(position);
        holder.TVCertificateName.setText(jewelRapItem.getItemName());
        if(!jewelRapItem.isClicked()) {
            holder.TVCertificateName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.TVCertificateName.setTextColor(mContext.getResources().getColor(R.color.text_color_default));
        } else {
            if(Utils.isKitKatOrLater()) {
                holder.TVCertificateName.setBackground(mContext.getResources().getDrawable(R.drawable.textview_background));
            }else{
                holder.TVCertificateName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.textview_background));
            }
            holder.TVCertificateName.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        holder.TVCertificateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onItemClickedCertificate(position);

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
        return listCertificate.size();
    }

    public List<JewelRapItem> getSelectedItems() {
        List<Integer> list = getSelectedItemPositionsCertificate();
        List<JewelRapItem> items = new ArrayList<>();
        JewelRapItem selectedItem;

        if (!list.isEmpty()) {

            for (int i = 0; i < list.size(); i++) {
                selectedItem = new JewelRapItem();
                int j = list.get(i);

                selectedItem.setId(listCertificate.get(j).getId());
                selectedItem.setItemName(listCertificate.get(j).getItemName());
                items.add(selectedItem);
            }
        }
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TVCertificateName;
        private ClickListener itemClickListener;

        public ViewHolder(View itemView, ClickListener itemClickListener) {
            super(itemView);
            TVCertificateName = (TextView) itemView.findViewById(R.id.tv_certificate_name);
            this.itemClickListener = itemClickListener;
        }

        public interface ClickListener {
            public void onItemClickedCertificate(int position);
        }
    }
}
