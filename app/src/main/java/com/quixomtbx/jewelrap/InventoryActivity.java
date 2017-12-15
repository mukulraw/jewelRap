package com.quixomtbx.jewelrap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.quixomtbx.jewelrap.Adapter.InventoryAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Model.Inventory;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    String invetoryURL = Global_variable.invetory_api;
    String version = BuildConfig.VERSION_NAME;
    SharedPreferences sharedPreferences;
    String userName, token, nextURL, stockID;
    String TAG = this.getClass().getSimpleName();
    TextView toolbartitle;
    Toolbar toolbar;
    RecyclerView recyclerViewStock;
    RecyclerView.Adapter invetoryAdapter;
    ArrayList<Inventory> inventoryArrayList;
    LinearLayoutManager layoutManager;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Intent intent = getIntent();
        stockID = intent.getStringExtra("stock_id");


        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, this.MODE_PRIVATE);
        userName = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        recyclerViewStock = (RecyclerView) findViewById(R.id.recyclerViewStock);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewStock.setLayoutManager(layoutManager);
        inventoryArrayList = new ArrayList<>();
        invetoryAdapter = new InventoryAdapter(this, inventoryArrayList);
        recyclerViewStock.setAdapter(invetoryAdapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Solitaire on SALE");
        fetchInventory(invetoryURL);

        if (stockID != null) {
            Intent newIntent = new Intent(InventoryActivity.this, InvetoryDetail.class);
            newIntent.putExtra("stock_id", stockID);
            startActivity(newIntent);
            overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
        }

        recyclerViewStock.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerViewStock.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    if (nextURL != null) {
                        fetchInventory(Global_variable.base_url + nextURL);
                    }
                    loading = true;
                }
            }
        });
    }

    public void fetchInventory(String url) {
        RestClientVolley restClientVolley = new RestClientVolley(InventoryActivity.this, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {

                parseJsonData(response);
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + userName + ":" + token);
        restClientVolley.addHeader("X-Android", version);
        restClientVolley.executeRequest();
    }

    public void parseJsonData(String response) {
        try {
            final JSONObject jsonObject = new JSONObject(response);
            nextURL = jsonObject.getJSONObject("meta").getString("next");
            JSONArray jsonArray = jsonObject.getJSONArray("objects");
            for (int i = 0; i < jsonArray.length(); i++) {
                Inventory inventory = new Inventory();
                JSONObject stockObject = jsonArray.getJSONObject(i);
                inventory.setStockObject(stockObject);
                inventory.setStockInfo(stockObject.getString("shape") + " " + stockObject.getString("size") + ", " + stockObject.getString("color") + ", " + stockObject
                        .getString("purity") + ", " + stockObject.getString("lab"));
                inventory.setCut(stockObject.getString("cut_grade"));
                inventory.setFluor(stockObject.getString("fluorescence"));
                inventory.setPolish(stockObject.getString("polish"));
                inventory.setMeas(stockObject.getString("measurement"));
                inventory.setSymmetry(stockObject.getString("symmetry"));
                inventory.setCertNumber(stockObject.getString("certificate_num"));
                inventory.setId(stockObject.getString("id"));
                if (stockObject.getJSONArray("images").length() > 0) {
                    for (int j = 0; j < stockObject.getJSONArray("images").length(); j++) {
                        inventory.setImage(stockObject.getJSONArray("images").get(j).toString());
                    }
                }
                inventoryArrayList.add(inventory);
            }
            invetoryAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
        MainActivity.id_pos = 0;
        overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
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


}
