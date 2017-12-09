package com.quixom.jewelrap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.baoyz.widget.PullRefreshLayout;
import com.quixom.jewelrap.Adapter.ContactAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ContactUsFragment extends Fragment{
    private static String name = "", token = "", id = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<JewelRapItem> itemArrayList;
    SessionManager sessionManager;
    PullRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sharedPreferences;
    public ContactUsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_contact_us, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemArrayList = new ArrayList<JewelRapItem>();
        mSwipeRefreshLayout=(PullRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent();
            }
        });

        contactUsData();

        return  view;
    }

    private void refreshContent()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemArrayList.clear();
                contactUsData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1000);

    }

    public void contactUsData()
    {
        String url= Global_variable.contact_us_api;
        RestClientVolley restClientVolley=new RestClientVolley(getActivity(), Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if(response!=null)
                {
                    populateList(response);
                }
            }
        });
        restClientVolley.executeRequest();
    }

    public void populateList(String response)
    {
        try {
            JSONObject obj=new JSONObject(response);
            if(obj.has("messages"))
            {
                if(Global_variable.logEnabled) {
                    Log.e("Message : ", obj.getString("messages"));
                }
                String msg=obj.optString("messages");
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(),R.style.DesignDemo);
                dlgAlert.setMessage(msg);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.logoutUser();
                                getActivity().overridePendingTransition(R.layout.push_right_out,R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            }else {
                JSONArray arrObj = obj.getJSONArray("objects");
                if (arrObj != null && arrObj.length() > 0) {
                    JewelRapItem item;
                    for (int i = 0; i < arrObj.length(); i++) {
                        JSONObject objItem = arrObj.getJSONObject(i);
                        item = new JewelRapItem();
                        item.setAddress(objItem.getString("address"));
                        item.setEmail(objItem.getString("email"));
                        item.setMobile(objItem.getString("mobile"));
                        item.setWebsite(objItem.getString("website"));
                        itemArrayList.add(item);

                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter=new ContactAdapter(getActivity(),itemArrayList);
        recyclerView.setAdapter(adapter);

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
