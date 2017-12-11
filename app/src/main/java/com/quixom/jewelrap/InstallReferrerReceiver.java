package com.quixom.jewelrap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by TBX on 12/11/2017.
 */

public class InstallReferrerReceiver extends BroadcastReceiver {


    SharedPreferences pref;
    SharedPreferences.Editor edit;


    @Override
    public void onReceive(Context context, Intent intent) {

        pref = context.getSharedPreferences("MyPrefs" , Context.MODE_PRIVATE);

        edit = pref.edit();

        String referrer = intent.getStringExtra("referrer");

        edit.putString("refer" , referrer);

        edit.apply();




        //Use the referrer
    }
}