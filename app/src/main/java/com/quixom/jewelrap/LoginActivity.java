package com.quixom.jewelrap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.quixom.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    EditText et_username, et_password;
    Button btnLogin, btnCreateNewAccnt;
    String url = Global_variable.login_api;
    String userid, pwd, msg, str;
    String TAG = "JEWELRAP";
    SessionManager session;
    GoogleCloudMessaging gcm;
    ImageButton imageButton;
    boolean pwdvisible = false;
    String regId, deviceId = "";

    ProgressBar progress;
    TextView forgot_psw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);

        progress = (ProgressBar)findViewById(R.id.progress);

        et_username = (EditText) findViewById(R.id.input_username);
        et_password = (EditText) findViewById(R.id.input_password);
        imageButton = (ImageButton) findViewById(R.id.showpwdimg);
        btnCreateNewAccnt = (Button) findViewById(R.id.btn_sign_up);
        btnCreateNewAccnt.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.btn_login);
        forgot_psw = (TextView) findViewById(R.id.tvForgotPsw);
        forgot_psw.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        session = new SessionManager(getApplicationContext());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_password.getText().toString().isEmpty()) {
                    if (pwdvisible == false) {
                        et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        et_password.setTypeface(Typeface.DEFAULT);
                        et_password.setSelection(et_password.getText().length());
                        pwdvisible = true;
                    } else {
                        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        et_password.setTypeface(Typeface.DEFAULT);
                        et_password.setSelection(et_password.getText().length());
                        pwdvisible = false;
                    }
                }
            }
        });

        colorFilter(getApplicationContext(), R.drawable.ic_account_black_24dp);
        colorFilter(getApplicationContext(), R.drawable.ic_key_black_24dp);
        colorFilter(getApplicationContext(), R.drawable.ic_eye_black_24dp);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvForgotPsw: {
                forgot_psw.setTextColor(getResources().getColor(R.color.colorPrimary));
                forgot_psw.setTypeface(null, Typeface.BOLD);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forgot_psw.setTextColor(getResources().getColor(R.color.black));
                        forgot_psw.setTypeface(null, Typeface.NORMAL);
                    }
                }, 1000);
                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jewelrap.com/user/password/reset/"));
                Intent chooser = Intent.createChooser(webintent, "Open with");
                startActivity(chooser);
                break;

            }
            case R.id.btn_login: {
                userid = et_username.getText().toString();
                pwd = et_password.getText().toString();

                checkEmptyStringValidation(et_username);
                checkEmptyStringValidation(et_password);

                if (et_password.getError() == null && et_username.getError() == null) {
                    /*if (TextUtils.isEmpty(deviceId)) {
                        regId = registerGCM();
                    } else {*/
                        loginUser();
                    //}

                }
                break;
            }
            case R.id.btn_sign_up: {
                Intent in = new Intent(getApplicationContext(), Registration.class);
                startActivity(in);
                overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                break;
            }
        }


    }





    private void populateData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (Global_variable.logEnabled) {
                Log.e("login responce", jsonObject.toString());
            }
            str = jsonObject.getString("status");
            msg = jsonObject.optString("message");
            if (str.equals("ok")) {
                String name = jsonObject.optString("username");
                String token = jsonObject.optString("key");
                Log.e("JEWELRAP", " token ::" + token);
                String id = jsonObject.optString("Id");
                String userrole = jsonObject.optString("userrole");
                String uniqueid = jsonObject.optString("unqiue_Id");
                String is_verified = jsonObject.optString("is_verified");
                String firm_name = jsonObject.optString("firm_name");
                String firstname = jsonObject.optString("first_name");
                String lastname = jsonObject.optString("last_name");
                session.createLoginSession(name, token, id, userrole, uniqueid, is_verified, firm_name, firstname, lastname);

                if (is_verified.equals("False")) {
                    regId = "";
                    android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.DesignDemo);
                    dlgAlert.setMessage(msg);
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                } else {
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                    NavigationDrawerAdapter.selected_item = 0;
                    MainActivity.id_pos = 0;
                }
            } else {
                regId = "";
                android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.DesignDemo);
                dlgAlert.setMessage(msg);
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);

        new GmsGetId().execute();

        return regId;
    }


    void checkEmptyStringValidation(EditText et) {
        String str = et.getText().toString();
        if (str.length() == 0) {
            et.setError("Required");
        } else {
            et.setError(null);

        }
    }

    public boolean isValidEmail(String eml) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]{2,3}";
        if (eml.length() == 0) {
            return false;
        } else {
            if (eml.matches(emailPattern)) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(mainActivity);
    }

    private void loginUser() {


        progress.setVisibility(View.VISIBLE);


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global_variable.base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<loginBean> call = cr.login(userid , pwd);

        call.enqueue(new Callback<loginBean>() {
            @Override
            public void onResponse(Call<loginBean> call, Response<loginBean> response) {



                str = response.body().getStatus();
                //msg  = ;
                if (str.equals("ok")) {
                    String name = response.body().getUsername();
                    String token = response.body().getKey();
                    Log.e("JEWELRAP", " token ::" + token);
                    String id = String.valueOf(response.body().getId());
                    String userrole = response.body().getUserrole();
                    String uniqueid = response.body().getUnqiueId();
                    String is_verified = String.valueOf(response.body().getIsVerified());
                    String firm_name = response.body().getFirmName();
                    String firstname = response.body().getFirstName();
                    String lastname = response.body().getLastName();
                    session.createLoginSession(name, token, id, userrole, uniqueid, is_verified, firm_name, firstname, lastname);

                    if (is_verified.equals("false")) {
                        regId = "";
                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.DesignDemo);
                        dlgAlert.setMessage(msg);
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();

                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                    } else {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                        NavigationDrawerAdapter.selected_item = 0;
                        MainActivity.id_pos = 0;
                    }
                } else {
                    regId = "";
                    android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.DesignDemo);
                    dlgAlert.setMessage(msg);
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<loginBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });






        /*RestClientVolley restClientVolley = new RestClientVolley(this, Request.Method.POST, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                populateData(response);
            }
        });
        restClientVolley.addParameter("email", userid);
        restClientVolley.addParameter("password", pwd);
        restClientVolley.addParameter("android_device_id", deviceId);
        if (Global_variable.logEnabled) {
            Log.e("**login device id", deviceId);
            Log.e("**login url", url);
        }
        restClientVolley.executeRequest();*/
    }

    class GmsGetId extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                regId = instanceID.getToken(Global_variable.GOOGLE_PROJECT_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                //regId = gcm.register(Global_variable.GOOGLE_PROJECT_ID);

                deviceId = regId;
                if (Global_variable.logEnabled) {
                    Log.e(TAG, "device id is : " + deviceId);
                }
            } catch (IOException ex) {
                deviceId = "";
                // deviceId = "Error :" + ex.getMessage();
                if (Global_variable.logEnabled) {
                    Log.e(TAG, "device id is : " + "Error :" + ex.getMessage());
                }
                ex.printStackTrace();
            }
            session.gcmId(deviceId);
            return deviceId;
        }

        @Override
        protected void onPostExecute(String msg) {
            loginUser();
        }

    }

    public void colorFilter(Context context, int resource) {
        int iColor = Color.parseColor("#747474");
        int red = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue = iColor & 0xFF;

        float[] matrix = {0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0};

        Drawable mDrawable = ContextCompat.getDrawable(context, resource);
        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        mDrawable.setColorFilter(colorFilter);

    }


}
