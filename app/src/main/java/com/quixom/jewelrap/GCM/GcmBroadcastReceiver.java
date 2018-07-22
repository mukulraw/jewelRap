package com.quixom.jewelrap.GCM;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
      startWakefulService(context, (intent.setComponent(comp)));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent.setComponent(comp));
//        }
//        else
//        {
//            startWakefulService(context, (intent.setComponent(comp)));
//        }
        setResultCode(Activity.RESULT_OK);
    }
}