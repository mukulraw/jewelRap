package com.quixom.jewelrap;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.baoyz.widget.PullRefreshLayout;
import com.quixom.jewelrap.Adapter.NewsAdapter;
import com.quixom.jewelrap.GCM.GCMNotificationIntentService;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Listener.OnLoadMoreListener;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<JewelRapItem> itemArrayList;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    private static String name = "", token = "", id = "";
    PullRefreshLayout mSwipeRefreshLayout;
    String url = Global_variable.news_api;
    String next = "";
    int count = 1;
    NewsAdapter newsAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        sessionManager = new SessionManager(getActivity());
        GCMNotificationIntentService.CancelNotification(getActivity(), GCMNotificationIntentService.NOTIFICATION_ID, "news");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemArrayList = new ArrayList<JewelRapItem>();
        mSwipeRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColor(getResources().getColor(R.color.colorPrimary));
        loadNewsData(url);
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent();
            }
        });
        return view;
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemArrayList.clear();
                loadNewsData(url);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    public void loadNewsData(String url) {
        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                Log.e("qqqaaa12","q"+response);
                populateList(response);
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        restClientVolley.executeRequest();

    }

    private void populateList(String response) {

        try {
            JSONObject parentObject = new JSONObject(response);
            if (parentObject.has("multiple_login")) {
                if (Global_variable.logEnabled) {
                    Log.e("Message : ", parentObject.getString("messages"));
                }
                String msg = parentObject.optString("messages");


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
                next = parentObject.getJSONObject("meta").getString("next");
                JSONArray objectarray = parentObject.getJSONArray("objects");
                for (int i = 0; i < objectarray.length(); i++) {
                    JSONObject jsonobject = objectarray.getJSONObject(i);
                    String title = jsonobject.getString("title");
                    String date = jsonobject.optString("date");
                    String description = jsonobject.getString("description");
                    String id = jsonobject.getString("id");
                    boolean unreadFlag = jsonobject.getBoolean("first_time");

                    JewelRapItem item = new JewelRapItem();
                    item.setNewsTitle(title);
                    item.setNewDescription(description);
                    item.setUnread(unreadFlag);
                    if (date.equals("")) {
                        date = "Date Not Avaliable";
                        item.setDate(date);
                    } else {
                        item.setDate(date);
                    }
                    item.setId(id);
                    itemArrayList.add(item);
                }
                if (count > 1) {
                    newsAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (count == 1) {

            newsAdapter = new NewsAdapter(getActivity(), R.layout.news_list, itemArrayList, recyclerView);
            recyclerView.setAdapter(newsAdapter);
            count++;
        }

        newsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // LOADDATA = "loaddata";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String loadurl = Global_variable.base_url + next;
                        if (loadurl.equals(Global_variable.base_url + null)) {
                            newsAdapter.setLoaded();
                        } else {
                            loadNewsData(loadurl);
                            newsAdapter.setLoaded();
                        }
                    }

                }, 500);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
