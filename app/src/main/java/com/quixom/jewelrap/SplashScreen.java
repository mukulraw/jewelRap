package com.quixom.jewelrap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.quixom.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixom.jewelrap.GCM.GCMNotificationIntentService;
import com.quixom.jewelrap.Global.SessionManager;


public class SplashScreen extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String TOKEN = "tokenKey";
    public static final String Id = "id";
    SessionManager session;
    SharedPreferences pref;
    String nm, eml, tkn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(MyPREFERENCES, 0);
        nm = pref.getString(Name, null);
        eml = pref.getString(Id, null);
        tkn = pref.getString(TOKEN, null);
        //GCMNotificationIntentService.CancelNotification(SplashScreen.this,GCMNotificationIntentService.NOTIFICATION_ID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (nm != null && tkn != null) {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(in);
                        NavigationDrawerAdapter.selected_item=0;
                        MainActivity.id_pos=0;
                        overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                    } else {
                        Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                    }
                }

            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
