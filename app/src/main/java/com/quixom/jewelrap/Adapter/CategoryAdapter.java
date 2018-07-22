package com.quixom.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.PlaceOrder;
import com.quixom.jewelrap.R;

import org.json.JSONObject;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    public static JSONObject jsonObject;
    Activity activity;
    Context mContext;
    JewelRapItem item;
    String TAG = "JEWELRAP1";
    private ArrayList<JewelRapItem> itemList;

    public CategoryAdapter(Context context, ArrayList<JewelRapItem> itemList, Activity activity) {
        this.mContext = context;
        this.itemList = itemList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final JewelRapItem item = itemList.get(position);
        viewHolder.TVcategoryName.setText(item.getCategoryName());
        viewHolder.CVcategoryCard.setTag(position + 1);
        Glide.with(mContext)
                .load(item.getCategoryImage())
                .into(viewHolder.IVcategoryImage);

        viewHolder.CVcategoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardView cardView = (CardView) view;
                final int tagId = (int) view.getTag();
                final ImageView imageView = (ImageView) view.findViewById(R.id.iv_category_image);
                final TextView textView = (TextView) view.findViewById(R.id.tv_category_name);
                final LinearLayout layout = (LinearLayout) view.findViewById(R.id.linlayout);
                textView.setTextColor(mContext.getResources().getColor(R.color.gray));

                //changeColor(imageView, textView, 25, 136, 254, mContext.getResources().getColor(R.color.text_color_overlay));
                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            startNewActivity(tagId, imageView, textView, layout);
                        }
                    }, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeColor(ImageView imageView, TextView textView, float r, float g, float b, int color) {
        Drawable drawable = imageView.getDrawable();
        float[] matrix = {1, 0, 0, 0, r
                , 0, 1, 0, 0, g
                , 0, 0, 1, 0, b
                , 0, 0, 0, 1, 0};
        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        drawable.setColorFilter(colorFilter);
        textView.setTextColor(color);
        imageView.setImageDrawable(drawable);
    }

    private void startNewActivity(int category, ImageView imageView, TextView textView, LinearLayout layout) {
        Intent intent = new Intent(mContext, PlaceOrder.class);
        intent.putExtra("json_object", itemList.get(category - 1).getJsonObjectCategory().toString());
        int categoryId = Integer.parseInt(itemList.get(category - 1).getId());
        intent.putExtra("selected_category", categoryId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ShapeAdapter.flag = "false";
        Glide.get(mContext).clearMemory();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
       // changeColor(textView, 0, 0, 0, mContext.getResources().getColor(R.color.white));
        textView.setTextColor(mContext.getResources().getColor(R.color.white));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView TVcategoryName;
        public ImageView IVcategoryImage;
        public CardView CVcategoryCard;

        public ViewHolder(View itemView) {
            super(itemView);
            TVcategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
            IVcategoryImage = (ImageView) itemView.findViewById(R.id.iv_category_image);
            CVcategoryCard = (CardView) itemView.findViewById(R.id.cv_category_card);
        }
    }
}