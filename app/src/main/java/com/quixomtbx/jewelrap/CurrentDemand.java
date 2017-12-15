package com.quixomtbx.jewelrap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.baoyz.widget.PullRefreshLayout;
import com.quixomtbx.jewelrap.Adapter.InquiriyAdapter;
import com.quixomtbx.jewelrap.Badge.Badges;
import com.quixomtbx.jewelrap.Badge.BadgesNotSupportedException;
import com.quixomtbx.jewelrap.GCM.GCMNotificationIntentService;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Listener.OnLoadMoreListener;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CurrentDemand extends Fragment {
    private static String name = "", token = "", id = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String url = Global_variable.current_demand_api;
    PullRefreshLayout mSwipeRefreshLayout;
    ArrayList<JewelRapItem> itemArrayList;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    InquiriyAdapter inquiriyAdapter;
    String LOADDATA = "";
    String next = "";
    static String status = "", comeinquiry = "", optionSelected = "";
    String TAG = "JEWELRAP";
    int countFragmentVisibility = 1, count = 1;
    boolean visibleToUser;
    Context mContext;

    public CurrentDemand() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        sessionManager = new SessionManager(getActivity());
        if (optionSelected.equals("yes")) {
            countFragmentVisibility = 0;
        }
        clearBadgeCount();
        currentDemandDetails();
        GCMNotificationIntentService.CancelNotification(getActivity(), GCMNotificationIntentService.NOTIFICATION_ID, "inquiry");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

    }

    public void clearBadgeCount() {
        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.POST, Global_variable.badge_api, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                Log.d(TAG, "response : " + response);
                badgeRemoved(response);
            }
        });

        restClientVolley.addParameter("id", id);
        restClientVolley.progressDialogVisibility(true);
        restClientVolley.executeRequest();
    }

    private void badgeRemoved(String response) {

        try {
            Badges.removeBadge(getActivity());

        } catch (BadgesNotSupportedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        optionSelected = "yes";
        Fragment fragment1 = new CurrentDemand();
        switch (item.getItemId()) {
            case R.id.itemAll:
                status = "all";
                if (fragment1 != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.relativelayout_currentdemand, fragment1);
                    fragmentTransaction.commit();
                    comeinquiry = "all";
                }
                break;
            case R.id.itemOpen:
                status = "open";
                if (fragment1 != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.relativelayout_currentdemand, fragment1);
                    fragmentTransaction.commit();
                    comeinquiry = "open";

                }
                break;
            case R.id.itemClosed:
                status = "closed";
                if (fragment1 != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.relativelayout_currentdemand, fragment1);
                    fragmentTransaction.commit();
                    comeinquiry = "closed";

                }

                break;
            case R.id.itemFavorite:
                status = "favorite";
                if (fragment1 != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.relativelayout_currentdemand, fragment1);
                    fragmentTransaction.commit();
                    comeinquiry = "favorite";
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_demand, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemArrayList = new ArrayList<JewelRapItem>();
        mSwipeRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        if (countFragmentVisibility == 0) {
            currentDemandDetails();
            optionSelected = "";
        }
        return view;
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemArrayList.clear();
                if (Global_variable.logEnabled) {
                    Log.e("comeInquiry Val", comeinquiry);
                }
                currentDemandDetails();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    private void currentDemandDetails() {
        String URL;
        if (comeinquiry.equals("open")) {
            URL = url + "&status=1";
            loadData(URL);
        } else if (comeinquiry.equals("closed")) {
            URL = url + "&status=0";
            loadData(URL);
        } else if (comeinquiry.equals("favorite")) {
            URL = url + "&favorite=1";
            loadData(URL);

        } else {
            URL = url;
            loadData(URL);
        }

    }

    private void loadData(String url) {
        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                populateList(response);
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        restClientVolley.executeRequest();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        visibleToUser = isVisibleToUser;
        if (visibleToUser && countFragmentVisibility == 1) {


            if (Global_variable.logEnabled) {
                Log.e("**user visible", "called");
            }
            if (mContext != null) {
                comeinquiry = "";
                if (itemArrayList != null) {
                    itemArrayList.clear();
                }
                currentDemandDetails();
            }
            countFragmentVisibility++;


        }
        setHasOptionsMenu(true);

    }

    private void populateList(String response) {

        JSONObject parentObject = null;
        try {
            parentObject = new JSONObject(response);
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
                    String cat_name = jsonobject.getJSONObject("category").getString("category_name");
                    String cat_img = jsonobject.getJSONObject("category").getString("Inquiry_category_image");
                    String date = jsonobject.optString("date");
                    String fnm = jsonobject.getJSONObject("user").getString("first_name");
                    String lnm = jsonobject.getJSONObject("user").getString("last_name");
                    String id = jsonobject.getString("id");
                    String status = jsonobject.getString("status");
                    String favorite = jsonobject.getString("is_favorite");
                    String firmname = jsonobject.getJSONObject("user").getJSONObject("user_profile").getString("firm_name");
                    JSONArray shaparray = jsonobject.optJSONArray("selected_shape");
                    String name = "";
                    if (shaparray.length() != 0) {
                        for (int j = 0; j < shaparray.length(); j++) {
                            name = name + shaparray.getJSONObject(j).getString("shape_name");
                            if (j != shaparray.length() - 1) {
                                name = name + ",";
                            }
                        }
                    } else {
                        name = "Shape Not Available";
                    }
                    JewelRapItem item = new JewelRapItem();
                    item.setCategoryName(cat_name);
                    item.setShapeTitle(name);
                    item.setStatus(status);
                    item.setFirstName(fnm);
                    item.setLastName(lnm);
                    item.setCategoryImage(cat_img);
                    item.setFavorite(favorite);
                    item.setFirmname(firmname);
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
                    inquiriyAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (count == 1) {
            inquiriyAdapter = new InquiriyAdapter(getActivity(), R.layout.inquiries_list, itemArrayList, recyclerView, "currentDemand");
            recyclerView.setAdapter(inquiriyAdapter);
            count++;
        }

        inquiriyAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LOADDATA = "loaddata";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String loadurl = Global_variable.base_url + next;
                        if (loadurl.equals(Global_variable.base_url + null)) {
                            inquiriyAdapter.setLoaded();
                        } else {
                            loadData(loadurl);
                            inquiriyAdapter.setLoaded();
                        }
                    }
                }, 500);
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
