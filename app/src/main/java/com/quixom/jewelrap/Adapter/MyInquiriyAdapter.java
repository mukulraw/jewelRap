package com.quixom.jewelrap.Adapter;


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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.InquiryDetail;
import com.quixom.jewelrap.Listener.OnLoadMoreListener;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyInquiriyAdapter extends RecyclerSwipeAdapter {

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

    public MyInquiriyAdapter(Context context, int resource, ArrayList<JewelRapItem> jewellist, RecyclerView recyclerView) {
        mcontext = context;
        this.mItems = jewellist;
        this.mActivity = (Activity) context;
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
    public int getItemViewType(int position) {
        return mItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {


        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.myinquiries_list, parent, false);
            vh = new ViewHolder(v);
        } 
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {

            final String url = Global_variable.inquiry_api;
            ((ViewHolder) holder).swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            ((ViewHolder) holder).swipeLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ViewHolder) holder).swipeLayout.close();
                }
            },50);

            JewelRapItem jitem = mItems.get(position);
            final String id = jitem.getId();
            String status = jitem.getStatus().toString();
            if (status.equals("0")) {
                ((ViewHolder) holder).status.setText("Closed");
                ((ViewHolder) holder).status.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).status.setTextColor(Color.RED);
                ((ViewHolder) holder).swipelinear.setVisibility(View.GONE);
            }
            else
            {
                ((ViewHolder) holder).status.setVisibility(View.GONE);
            }
            ((ViewHolder) holder).inquiriestitle.setText(jitem.getCategoryName().toString());
            ((ViewHolder) holder).inquiried_details.setText(jitem.getShapeTitle().toString());
            ((ViewHolder) holder).datetime.setText(jitem.getDate().toString());
            ((ViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mcontext, InquiryDetail.class);
                    Log.e(Global_variable.LOG_TAG,"Response");
                    in.putExtra("inq_id", id);
                    mcontext.startActivity(in);
                    mActivity.overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                }
            });

            ((ViewHolder) holder).deleteimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeEnquiry(id, holder, position);
                }
            });
            Glide.with(mcontext)
                    .load(Global_variable.base_url + jitem.getCategoryImage().toString())
                    .into(((ViewHolder) holder).circleImageView);

            mItemManger.bindView(
                    ((ViewHolder) holder).itemView, position);
        }
    }


    private void closeEnquiry(String id, final RecyclerView.ViewHolder holder, final int position) {
        RestClientVolley restClientVolley = new RestClientVolley(mActivity, Request.Method.POST, Global_variable.inquiry_api, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                String status, message;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(Global_variable.logEnabled){
                        Log.e("colose enquiry response" ," "+ response);
                    }
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equals("ok")) {

                        JewelRapItem itm=mItems.get(position);
                        itm.setStatus("0");
                        notifyItemRangeChanged(position, mItems.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        restClientVolley.addParameter("inq_id", id);
        restClientVolley.addParameter("status", "close");
        restClientVolley.executeRequest();
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inquiriestitle, inquiried_details, datetime, status;
        public CardView card;
        public ImageView circleImageView;
        public LinearLayout swipelinear, mainlinear;
        public SwipeLayout swipeLayout;
        Button deleteimg;

        public ViewHolder(View itemView) {
            super(itemView);
            inquiriestitle = (TextView) itemView.findViewById(R.id.text_inquiri_title);
            inquiried_details = (TextView) itemView.findViewById(R.id.text_inquiri_details);
            datetime = (TextView) itemView.findViewById(R.id.text_Datetime);
            circleImageView = (ImageView) itemView.findViewById(R.id.image);
            card = (CardView) itemView.findViewById(R.id.cardView);
            status = (TextView) itemView.findViewById(R.id.status);
            mainlinear = (LinearLayout) itemView.findViewById(R.id.list1);
            swipelinear = (LinearLayout) itemView.findViewById(R.id.list2);
            deleteimg = (Button) itemView.findViewById(R.id.trash);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
           /*   swipeLayout.setSwipeEnabled(true);
            swipeLayout.close(true);
*/

        }
    }
}

