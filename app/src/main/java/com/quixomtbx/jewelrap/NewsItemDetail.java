package com.quixomtbx.jewelrap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.quixomtbx.jewelrap.GCM.GCMNotificationIntentService;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsItemDetail extends AppCompatActivity {
    TextView textView_title,textView_description,textView_date;
    String title,description,date,id, token,name;
    String url= Global_variable.news_api;
    Toolbar toolbar;
    TextView toolbartitle;
    SharedPreferences sharedPreferences;
    LinearLayout linearLayoutNotificationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_news_item_detail);
        initComponents();
        Intent in=getIntent();
        id=in.getStringExtra("news_id");
        GCMNotificationIntentService.CancelNotification(NewsItemDetail.this, GCMNotificationIntentService.NOTIFICATION_ID,"news");
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        name = sharedPreferences.getString(SessionManager.Name, null);
        NewsDetail();

    }

    public void initComponents()
    {
        textView_title=(TextView)findViewById(R.id.tv_title);
        textView_description=(TextView)findViewById(R.id.tv_description);
        textView_date=(TextView)findViewById(R.id.tv_date);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("News Details");

        linearLayoutNotificationDetails=(LinearLayout)findViewById(R.id.linearLayoutNotificationDetails);
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

   /* @Override
    public void onBackPressed() {
        Intent in=new Intent(getApplicationContext(),MainActivity.class);
        in.putExtra("fragment_id", 3);
        startActivity(in);
    }
*/
    public void NewsDetail()
    {
        String Url=url+id+"/";
      /*  Log.e("new detailes url "," "+Url);
        Log.e("name and token "," "+ name + token);*/
        RestClientVolley restClientVolley=new RestClientVolley(NewsItemDetail.this, Request.Method.GET, Url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                Log.e("JEWELRAP" , "news detail: " + response);
                    if(response!=null)
                    {
                        setData(response);
                    }
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        restClientVolley.executeRequest();
    }

    public void setData(String response)
    {
        try {
            JSONObject jsonObject=new JSONObject(response);
            textView_title.setText(jsonObject.getString("title"));
            textView_description.setText(jsonObject.getString("description"));
            textView_date.setText(jsonObject.getString("date"));

            linearLayoutNotificationDetails.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
