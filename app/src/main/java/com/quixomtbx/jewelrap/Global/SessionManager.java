package com.quixomtbx.jewelrap.Global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.quixomtbx.jewelrap.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String Name = "nameKey";
    public static final String TOKEN = "tokenKey";
    public static final String Id = "id";
    public static final String USERROLE = "userrole";
    public static final String UNIQUEID ="uniqueid";
    public static final String GCMID ="gcmid";
    public static final String IS_VERIFIED="verified";
    public static final String FIRM_NAME="firm_name";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String HAS_BROKER="has_broker";
    public static final String NEWS_COUNT="newsCount";
    public static final String PROFILE_IMAGE="profile_image";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String name,String tokenKey,String id,String userrole,String uniqueid,String verified,String firmname ,String firstname,String lastname){
       editor.putBoolean(IS_LOGIN, true);
       editor.putString(Name, name);
       editor.putString(TOKEN, tokenKey);
       editor.putString(Id,id);
        editor.putString(USERROLE,userrole);
        editor.putString(UNIQUEID,uniqueid);
        editor.putString(IS_VERIFIED,verified);
        editor.putString(FIRM_NAME,firmname);
        /*editor.putString(FIRST_NAME,firstname);
        editor.putString(LAST_NAME,lastname);*/
       editor.commit();

    }

    public void saveHasBrokerValue(boolean val)
    {
        editor.putBoolean(HAS_BROKER,val);
        editor.commit();
    }
    public void newsCount(String count){
        editor.putString(NEWS_COUNT,count);
        editor.commit();
    }
    public void username(String firstname,String lastname){
        editor.putString(FIRST_NAME,firstname);
        editor.putString(LAST_NAME,lastname);
        editor.commit();
    }

    public void getProfileImage(String profileImage){
        editor.putString(PROFILE_IMAGE,profileImage);
        editor.commit();
    }
    public void gcmId(String gcmID){
        editor.putString(GCMID,gcmID);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Name, pref.getString(Name, null));
        user.put(Id, pref.getString(Id, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }


    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


}
