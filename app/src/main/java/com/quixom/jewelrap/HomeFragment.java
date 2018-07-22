package com.quixom.jewelrap;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixom.jewelrap.Adapter.CategoryAdapter;
import com.quixom.jewelrap.Adapter.CustomViewPager;
import com.quixom.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixom.jewelrap.Adapter.StockAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Model.Inventory;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment implements View.OnClickListener {
    public static String categoryValue = "", categoryId = "";

    RecyclerView recyclerView, recyclerViewStock;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager layoutMangaerStock;
    RecyclerView.Adapter recyclerAdapter, recyclerStockAdapter;
    CardView cardViewSolitaire, cardViewLD, cardUniversalGems;
    JSONArray jsonArray;
    SessionManager sessionManager;
    int i;
    final int speedScroll = 5000;
    Handler mHandler; /*new Handler(Looper.getMainLooper());*/
    Runnable runnable;
    ArrayList<JewelRapItem> categoryArrayList;
    ArrayList<Inventory> stockList;
    SharedPreferences sharedPreferences;
    String name, token, id, userrole, deviceId, uniqueid;
    Context mcontext;
    Dialog dialog;
    TextView mCounter, mNewsCounter, textCount, textFirmName;
    String TAG = "JEWELRAP";
    String badgecount = "0";
    String version = BuildConfig.VERSION_NAME;
    public static Date dialodTime, recyclerviewTime;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseAnalytics firebaseAnalytics;
    Menu menu;
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    int totalCount;
    public static final String TOTAL_COUNT = "totalcount";
    public int newsCount;
    View indicatorContainer;
    AutoScrollViewPager viewPager;
    LinearLayout layoutIndicator;
    CustomViewPager customViewPagerAdapter;
    FloatingActionMenu floatingActionsMenu;
    FloatingActionButton FAB_solitaire, FAB_loosediamond;
    String shortCutId, shortCutMsg, shortCutFirmName;

    LinearLayout linearLayouthome;
    ImageView volume,mute;
    ImageView left,right;
    MediaPlayer mp;
    String url_paid = Global_variable.paid_api;
    RequestQueue requestQueue;
    // FloatingActionsMenu actionsMenu;
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mcontext = getActivity();
        requestQueue = Volley.newRequestQueue(getContext());
        mp = MediaPlayer.create(mcontext,R.raw.music);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, mcontext.MODE_PRIVATE);

        linearLayouthome=(LinearLayout)view.findViewById(R.id.linearLayouthome);

        textCount = (TextView) view.findViewById(R.id.stockCount);
        textFirmName = (TextView) view.findViewById(R.id.home_text_firm_name);
        name = sharedPreferences.getString(SessionManager.Name, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        deviceId = sharedPreferences.getString(SessionManager.GCMID, null);
        userrole = sharedPreferences.getString(SessionManager.USERROLE, null);



        if (Global_variable.logEnabled) {
            Log.e("userrole", userrole);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerViewStock = (RecyclerView) view.findViewById(R.id.recyclerStock);
        cardViewSolitaire = (CardView) view.findViewById(R.id.home_card_solitaire);
        cardViewLD = (CardView) view.findViewById(R.id.home_card_ld);
        cardUniversalGems = (CardView) view.findViewById(R.id.home_card_universal);
        left=(ImageView)view.findViewById(R.id.previous_button);
        right=(ImageView)view.findViewById(R.id.next_button);
        volume=(ImageView)view.findViewById(R.id.volume);
        mute=(ImageView)view.findViewById(R.id.mute);
//        textViewStock = (TextView) view.findViewById(R.id.home_stock_info);
//        textViewStock.setSelected(true);
        cardViewSolitaire.setOnClickListener(this);
        cardViewLD.setOnClickListener(this);
        cardUniversalGems.setOnClickListener(this);
        sessionManager = new SessionManager(getActivity());
        layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutMangaerStock = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewStock.setLayoutManager(layoutMangaerStock);
        recyclerViewStock.setNestedScrollingEnabled(false);
        stockList = new ArrayList<>();
        recyclerStockAdapter = new StockAdapter(getActivity(), stockList, getActivity());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewStock);
        recyclerViewStock.setAdapter(recyclerStockAdapter);
        categoryArrayList = new ArrayList<JewelRapItem>();
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.BLUE, Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(  new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });

        pref1 = getContext().getSharedPreferences("MyPREFERENCES_LoginCount", 0);
        editor1 = pref1.edit();
        totalCount = pref1.getInt(TOTAL_COUNT, 0);
        getCategory();
        // getHorizontalView();
        setViewPager(view);
        floatingActionsMenu = (FloatingActionMenu) view.findViewById(R.id.fam_search);
        floatingActionsMenu.setOnClickListener(this);
        floatingActionsMenu.setIconAnimated(false);

        /*actionsMenu=(FloatingActionsMenu)view.findViewById(R.id.multiple_actions_left);
        actionsMenu.setOnClickListener(this);*/
        loadPaidData(url_paid);

        autoScroll();
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int firstVisibleItemIndex = layoutMangaerStock.findFirstVisibleItemPosition();
                if (firstVisibleItemIndex > 0) {
                    // linearLayoutManager.smoothScrollToPosition(recyclerViewStock,null,firstVisibleItemIndex-1);
                    layoutMangaerStock.scrollToPosition(firstVisibleItemIndex-1);
                }
            }

        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int totalItemCount = recyclerViewStock.getAdapter().getItemCount();
//         if (totalItemCount <= 0) return;
                int lastVisibleItemIndex = layoutMangaerStock.findLastVisibleItemPosition();

//         if (lastVisibleItemIndex >= totalItemCount) return;
                // linearLayoutManager.smoothScrollToPosition(recyclerViewStock,null,lastVisibleItemIndex+1);
                layoutMangaerStock.scrollToPosition(lastVisibleItemIndex+1);
            }
        });
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp = MediaPlayer.create(mcontext, R.raw.music);
                mp.start();
                volume.setVisibility(View.GONE);
                mute.setVisibility(View.VISIBLE);
            }
        });
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp = MediaPlayer.create(mcontext, R.raw.music);
                mp.stop();
                mute.setVisibility(View.GONE);
                volume.setVisibility(View.VISIBLE);

            }
        });
        return view;
    }


    public void autoScroll() {

        mHandler = new Handler();
        runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count == recyclerStockAdapter.getItemCount())
                    count = 0;
                if (count < recyclerStockAdapter.getItemCount()) {

                    textCount.setText("Solitaire on SALE (" + (count + 1) + "/" + recyclerStockAdapter.getItemCount() + ")");
                    recyclerViewStock.smoothScrollToPosition(++count);
                    mHandler.postDelayed(this, speedScroll);
                }
            }
        };
        mHandler.postDelayed(runnable, speedScroll);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
    }
    private void setViewPager(View view) {
        indicatorContainer = view.findViewById(R.id.pagerBulletIndicatorContainer);
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPagerBullet);
        customViewPagerAdapter = new CustomViewPager(getContext());
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewPager.setAdapter(customViewPagerAdapter);
        indicator.setViewPager(viewPager);
        customViewPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        viewPager.startAutoScroll(5000);
        viewPager.setInterval(5000);
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryArrayList.clear();
                stockList.clear();
                getCategory();
                 /*   Intent intent=new Intent(mcontext,MainActivity.class);
                intent.putExtra("pos", 0);
                startActivity(intent);
                MainActivity.id_pos = 0;
                NavigationDrawerAdapter.selected_item = 0;*/
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);

    }

    public void getCategory() {
        String URL = Global_variable.get_categories_api_new;
        if (Global_variable.logEnabled) {
            Log.e(TAG, "fiknal url : " + URL);
        }
        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, URL, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if (Global_variable.logEnabled) {
                    Log.e(TAG, "response home: " + response);
                }
                populateData(response);
                if (userrole.equals("retailer")) {

                    setHasOptionsMenu(false);

                } else {
                    Log.e("** refresh on count", "valled");
                    setHasOptionsMenu(true);
                    getActivity().supportInvalidateOptionsMenu();
                }
                //   getActivity().supportInvalidateOptionsMenu();
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        Log.d(TAG, "ApiKey " + name + ":" + token);
        restClientVolley.addHeader("X-Android", version);
        Log.d("version" , version);
        restClientVolley.executeRequest();

    }

    private void populateData(String response) {
        String categoryName, categoryImage, categoryId;
        String isSubscribed = "";
        String versioncode = "";
        String firstname, lastname;
        try {
            JSONObject responseObject = new JSONObject(response);
            if (responseObject.has("messages")) {
                if (Global_variable.logEnabled) {
                    Log.e("Message : ", responseObject.getString("messages"));
                }
                String msg = responseObject.optString("messages");


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.DesignDemo);
                dlgAlert.setMessage(msg);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.logoutUser();
                                getActivity().overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();

            } else {
                showStock(responseObject.getJSONArray("jr_stocks"));
                boolean hasBrokerVal = responseObject.getJSONObject("meta").getBoolean("has_broker");
                sessionManager.saveHasBrokerValue(hasBrokerVal);
                shortCutId = responseObject.getJSONObject("meta").getJSONObject("unique_id_home").getString("unique_id");
                shortCutFirmName = responseObject.getJSONObject("meta").getJSONObject("unique_id_home").getString("firm_name");
                shortCutMsg = responseObject.getJSONObject("meta").getJSONObject("unique_id_home").getString("message");
                textFirmName.setText(shortCutFirmName);
                isSubscribed = responseObject.getJSONObject("meta").getString("is_subscribed");
                newsCount = responseObject.getJSONObject("meta").getInt("news_count");
                String newscount = responseObject.getJSONObject("meta").getString("news_count");
                sessionManager.newsCount(newscount);
                NavigationDrawerAdapter.setNewsCount(newscount);
                versioncode = responseObject.getJSONObject("meta").getJSONObject("update").getString("android_version");
                if (Global_variable.logEnabled) {
                    Log.e("version code ", "" + versioncode + " " + version);
                }
                firstname = responseObject.getJSONObject("meta").getString("first_name");
                lastname = responseObject.getJSONObject("meta").getString("last_name");
                sessionManager.username(firstname, lastname);
                MainActivity.setTextUsername(firstname + " " + lastname);
                String url = Global_variable.base_url + responseObject.getJSONObject("meta").getString("profile_image");
                sessionManager.getProfileImage(url);





                Glide.with(mcontext)
                        .load(url)
                        .asBitmap()
                        .error(R.drawable.ic_launcher)
                        .placeholder(R.drawable.ic_launcher)
                        .into(MainActivity.iv_profile_image);




                if (Global_variable.logEnabled) {
                    Log.e("*****user name ", " " + firstname + " " + lastname);
                }
                if (Float.valueOf(versioncode) > Float.valueOf(version)) {
                    Calendar c = Calendar.getInstance();
                    Date time2 = c.getTime();
                    if (Global_variable.logEnabled) {
                        Log.e("get time2 ", " " + time2);
                        Log.e(" total count before ", " " + totalCount);
                        Log.e("get dialogtime ", " " + dialodTime);

                        Log.e("count ", " " + Global_variable.COUNT);
                    }
                    final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                    dlgAlert.setMessage(responseObject.getJSONObject("meta").getJSONObject("update").getString("android_update_message"));
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    redirectplayStore();
                                    getActivity().overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                                }
                            });
                    dlgAlert.setNeutralButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            totalCount++;
                            editor1.putInt(TOTAL_COUNT, totalCount);
                            editor1.commit();
                            if (Global_variable.logEnabled) {
                                Log.e("count incre", " " + Global_variable.COUNT);


                            }
                            dlgAlert.create().dismiss();
                        }
                    });
                    dlgAlert.setCancelable(false);

                    if (totalCount == 0) {
                        dlgAlert.create().show();
                        dialodTime = time2;
                        pref1.edit().putLong("time", dialodTime.getTime()).apply();
                        if (Global_variable.logEnabled) {
                            Log.e("get dialogtime ", " " + dialodTime);
                        }
                    } else {
                        // dialodTime=time2;
                        Date myDate = new Date(pref1.getLong("time", 0));
                        long diff = time2.getTime() - myDate.getTime();
                        long diffMinutes = diff / (60 * 1000);
                        float diffHours = diffMinutes / 60;
                        if (Global_variable.logEnabled) {
                            System.out.println("diff hours" + diffHours);
                            System.out.println("diff hours" + diffMinutes);
                            Log.e("diff min", " " + diffMinutes);
                            Log.e("else time2 ", " " + time2);
                            Log.e("else dialogtime ", " " + dialodTime);
                            Log.e(" my date ", " " + myDate);
                        }
                        if (diffHours > 23) {
                            //Global_variable.COUNT=0;
                            totalCount = 0;
                            editor1.putInt(TOTAL_COUNT, totalCount);
                            editor1.commit();
                            if (Global_variable.logEnabled) {
                                Log.e("get diffMinutes ", " " + diffMinutes);

                            }
                        }
                    }


                }
                if (isSubscribed.equals("false")) {
                    subscriptionExpireAlert();
                } else {
                    jsonArray = responseObject.getJSONArray("objects");
                    Global_variable.searchResponse = jsonArray.getJSONObject(0);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        categoryName = object.getString("category_name");
                        Log.d(TAG, "category Name _________: " + categoryName);
                        categoryImage = object.getString("category_image");
                        categoryId = object.getString("id");
                        badgecount = object.getString("badge_count");
                        JewelRapItem item = new JewelRapItem();
                        item.setCategoryImage(Global_variable.base_url + categoryImage);
                        item.setCategoryName(categoryName);
                        item.setId(categoryId);
                        item.setJsonObjectCategory(object);
                        item.setBadgecount(badgecount);
                        categoryArrayList.add(item);
                    }
                    recyclerAdapter = new CategoryAdapter(getActivity(), categoryArrayList, getActivity());
                    recyclerView.setAdapter(recyclerAdapter);
                    /*Bundle bundle = new Bundle();
                    bundle.putString("user id", id);
                    bundle.putString("jewelrap unique id",uniqueid );
                    bundle.putString("username",name);
                    firebaseAnalytics.logEvent("Login on daily basis", bundle);*/
                   /* Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
                    bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, uniqueid);
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);*/

                    Bundle params = new Bundle();
                    params.putString("jewelrap_id", uniqueid);
                    params.putString("user_name", name);
//                    params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "home_page_visit");
                    firebaseAnalytics.logEvent("open_home_page", params);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        linearLayouthome.setVisibility(View.VISIBLE);
    }

    private void subscriptionExpireAlert() {

        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_subscribedlayout);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        final TextView textView = (TextView) dialog.findViewById(R.id.textview_weblink);
        SpannableString ss = new SpannableString("Oops..! It look like there are some issues with your account. Don't worry, Login on www.jewelrap.com for more details or " +
                "contact us at info@jewelrap.com any time or you can call us at 011-41412275.");
        ClickableSpan spanWebsiteLink = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                TextView tv = (TextView) textView;
                Spanned s = (Spanned) tv.getText();
                int start = s.getSpanStart(this);
                int end = s.getSpanEnd(this);
                String jewelraplink = s.subSequence(start, end).toString();
                String url = jewelraplink;

                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" +
                        jewelraplink));
                Intent chooser = Intent.createChooser(webintent, "Open with");
                dialog.dismiss();
                startActivity(chooser);
                getActivity().finish();
            }
        };
        ClickableSpan spanEmail = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                TextView tv = (TextView) textView;
                Spanned s = (Spanned) tv.getText();
                int start = s.getSpanStart(this);
                int end = s.getSpanEnd(this);
                String emailLink = s.subSequence(start, end).toString();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", emailLink, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                dialog.dismiss();
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                getActivity().finish();
            }
        };
        ClickableSpan spanContactNo = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                TextView tv = (TextView) textView;
                Spanned s = (Spanned) tv.getText();
                int start = s.getSpanStart(this);
                int end = s.getSpanEnd(this);
                String contactNoLink = s.subSequence(start, end).toString();

                String message = "tel:" + contactNoLink;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(message));
                dialog.dismiss();
                startActivity(intent);

            }
        };


        ss.setSpan(spanWebsiteLink, 84, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(spanEmail, 135, 152, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(spanContactNo, 184, 196, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.show();
    }

    void showStock(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Inventory inventory = new Inventory();
            inventory.setId(jsonObject.getString("id"));
            inventory.setStockInfo(jsonObject.getString("value"));
            stockList.add(inventory);
        }
        recyclerStockAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_badge, menu);
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.badge).getActionView();
        ImageView imageView = (ImageView) badgeLayout.findViewById(R.id.badge_icon_button);
        mCounter = (TextView) badgeLayout.findViewById(R.id.badge_textView);


        Log.e("** badge length", "" + mCounter.length());
        if (Global_variable.logEnabled) {
            Log.e("** badge count", badgecount);
        }

        if (Integer.valueOf(badgecount) > 0) {
            mCounter.setVisibility(View.VISIBLE);
            if (badgecount.length() > 1) {
                mCounter.setText(badgecount.toString());
            } else {
                mCounter.setText("0" + badgecount.toString());
            }
           /* if(mCounter.length()>3){
                mCounter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
            }else {
                mCounter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            }*/
        } else {
            mCounter.setVisibility(View.GONE);

        }

        RelativeLayout newsBadgeLayout = (RelativeLayout) menu.findItem(R.id.newbadge).getActionView();
        ImageView newsimageView = (ImageView) newsBadgeLayout.findViewById(R.id.badge_icon_button);
        mNewsCounter = (TextView) newsBadgeLayout.findViewById(R.id.badge_textView);
        if (Global_variable.logEnabled) {
            Log.e("** newsCount count", "" + newsCount);
        }
        if (newsCount > 0) {
            mNewsCounter.setVisibility(View.VISIBLE);
            mNewsCounter.setText(String.valueOf(newsCount));

        } else {
            mNewsCounter.setVisibility(View.GONE);
            newsimageView.setVisibility(View.GONE);
        }
        super.onCreateOptionsMenu(menu, inflater);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, MainActivity.class);
                intent.putExtra("pos", 0);
                startActivity(intent);
                MainActivity.id_pos = 1;
                NavigationDrawerAdapter.selected_item = 1;
            }
        });
        newsimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, MainActivity.class);
                //  intent.putExtra("pos", 0);
                startActivity(intent);
                MainActivity.id_pos = 4;
                NavigationDrawerAdapter.selected_item = 4;
            }
        });
    }

    public void redirectplayStore() {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = getActivity().getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
            startActivity(webIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fam_search:
                floatingActionsMenu.setClosedOnTouchOutside(true);
                break;
            case R.id.home_card_solitaire:
                Intent intent = new Intent(mcontext, MainActivity.class);
                startActivity(intent);
                MainActivity.id_pos = 2;
                NavigationDrawerAdapter.selected_item = 2;
                break;
            case R.id.home_card_ld:
                Intent intent1 = new Intent(mcontext, MainActivity.class);
                intent1.putExtra("searchpos", 2);
                startActivity(intent1);
                MainActivity.id_pos = 2;
                NavigationDrawerAdapter.selected_item = 2;
                break;

            case R.id.home_card_universal:
                if (shortCutId.equalsIgnoreCase("00000")) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.DesignDemo);
                    dlgAlert.setMessage(shortCutMsg);
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.setCancelable(false);
                    dlgAlert.create().show();
                } else {
                    Intent newIntent = new Intent(mcontext, MainActivity.class);
                    newIntent.putExtra("universalgems", true);
                    newIntent.putExtra("shortcut_unique_id", shortCutId);
                    startActivity(newIntent);
                    MainActivity.id_pos = 2;
                    NavigationDrawerAdapter.selected_item = 2;
                }
                break;

           /* case R.id.multiple_actions_left:
                actionsMenu.collapse();
                break;*/
        }


    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
       *//*switch (item.getItemId()){
           case R.id.badge:

           break;
       }*//*
        RelativeLayout badgelayout=(RelativeLayout)item.
                return super.onOptionsItemSelected(item);
    }*/


    public void loadPaidData(String url) {

        Log.e("qq","q    "+url );
        Log.e("qwedsa","ApiKey " + name + ":" + token);
        String a = url+"?GCM_Token="+name + ":" + token;
//        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, url, new VolleyCallBack() {
//            @Override
//            public void processCompleted(String response) {
//                try {
//                    Log.e("qqqaaa1","q");
//                    JSONObject parentObject = new JSONObject(response);
//                    Log.e("qqqaaa","q"+parentObject);
//                    Toast.makeText(getActivity(), ""+parentObject, Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("exceptionb",""+e);
//                }
//            }
//        });
//        restClientVolley.addHeader("Authorization",  "ApiKey " +name + ":" + token);
//        Log.e("xxx",""+("Authorization"+ name + ":" + token));
//        restClientVolley.executeRequest();


//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("qqqaaa1","q   "+response);
//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("exceptionb","e     "+error.toString());
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//               // params.put("Accept","application/json");
//               // params.put("Content-Type","application/json");
//               // params.put("X-Ios","3.4");
//               // params.put("Accept","application/vnd");
//                params.put("Authorization","ApiKey " +name+ ":" +token);
//                Log.e("headerhome",""+params);
//                return params;
//
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return super.getBodyContentType();
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                return params;
//            }
//
//        }
//        ;
//        requestQueue.add(stringRequest);




    }


}
