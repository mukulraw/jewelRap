package com.quixomtbx.jewelrap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.quixomtbx.jewelrap.Adapter.StockImageAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.quixomtbx.jewelrap.R.id.detailLab;


public class InvetoryDetail extends AppCompatActivity implements View.OnClickListener {

    String TAG = this.getClass().getSimpleName();
    String stock_id;
    RecyclerView recyclerView;
    RecyclerView.Adapter imageAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<String> imageList;
    JSONObject stockObject;
    TextView shape, stockID, certNumber, lab, size, color, purity, fluor, meas, cut, pol, sym, ta, td, imagePosition;
    ExpandableTextView stockComments;
    Toolbar toolbar;
    TextView toolbartitle;
    SharedPreferences sharedPreferences;
    String userName, token;
    String version = BuildConfig.VERSION_NAME;
    LinearLayout layoutPhone;
    TextView textPhone;
    SnapHelper snapHelper;
    ImageView imageView;

    ScrollView scrollViewstockdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invetory_detail);

        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, this.MODE_PRIVATE);
        userName = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);


        scrollViewstockdetails=(ScrollView)findViewById(R.id.scrollViewStockDetails);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Stock Details");

        textPhone = (TextView) findViewById(R.id.textPhone);
        imageView = (ImageView) findViewById(R.id.img_stock);

        recyclerView = (RecyclerView) findViewById(R.id.rv_stockImage);
        layoutPhone = (LinearLayout) findViewById(R.id.callPhone);
        layoutPhone.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        stockComments = (ExpandableTextView) findViewById(R.id.expand_text_view);
        recyclerView.setLayoutManager(layoutManager);
        imageList = new ArrayList<>();
        imageAdapter = new StockImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        shape = (TextView) findViewById(R.id.detailShape);
        stockID = (TextView) findViewById(R.id.detailStockID);
        certNumber = (TextView) findViewById(R.id.detailCert);
        lab = (TextView) findViewById(detailLab);
        size = (TextView) findViewById(R.id.detailSize);
        color = (TextView) findViewById(R.id.detailColor);
        purity = (TextView) findViewById(R.id.detailPurity);
        fluor = (TextView) findViewById(R.id.detailFlour);
        meas = (TextView) findViewById(R.id.detailMeas);
        cut = (TextView) findViewById(R.id.detailCut);
        pol = (TextView) findViewById(R.id.detailPol);
        sym = (TextView) findViewById(R.id.detailSym);
        ta = (TextView) findViewById(R.id.detailTA);
        td = (TextView) findViewById(R.id.detailTD);
        imagePosition = (TextView) findViewById(R.id.imagePosition);

        Intent intent = getIntent();
        stock_id = intent.getStringExtra("stock_id");

        fetchStockDetail(Global_variable.invetory_api_details + stock_id + File.separator);
        certNumber.setOnClickListener(this);
        certNumber.setPaintFlags(certNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(layoutManager);
                    int pos = layoutManager.getPosition(centerView);
                    imagePosition.setText((pos + 1) + "/" + imageList.size());
                }
            }
        });
    }


    void fetchStockDetail(String url) {
        RestClientVolley restClientVolley = new RestClientVolley(InvetoryDetail.this, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                parseData(response);
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + userName + ":" + token);
        restClientVolley.executeRequest();
    }

    void parseData(String response) {
        try {
            JSONObject stockObject = new JSONObject(response);
            shape.setText(stockObject.getString("shape"));
            stockID.setText(stockObject.getString("stock"));
            certNumber.setText(stockObject.getString("certificate_num"));
            lab.setText(stockObject.getString("lab"));
            size.setText(stockObject.getString("size"));
            color.setText(stockObject.getString("color"));
            purity.setText(stockObject.getString("purity"));
            fluor.setText(stockObject.getString("fluorescence"));
            meas.setText(stockObject.getString("measurement"));
            cut.setText(stockObject.getString("cut_grade"));
            pol.setText(stockObject.getString("polish"));
            sym.setText(stockObject.getString("symmetry"));
            ta.setText(stockObject.getString("ta"));
            td.setText(stockObject.getString("td"));
            textPhone.setText(stockObject.getString("sell_contact"));
            stockComments.setText(stockObject.getString("member_comment"));


            JSONArray jsonArray = stockObject.getJSONArray("images");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    imageList.add(Global_variable.base_url + File.separator + jsonArray.get(i).toString());
                }
                imageView.setVisibility(View.GONE);
                imagePosition.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageAdapter.notifyDataSetChanged();
        imagePosition.setText("1/" + imageList.size());
        scrollViewstockdetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callPhone:
                callPhone(textPhone.getText().toString());
                break;
            case R.id.detailCert:
                if (lab.getText().toString().trim().equalsIgnoreCase("GIA")) {
                    String gia_url = "https://www.gia.edu/report-check?reportno=" + certNumber.getText().toString().trim();
                    openURL(gia_url);
                } else if (lab.getText().toString().trim().equalsIgnoreCase("IGI")) {
                    String igi_uri = "http://www.igiworldwide.com/verify.php?r=" + certNumber.getText().toString().trim();
                    openURL(igi_uri);
                } else if (lab.getText().toString().trim().equalsIgnoreCase("HRD")) {
                    RestClientVolley restClientVolley = new RestClientVolley(this, Request.Method.POST, "https://my.hrdantwerp.com/", new VolleyCallBack() {
                        @Override
                        public void processCompleted(String response) {
                            Intent newIntent = new Intent(InvetoryDetail.this, HRDActivity.class);
                            newIntent.putExtra("response", response);
                            startActivity(newIntent);
                        }
                    });
                    restClientVolley.addParameter("record_number", certNumber.getText().toString().trim());
                    restClientVolley.executeRequest();
                }
                break;
        }
    }

    void callPhone(String phone) {
        String message = "tel:" + phone;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(message));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    void openURL(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
