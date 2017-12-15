package com.quixomtbx.jewelrap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.quixomtbx.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixomtbx.jewelrap.Adapter.SearchListAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Listener.OnLoadMoreListener;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class SolitaireSearchList extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RecyclerView recyclerSearchList;
    LinearLayoutManager layoutManager;
    ArrayList<JewelRapItem> jitem = new ArrayList<JewelRapItem>();
    RecyclerView.Adapter adapter;
    SearchListAdapter searchListAdapter;
    String url = Global_variable.search_api;
    String shape, certificate, color, purity, size_to, size_from, next, searchid;
    String serachurl, decodesUrl, decodessortUrl, decodessortpricesUrl;
    String categoryName;
    TextView toolbartitle, tv_sortbydiscount, tv_sortbyprice;
    int count = 1;
    LinearLayout mRevealView;
    boolean hidden = true;
    RestClientVolley restClientVolley;
    SharedPreferences sharedPreferences;
    String userid;
    Uri.Builder builder;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, MODE_PRIVATE);
        sessionManager=new SessionManager(SolitaireSearchList.this);
        userid = sharedPreferences.getString(SessionManager.Id, null);
        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.INVISIBLE);
        tv_sortbydiscount = (TextView) findViewById(R.id.sortby_discount);
        tv_sortbyprice = (TextView) findViewById(R.id.sortby_price);
        tv_sortbyprice.setOnClickListener(this);
        tv_sortbydiscount.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Diamond List");
        recyclerSearchList = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        recyclerSearchList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSearchList.setLayoutManager(layoutManager);
        Intent in = getIntent();
        shape = in.getStringExtra("shape");
        certificate = in.getStringExtra("certificate");
        color = in.getStringExtra("color");
        purity = in.getStringExtra("purity");
        size_to = in.getStringExtra("size_to");
        size_from = in.getStringExtra("size_from");
        searchid = in.getStringExtra("searchid");
        categoryName=in.getStringExtra("category_name");
        builder = new Uri.Builder();
        parameterDetails();


        String myUrl = builder.build().toString();
        serachurl = url + myUrl;

        try {
            decodesUrl = java.net.URLDecoder.decode(serachurl, "UTF-8").replace(" ", "");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(Global_variable.logEnabled) {
            Log.e("**decode url", decodesUrl);
        }
        SearchListData(decodesUrl, false);
    }


    void SearchListData(String url, final boolean clearData) {
        restClientVolley = new RestClientVolley(SolitaireSearchList.this, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if (clearData) {
                    jitem.clear();

                    populateList(response);
                } else {
                    populateList(response);
                }
                if(Global_variable.logEnabled) {
                    Log.e("**response search", response);
                }
            }
        });

        restClientVolley.executeRequest();
        builder.clearQuery();
    }

    @Override
    protected void onPause() {
        super.onPause();
        restClientVolley.onPause();
    }

    private void populateList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("multiple_login"))
            {
                if(Global_variable.logEnabled) {
                    Log.e("Message : ", jsonObject.getString("messages"));
                }
                String msg=jsonObject.optString("messages");


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SolitaireSearchList.this,R.style.DesignDemo);
                dlgAlert.setMessage(msg);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                              sessionManager.logoutUser();
                              overridePendingTransition(R.layout.push_right_out,R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();

            }

            next = jsonObject.getJSONObject("meta").getString("next");
            JSONArray jsonArray = jsonObject.getJSONArray("objects");

            if (jsonArray.length() > 0) {
                String purityname = null, shapename = null, certificatename = null, colorname = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject subjsonObject = jsonArray.getJSONObject(i);
                    if (!subjsonObject.isNull("shape")) {
                        shapename = subjsonObject.getJSONObject("shape").optString("shape_name");
                    }
                    if (!subjsonObject.isNull("certification")) {
                        certificatename = subjsonObject.getJSONObject("certification").optString("certified_name");
                    } else {
                        certificatename = "-NA-";
                    }
                    if (!subjsonObject.isNull("purity")) {
                        JSONObject jsonpurity = subjsonObject.getJSONObject("purity");
                        purityname = jsonpurity.optString("purity_name");
                    } else {
                        purityname = "-NA-";
                    }
                    if (!subjsonObject.isNull("color")) {
                        colorname = subjsonObject.getJSONObject("color").optString("color_name");
                    } else {
                        colorname = "-NA-";
                    }
                    String size = subjsonObject.optString("size");
                    String discount = subjsonObject.getString("discount");
                    String price = subjsonObject.getString("price");
                    String id = subjsonObject.getString("id");
                    String cut = subjsonObject.getString("cut_grade");
                    String polish = subjsonObject.getString("polish");
                    String sym = subjsonObject.getString("symmetry");
                    String fluor = subjsonObject.getString("fluorescence");
                    String firstName = subjsonObject.getJSONObject("user").getString("first_name");
                    String lastName = subjsonObject.getJSONObject("user").getString("last_name");
                    String firmname=subjsonObject.getJSONObject("user").getJSONObject("user_profile").getString("firm_name");
                    JewelRapItem item = new JewelRapItem();
                    item.setCategoryName(categoryName);
                    item.setShapeTitle(shapename);
                    item.setCertificate(certificatename);
                    item.setColor(colorname);
                    item.setPurity(purityname);
                    item.setSize(size);
                    item.setPrice(price);
                    item.setId(id);
                    item.setCut(cut);
                    item.setPolish(polish);
                    item.setSym(sym);
                    item.setFirstName(firstName);
                    item.setLastName(lastName);
                    item.setFirmname(firmname);
                    item.setFluor(fluor);
                    item.setMeas(subjsonObject.optString("measurement"));
                    item.setDiscount(discount);
                    item.setCertificateNumber(subjsonObject.optString("certificate_num"));
                    item.setCity(subjsonObject.optString("city"));
                    item.setJsonObject(subjsonObject);
                    jitem.add(item);
                }
                if (count > 1) {
                    searchListAdapter.notifyDataSetChanged();
                }

                if (count == 1) {
                    searchListAdapter = new SearchListAdapter(getApplicationContext(), jitem, recyclerSearchList, this);
                    recyclerSearchList.setAdapter(searchListAdapter);
                    count++;
                }

                searchListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String loadurl = Global_variable.base_url + next;
                                if (loadurl.equals(Global_variable.base_url + null)) {
                                    searchListAdapter.setLoaded();
                                } else {
                                    SearchListData(loadurl, false);
                                    searchListAdapter.setLoaded();
                                }
                            }

                        }, 500);
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DesignDemo);
                builder.setMessage("No results found")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                                in.putExtra("fragment_id", 2);
                                startActivity(in);
                                overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                                NavigationDrawerAdapter.selected_item = 2;
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sortby, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSortBy:
                int cx = (mRevealView.getLeft() + mRevealView.getRight());
                int cy = mRevealView.getTop();
                int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

                //Below Android LOLIPOP Version
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    if (hidden) {
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        mRevealView.setVisibility(View.VISIBLE);
                        animator.start();
                        hidden = false;
                    } else {
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                mRevealView.setVisibility(View.GONE);
                                hidden = true;

                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
                        animator.start();
                    }
                }
                // Android LOLIPOP And ABOVE Version
                else {
                    if (hidden) {
                        Animator anim = android.view.ViewAnimationUtils.
                                createCircularReveal(mRevealView, cx, cy, 0, radius);
                        mRevealView.setVisibility(View.VISIBLE);
                        anim.start();
                        hidden = false;
                    } else {
                        Animator anim = android.view.ViewAnimationUtils.
                                createCircularReveal(mRevealView, cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mRevealView.setVisibility(View.GONE);
                                hidden = true;
                            }
                        });
                        anim.start();
                    }
                }
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.putExtra("fragment_id", 2);
        in.putExtra("searchpos",0);
        startActivity(in);*/
        overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
        //  NavigationDrawerAdapter.selected_item = 2;
    }

    @Override
    public void onClick(View view) {
        hideRevealView();
        switch (view.getId()) {
            case R.id.sortby_discount:
                builder.clearQuery();
                Toast.makeText(getApplicationContext(), "discount", Toast.LENGTH_SHORT).show();
                parameterDetails();
                builder.appendQueryParameter("order_by", "discount");
                String DiscountUrl = builder.build().toString();
                String sorturl = url + DiscountUrl;
                try {
                decodessortUrl = java.net.URLDecoder.decode(sorturl, "UTF-8").replace(" ", "");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
                if(Global_variable.logEnabled) {
                    Log.e("***sorturl", "" + decodessortUrl);
                }
                SearchListData(decodessortUrl, true);

                break;
            case R.id.sortby_price:
                builder.clearQuery();
                Toast.makeText(getApplicationContext(), "price", Toast.LENGTH_SHORT).show();
                parameterDetails();
                builder.appendQueryParameter("order_by", "price");
                String priceUrl = builder.build().toString();
                String sorturlbyprice = url + priceUrl;
                try {
                    decodessortpricesUrl = java.net.URLDecoder.decode(sorturlbyprice, "UTF-8").replace(" ", "");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(Global_variable.logEnabled) {
                    Log.e("**** sort by price", decodessortpricesUrl);
                }
                SearchListData(decodessortpricesUrl, true);
                // SearchListData(url+"?order_by=price",true);

                break;
        }
    }

    public void parameterDetails() {
        if (searchid != null && !searchid.equals("null") && !searchid.equals("")) {
            builder.appendQueryParameter("user__user_profile__unique_id__in", searchid);
        }
        if (shape != null && !shape.equals("")) {
            builder.appendQueryParameter("shape__in", shape);
        }
        if (certificate != null && !certificate.equals("")) {
            builder.appendQueryParameter("certification__in", certificate);
        }
        if (color != null && !color.equals("")) {
            builder.appendQueryParameter("color__in", color);
        }
        if (purity != null && !purity.equals("")) {
            builder.appendQueryParameter("purity__in", purity);
        }
        if ((size_from != null && !size_from.equals("")) && (size_to != null && !size_to.equals(""))) {
            builder.appendQueryParameter("size__range", size_from + "," + size_to);
        }
    }
}
