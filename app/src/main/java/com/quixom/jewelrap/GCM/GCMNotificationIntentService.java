package com.quixom.jewelrap.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixom.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixom.jewelrap.Badge.Badges;
import com.quixom.jewelrap.Badge.BadgesNotSupportedException;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.InquiryDetail;
import com.quixom.jewelrap.InquiryFragment;
import com.quixom.jewelrap.MainActivity;
import com.quixom.jewelrap.Model.NotificationModel;
import com.quixom.jewelrap.NewsItemDetail;
import com.quixom.jewelrap.R;

import java.util.ArrayList;
import java.util.Iterator;


public class GCMNotificationIntentService extends IntentService {

    public static  int NOTIFICATION_ID = 1;
    public static final String TAG = "GCMNotificationIntentService";
    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    static ArrayList<String> msgs=new ArrayList<>();
    static ArrayList<NotificationModel> messeges=new ArrayList<>();
    NotificationModel model;
    Intent intent;
    static int news=0,inquiries=0;
    public static int newsCount;
  /*  SharedPreferences sharedPreferences;
    String name,uniqueid;
    FirebaseAnalytics firebaseAnalytics;*/
    public GCMNotificationIntentService() {
        super("GcmIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("" + extras.toString(), "1","","");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("" + extras.toString(), "1","","");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                sendNotification("" + extras.get(Global_variable.MESSAGE_KEY), "" + extras.get(Global_variable.INQUIRY_ID),""+extras.get(Global_variable.BADGE),""+extras.get(Global_variable.NEWS));
                if(Global_variable.logEnabled) {
                    Log.e(TAG, "inquiry id : " + extras.get(Global_variable.INQUIRY_ID) + "news" + extras.get(Global_variable.NEWS) + "count" + extras.toString());
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, String inquiry_id,String badge,String newsID) {
       /* sharedPreferences=getSharedPreferences(SessionManager.MyPREFERENCES,MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        uniqueid=sharedPreferences.getString(SessionManager.UNIQUEID,"");
        Log.e("JEWELRAP"," name "+ name + uniqueid);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);*/
        news=0;
        inquiries=0;
        msgs.add(msg);
        if(Global_variable.logEnabled) {
            Log.e(TAG + "NotificationMsg ", msgs.toString());
            Log.e(TAG + "news ", newsID);
            Log.e(TAG + "badge ", badge);
        }
        int number = 0;
        if(!badge.equals("null")) {
            number = Integer.parseInt(badge);
        }
        NotificationCompat.Style style;
        if(!newsID.equals("null")){
            model=new NotificationModel();
            model.setId(newsID);
            model.setMessage(msg);
            model.setType("news");
            messeges.add(model);
            if(Global_variable.logEnabled) {
                Log.e(TAG + "news ", "called");
            }
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            intent = new Intent(this, NewsItemDetail.class);
            intent.putExtra("news_id", newsID);
            news++;
        }
        if(!inquiry_id.equals("null")) {
            inquiries++;
            model=new NotificationModel();
            model.setId(inquiry_id);
            model.setMessage(msg);
            model.setType("inquiry");
            messeges.add(model);
            if(Global_variable.logEnabled) {
                Log.e(TAG + "inquiry ", "called");
            }
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            intent = new Intent(this, InquiryDetail.class);
            intent.putExtra("inq_id", inquiry_id);
            intent.putExtra("comeinquiry", "currentDemand");
            intent.putExtra("enquiry","analytics");

        }
        if(Global_variable.logEnabled) {
            Log.e(">> messages size", "" + messeges.size());
        }

        if(messeges.size()>1)
        {
            news=0;
            inquiries=0;
            for(int i=0;i<messeges.size();i++)
            {
                NotificationModel model=messeges.get(i);
                String type=model.getType();
                if(type.equals("news")){
                    news++;
                }
                else {
                    inquiries++;
                }

            }
            if(Global_variable.logEnabled) {
                Log.e("messeges size :: send notification", messeges.size() + "");
                Log.e("news :: send notification", news + "");
                Log.e("inquiries :: send notification", inquiries + "");
            }


            if(news==messeges.size())
            {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment_id", 3);
                NavigationDrawerAdapter.selected_item=3;
            }
            else if(inquiries==messeges.size())
            {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment_id", 1);
                NavigationDrawerAdapter.selected_item=1;
            }
            else
            {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment_id", 0);
                NavigationDrawerAdapter.selected_item=0;
            }

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setSummaryText("You have "+messeges.size()+" unread notifications.");
            style = inboxStyle;
            for (NotificationModel r : messeges) {
                inboxStyle.addLine(r.getMessage());
                if(Global_variable.logEnabled) {
                    Log.e(TAG + " String r :", r.getMessage());
                }
            }



        }
        else
        {
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            style = bigTextStyle.bigText(msg);
        }
        newsCount=news;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction("com.quixom.jewelrap");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("JewelRap")
                .setAutoCancel(true)
                .setStyle(style)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(msg);
        InquiryFragment.Comefrom="CURRENT";

        mBuilder.setContentIntent(contentIntent);
        updateBadge(number);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private void updateBadge(int count) {
        try {
            Badges.setBadge(this, count);
        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            if(Global_variable.logEnabled) {
                Log.d(TAG, badgesNotSupportedException.getMessage());
            }
        }
    }
    public static void CancelNotification(Context ctx, int notifyId,String type) {
        if(Global_variable.logEnabled) {
            Log.e("messeges size :: cancel notification before", messeges.size() + "");
        }
        if(type.equals("news"))
        {
            newsCount=0;
        }
        if(messeges.size()>0)
        {
            Iterator<NotificationModel> iter = messeges.iterator();
            while (iter.hasNext()) {
                NotificationModel model1 = iter.next();
                if (model1.getType().equals(type))
                    iter.remove();
            }

        }
        if(Global_variable.logEnabled) {
            Log.e("type :: cancel notification", type);
            Log.e("messeges size after remove :: cancel notification", messeges.size() + "");
        }
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx
                .getSystemService(ns);
        nMgr.cancel(notifyId);
        msgs.clear();
    }

}