package com.quixom.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.InquiryDetail;
import com.quixom.jewelrap.Listener.OnLoadMoreListener;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.NewsItemDetail;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class NewsAdapter extends RecyclerSwipeAdapter {


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
    Uri.Builder builder;
    String deleteUrl,decodeDeleteUrl;
    String url=Global_variable.new_delete_api;
    SharedPreferences sharedPreferences;
    String uniqueid,name,token;
    public NewsAdapter(Context context, int resource, ArrayList<JewelRapItem> jewellist, RecyclerView recyclerView) {
        mcontext = context;
        this.mItems = jewellist;
        this.mActivity = (Activity) context;
        this.comeinquiry=comeinquiry;
        sharedPreferences=context.getSharedPreferences(SessionManager.MyPREFERENCES,Context.MODE_PRIVATE);
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
                    R.layout.news_list, parent, false);

            vh = new ViewHolder(v);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            JewelRapItem jitem = mItems.get(position);
            final String id = jitem.getId();
            ((ViewHolder) holder).swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            ((ViewHolder) holder).swipeLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ViewHolder) holder).swipeLayout.close();
                }
            },50);

            uniqueid=sharedPreferences.getString(SessionManager.UNIQUEID,null);
            name=sharedPreferences.getString(SessionManager.Name,null);
            token=sharedPreferences.getString(SessionManager.TOKEN,null);


            boolean unreadFlag=jitem.isUnread();
           // Log.e("** unread "," "+ unreadFlag);

            if(unreadFlag){
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
                ((ViewHolder) holder).datetime.setLayoutParams(params);
                ((ViewHolder) holder).newOnlineImage.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).newsTitle.setTextColor(mcontext.getResources().getColor(R.color.carbon_black));
            }else{
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f);
                ((ViewHolder) holder).datetime.setLayoutParams(params);
                ((ViewHolder) holder).newOnlineImage.setVisibility(View.GONE);
                ((ViewHolder) holder).newsTitle.setTextColor(mcontext.getResources().getColor(R.color.light_grey));

            }
            ((ViewHolder) holder).newsTitle.setText(jitem.getNewsTitle().toString());
            ((ViewHolder) holder).newsDetails.setText(jitem.getNewDescription().toString());
            ((ViewHolder) holder).datetime.setText(jitem.getDate().toString());
            ((ViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mcontext,NewsItemDetail.class);
                    in.putExtra("news_id", id);
                    mActivity.startActivity(in);
                    mActivity.overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                }
            });

            ((ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //delete url;

                    builder = new Uri.Builder();
                    builder.appendQueryParameter("news",id);
                    builder.appendQueryParameter("jewelrap_id",uniqueid);
                    String myUrl = builder.build().toString();
                    deleteUrl = url + myUrl;
                    try {
                        decodeDeleteUrl = java.net.URLDecoder.decode(deleteUrl, "UTF-8").replace(" ", "");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("** new delete url "," "+ decodeDeleteUrl);

                    RestClientVolley restClientVolley=new RestClientVolley(mcontext, Request.Method.GET, decodeDeleteUrl, new VolleyCallBack() {
                        @Override
                        public void processCompleted(String response) {
                            Log.e("**JEWELRAP" , "news detail: " + response);
                            if(response!=null)
                            {
                                try {
                                    JSONObject object=new JSONObject(response);
                                    String status=object.getString("status");
                                    if(status.equals("ok")){
                                        Toast.makeText(mcontext,object.getString("message"),Toast.LENGTH_SHORT).show();
                                        mItemManger.removeShownLayouts(((ViewHolder) holder).swipeLayout);
                                        mItems.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, mItems.size());
                                        mItemManger.closeAllItems();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // setData(response);
                            }
                        }
                    });
                    restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
                    restClientVolley.executeRequest();
                }
            });
            mItemManger.bindView(
                    ((ViewHolder) holder).itemView, position);
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.newSwipe;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle, newsDetails, datetime;
        public CardView card;
        public  ImageView newOnlineImage,newDeleteImage;
        public SwipeLayout swipeLayout;
        public   LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.text_news_title);
            newsDetails = (TextView) itemView.findViewById(R.id.text_news_details);
            datetime = (TextView) itemView.findViewById(R.id.text_Datetime);
            card = (CardView) itemView.findViewById(R.id.cardView);
            newOnlineImage=(ImageView)itemView.findViewById(R.id.new_image_online);
            //mainlinear = (LinearLayout) itemView.findViewById(R.id.list1);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.list2);
            newDeleteImage = (ImageView) itemView.findViewById(R.id.new_trash_image);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.newSwipe);
        }
    }


}

