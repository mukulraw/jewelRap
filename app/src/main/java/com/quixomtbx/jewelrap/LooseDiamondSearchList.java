package com.quixomtbx.jewelrap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.quixomtbx.jewelrap.Adapter.LDSearchListAdapter;
import com.quixomtbx.jewelrap.Adapter.NavigationDrawerAdapter;
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

public class LooseDiamondSearchList extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RecyclerView recyclerLDSearchList;
    LinearLayoutManager layoutManager;
    ArrayList<JewelRapItem> jewelRapItems = new ArrayList<JewelRapItem>();
    RecyclerView.Adapter adapter;
    LDSearchListAdapter loosediamondSearchListAdapter;
    String url = Global_variable.ld_search_api;
    String shape, size, color, purity, shade,searchid;
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
    String next,name,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loose_diamond_search_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, MODE_PRIVATE);
        sessionManager=new SessionManager(LooseDiamondSearchList.this);
        userid = sharedPreferences.getString(SessionManager.Id, null);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);
        tv_sortbydiscount = (TextView) findViewById(R.id.sortby_discount);
        tv_sortbyprice = (TextView) findViewById(R.id.sortby_price);
        tv_sortbyprice.setOnClickListener(this);
        tv_sortbydiscount.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Loose Diamond List");
        recyclerLDSearchList = (RecyclerView) findViewById(R.id.recyclerViewLDSearch);
        recyclerLDSearchList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerLDSearchList.setLayoutManager(layoutManager);

        Intent in = getIntent();
        shape = in.getStringExtra("shape");
        size = in.getStringExtra("size");
        color = in.getStringExtra("color");
        purity = in.getStringExtra("purity");
        shade = in.getStringExtra("shade");
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
        SearchLooseDiamondListData(decodesUrl, false);
    }

    private void SearchLooseDiamondListData(String url,final boolean clearData){
       // Log.e("JEWELRAP "," search url : "+url );
        restClientVolley = new RestClientVolley(LooseDiamondSearchList.this, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if (clearData) {
                    jewelRapItems.clear();

                    populateList(response);
                } else {
                    populateList(response);
                }
                if(Global_variable.logEnabled) {
                    Log.e("**response search", response);
                }
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        restClientVolley.executeRequest();
        builder.clearQuery();
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


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LooseDiamondSearchList.this,R.style.DesignDemo);
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
           // Log.e("JEWELRAP","json array " + jsonArray);
            if (jsonArray.length() > 0) {
                String purityname = null, shapename = null, shadename = null, colorname = null,size=null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject subjsonObject = jsonArray.getJSONObject(i);
                  //  Log.e("JEWELRAP "," json obj response : "+ subjsonObject);
                    if (!subjsonObject.isNull("shape")) {
                        shapename = subjsonObject.getJSONObject("shape").optString("shape_name");
                    }
                    if (!subjsonObject.isNull("shade")) {
                        shadename = subjsonObject.getJSONObject("shade").optString("shade_name");
                    } else {
                        shadename = "-NA-";
                    }
                    if (!subjsonObject.isNull("purity_rng")) {
                        purityname=subjsonObject.getString("purity_rng");
                        /*JSONArray purityArray=subjsonObject.getJSONArray("purity");
                       // JSONObject jsonpurity = subjsonObject.getJSONObject("purity");
                        if(purityArray.length() > 0) {
                            purityname = purityArray.getString(0);
                         //   Log.e("JEWELRAP", " purity name ::" + purityname);
                        }else{
                            purityname = "-NA-";
                        }*/

                    } else {
                        purityname = "-NA-";
                    }
                    if (!subjsonObject.isNull("color_rng")) {
                        colorname=subjsonObject.getString("color_rng");
                        /*JSONArray colorArray=subjsonObject.getJSONArray("color");
                        //Log.e("JEWELRAP"," color array ::"+colorArray.get(0));
                        if(colorArray.length()>0) {
                            colorname = colorArray.getString(0);
                        }else{
                            colorname="-NA-";
                        }*/
                        //Log.e("JEWELRAP"," color name ::"+colorname);
                       // colorname = subjsonObject.getJSONObject("color").optString("color_name");
                    } else {
                        colorname = "-NA-";
                    }

                    if (!subjsonObject.isNull("size")) {
                        size = subjsonObject.getJSONObject("size").optString("size_name");
                    } else {
                        size = "-NA-";
                    }
                    //String size = subjsonObject.optString("size");
                    //String discount = subjsonObject.getString("discount");
                    String price = subjsonObject.getString("price");
                    String id = subjsonObject.getString("id");
                   // Log.e("JEWELRAP ", " shape " +shadename);
                    JewelRapItem item = new JewelRapItem();
                    item.setCategoryName(categoryName);
                    item.setShapeTitle(shapename);
                    item.setShade(shadename);
                    item.setColor(colorname);
                    item.setPurity(purityname);
                    item.setSize(size);
                    item.setPrice(price);
                    item.setId(id);
                    item.setJsonObject(subjsonObject);
                    jewelRapItems.add(item);
                }
                if (count > 1) {
                    loosediamondSearchListAdapter.notifyDataSetChanged();
                }

                if (count == 1) {
                    loosediamondSearchListAdapter = new LDSearchListAdapter(getApplicationContext(), jewelRapItems, recyclerLDSearchList, this);
                    recyclerLDSearchList.setAdapter(loosediamondSearchListAdapter);
                    count++;
                }

                loosediamondSearchListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String loadurl = Global_variable.base_url + next;
                                if (loadurl.equals(Global_variable.base_url + null)) {
                                    loosediamondSearchListAdapter.setLoaded();
                                } else {
                                    SearchLooseDiamondListData(loadurl, false);
                                    loosediamondSearchListAdapter.setLoaded();
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
                                in.putExtra("searchpos",1);
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
                SearchLooseDiamondListData(decodessortUrl, true);

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
                SearchLooseDiamondListData(decodessortpricesUrl, true);
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
        if (size != null && !size.equals("")) {
            builder.appendQueryParameter("size__in", size);
        }
        if (color != null && !color.equals("")) {
            builder.appendQueryParameter("color__in", color);
        }
        if (purity != null && !purity.equals("")) {
            builder.appendQueryParameter("purity__in", purity);
        }
        if (shade != null && !shade.equals("")) {
            builder.appendQueryParameter("shade__in", shade);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        restClientVolley.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ld, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

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

        overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
        //NavigationDrawerAdapter.selected_item = 2;
    }
}
