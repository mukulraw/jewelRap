package com.quixomtbx.jewelrap.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quixomtbx.jewelrap.Adapter.Support.SelectableAdapterShape;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.R;

import java.util.ArrayList;
import java.util.List;


public class SolitaireShapeAdapter extends SelectableAdapterShape<SolitaireShapeAdapter.ViewHolder> {

    static String TAG = "JEWELRAP";
    static ArrayList<JewelRapItem> listShape = new ArrayList<>();
    static Context mContext;
    private ViewHolder.ClickListener clickListener;
    public static String flag="false";
    int categoryId;
    float textsize;
    public SolitaireShapeAdapter(ViewHolder.ClickListener clickListener, ArrayList<JewelRapItem> listShape, Context context, int categoryId, float textsize) {
        this.listShape = listShape;
        this.clickListener = clickListener;
        this.mContext = context;
        this.categoryId=categoryId;
        this.textsize=textsize;
        Glide.get(mContext).clearMemory();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_adapter, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final JewelRapItem jewelRapItem = listShape.get(position);
        String url = Global_variable.base_url + jewelRapItem.getCategoryImage();
        holder.TVshapeName.setText(jewelRapItem.getItemName());
        holder.TVshapeName.setTextSize(textsize);
        Drawable drawable=mContext.getResources().getDrawable(R.drawable.ic_launcher);
        int width=drawable.getMinimumWidth();
        int height=drawable.getMinimumHeight();
       /* if(categoryId==5) {
             holder.IVshapeImage.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0,10,0,10);

            holder.TVshapeName.setLayoutParams(layoutParams);
        }*/


        Glide.with(mContext)
                .load(url)
                .override(width, height)
                .placeholder(R.drawable.ic_launcher)
                .into(holder.IVshapeImage);
        if(!jewelRapItem.isClicked()) {
            if (categoryId ==5 ||categoryId== 6) {

            }else{
                holder.changeColor(holder.IVshapeImage,0, 0, 0);
            }

           // holder.changeColor(holder.IVshapeImage,0, 0, 0);
            holder.TVshapeName.setTextColor(mContext.getResources().getColor(R.color.text_color_default));
        }
        else
        {
            if (categoryId ==5 ||categoryId== 6) {

            }else{
                holder.changeColor(holder.IVshapeImage, 25, 136, 254);
            }

            holder.TVshapeName.setTextColor(mContext.getResources().getColor(R.color.text_color_overlay));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null)
                    clickListener.onItemClickedShape(position);
                if (isSelected(position)){
                    jewelRapItem.setClicked(true);
                }
                else{
                    jewelRapItem.setClicked(false);
                }
                notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listShape.size();
    }

    public List<JewelRapItem> getSelectedItems() {
        List<Integer> list = getSelectedItemPositionsShape();
        List<JewelRapItem> items = new ArrayList<>();
        JewelRapItem selectedItem;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                selectedItem = new JewelRapItem();
                int j = list.get(i);
                selectedItem.setId(listShape.get(j).getId());
                selectedItem.setItemName(listShape.get(j).getItemName());
                items.add(selectedItem);
            }
        }
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TVshapeName;
        ImageView IVshapeImage;
        private ClickListener itemClickListener;

        public ViewHolder(View itemView, ClickListener itemClickListener) {
            super(itemView);
            TVshapeName = (TextView) itemView.findViewById(R.id.tv_shape_name);
            IVshapeImage = (ImageView) itemView.findViewById(R.id.iv_shape_image);
            this.itemClickListener = itemClickListener;
            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            switch(displayMetrics.densityDpi){
                case DisplayMetrics.DENSITY_LOW:
                    //set text-size for low-density devices.
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    //set text-size for medium-density devices.
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    //set text-size for high-density devices.
                    int paddingPixel = 7;
                    float density = mContext.getResources().getDisplayMetrics().density;
                    int paddingDp = (int)(paddingPixel * density);
                    IVshapeImage.setPadding(paddingDp,0,paddingDp,0);
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    break;
                case DisplayMetrics.DENSITY_280:
                    break;
                case DisplayMetrics.DENSITY_400:
                    break;
                case DisplayMetrics.DENSITY_560:
                    break;
                case DisplayMetrics.DENSITY_TV:
                    break;
                default:
            }

        }

        public void changeColor(ImageView imageView, int r, int g, int b) {
            Drawable drawable = imageView.getDrawable();
            float[] matrix = {1, 0, 0, 0, r
                    , 0, 1, 0, 0, g
                    , 0, 0, 1, 0, b
                    , 0, 0, 0, 1, 0};
            ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
            //    drawable.setColorFilter(colorFilter);
            imageView.setColorFilter(colorFilter);

        }


        public interface ClickListener {
            public void onItemClickedShape(int position);
        }


    }
}