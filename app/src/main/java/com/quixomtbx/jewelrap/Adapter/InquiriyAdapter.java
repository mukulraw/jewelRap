package com.quixomtbx.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.InquiryDetail;
import com.quixomtbx.jewelrap.Listener.OnLoadMoreListener;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.R;

import java.util.ArrayList;


public class InquiriyAdapter extends RecyclerView.Adapter {


    public static String inquuiry = "";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    Context mcontext;
    Activity mActivity;
    private ArrayList<JewelRapItem> mItems;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    String comeinquiry;


    public InquiriyAdapter(Context context, int resource, ArrayList<JewelRapItem> jewellist, RecyclerView recyclerView,String comeinquiry) {
        mcontext = context;
        this.mItems = jewellist;
        this.mActivity = (Activity) context;
        this.comeinquiry=comeinquiry;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh=null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.inquiries_list, parent, false);

            vh = new ViewHolder(v);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public  void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            JewelRapItem jitem = mItems.get(position);
            final String id = jitem.getId();
            String status = jitem.getStatus().toString();
            if (status.equals("0")) {
                ((ViewHolder) holder).status.setText("Closed");
                ((ViewHolder) holder).status.setTextColor(Color.RED);

            } else{
                ((ViewHolder) holder).status.setText("");
            }
            String favorite=jitem.getFavorite().toString();
            if(Global_variable.logEnabled) {
                Log.e("** favorite value", favorite);
            }
            if(favorite.equals("true")){
                ((ViewHolder) holder).imageviewfillFavorite.setVisibility(View.VISIBLE);
            }
            else{
                ((ViewHolder) holder).imageviewfillFavorite.setVisibility(View.INVISIBLE);
            }
            ((ViewHolder) holder).inquiriestitle.setText(jitem.getCategoryName().toString());
            ((ViewHolder) holder).inquiried_details.setText(jitem.getShapeTitle().toString());
            ((ViewHolder) holder).datetime.setText(jitem.getDate().toString());
            ((ViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mcontext, InquiryDetail.class);
                    in.putExtra("inq_id", id);
                    in.putExtra("comeinquiry",comeinquiry);
                    mActivity.startActivity(in);
                    mActivity.overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                  //  mActivity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            });

            ((ViewHolder) holder).firmname.setText(jitem.getFirmname().toString());

            Glide.with(mcontext)
                    .load(Global_variable.base_url + jitem.getCategoryImage().toString())

                    .into(((ViewHolder) holder).circleImageView);
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inquiriestitle, inquiried_details, datetime, status, firmname;
        public CardView card;
        public ImageView circleImageView,imageviewfillFavorite;

        Button deleteimg;

        public ViewHolder(View itemView) {
            super(itemView);
            inquiriestitle = (TextView) itemView.findViewById(R.id.text_inquiri_title);
            inquiried_details = (TextView) itemView.findViewById(R.id.text_inquiri_details);
            datetime = (TextView) itemView.findViewById(R.id.text_Datetime);
            circleImageView = (ImageView) itemView.findViewById(R.id.image);
            card = (CardView) itemView.findViewById(R.id.cardView);
            status = (TextView) itemView.findViewById(R.id.status);
            firmname = (TextView) itemView.findViewById(R.id.text_inquiri_username);
            imageviewfillFavorite=(ImageView)itemView.findViewById(R.id.imageviewfillFavorite);
        }
    }


}

