package com.quixom.jewelrap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quixom.jewelrap.Adapter.NavigationDrawerAdapter;

public class ProfileView extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbartitle;
    ImageView imageView;
    String username,profileview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        initView();

    }
    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        imageView=(ImageView)findViewById(R.id.iv_user_profile);
        Intent in=getIntent();
        username=in.getStringExtra("username");
        profileview=in.getStringExtra("profileview");
        toolbartitle.setText(username);
        Log.e("JEWELRAP ", " url : " +profileview);
        Glide.with(getApplicationContext())
                .load(profileview)
                .into(imageView);
      //  imageView.setImageURI(Uri.parse(profileview));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
    }
}

