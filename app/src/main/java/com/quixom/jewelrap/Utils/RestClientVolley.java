package com.quixom.jewelrap.Utils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RestClientVolley {

    ContentValues headerValues, paramsValues;
    Map<String, String> header, params;
    String TAG = "jlajsdajsdkjlasd;k";
    VolleyCallBack callBack;
    ProgressDialog progressDialog;
    boolean visiblity;
    SharedPreferences sharedPreferences;
    String gcmID;
    private int method;
    private String URL, jsonResponse;
    private Context mContext;

    public RestClientVolley(Context context, int method, String url, VolleyCallBack callBack) {
        Log.d(TAG, "context : " + context);
        sharedPreferences = context.getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        gcmID = sharedPreferences.getString(SessionManager.GCMID, "");

        String myUrl = "";
        if (url.equals(Global_variable.state_api) || url.contains(Global_variable.city_api)) {
            this.URL = url;
        } else {
            if (url.endsWith("/")) {
                myUrl = "?GCM_Token=" + gcmID;
            } else {
                myUrl = "&GCM_Token=" + gcmID;
            }
            this.URL = url + myUrl;
        }
        this.mContext = context;
        this.method = method;

        if (Global_variable.logEnabled) {
            Log.e("*** rest client url ", URL);
        }
        this.callBack = callBack;
        headerValues = new ContentValues();
        paramsValues = new ContentValues();
        header = new HashMap<>();
        params = new HashMap<>();
        progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }


    public void addHeader(String key, String value) {
        headerValues.put(key, value);
    }

    public void addParameter(String key, String value) {
        paramsValues.put(key, value);
    }

    public void progressDialogVisibility(boolean value) {
        this.visiblity = value;
    }

    public void onPause() {
        //  super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

    public void executeRequest() {

        if (!visiblity) {
            if ((progressDialog != null)) {
                progressDialog.show();
            }
        }

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        Log.d("asdasdURL", URL);

        StringRequest request = new StringRequest(method, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callBack.processCompleted(response);

                        Log.d("Asdasd", response);

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();


                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.d("error" , res);
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }


                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Log.d(TAG, "error : " + error.toString());
                        String errormsg = "";


                        if (error instanceof NoConnectionError) {
                            errormsg = "No internet Access, Check your internet connection.";
                            Toast.makeText(mContext, errormsg, Toast.LENGTH_LONG).show();
                        }
                    }

                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                for (Map.Entry<String, Object> entry : headerValues.valueSet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    header.put(key, value);
                }
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                for (Map.Entry<String, Object> entry : paramsValues.valueSet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    params.put(key, value);
                }
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


}
