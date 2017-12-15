package com.quixomtbx.jewelrap.Utils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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

import com.quixomtbx.jewelrap.Global.Global_variable;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestClientMultipartRequest {

    private static final String ATTACHMENTS = "profile_pic";
    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    Response.Listener<String> mListener;
    File mFilePart;
    /* private final Map<String, String> mStringPart;*/
    ContentValues headerValues, paramsValues, bodyVlaues;
    Map<String, String> header, params, mStringPart;
    String TAG = "JEWELRAP";
    VolleyCallBack callBack;
    ProgressDialog progressDialog;
    boolean visiblity;
    int statuscode;
    private int method;
    private String URL, jsonResponse;
    private Context mContext;
    ArrayList<File> fileArrayList;

    public RestClientMultipartRequest(Context context, int method, String url, File file, VolleyCallBack callBack) {

        this.mContext = context;
        this.method = method;
        this.URL = url;
        this.callBack = callBack;
        headerValues = new ContentValues();
        paramsValues = new ContentValues();
        bodyVlaues = new ContentValues();
        header = new HashMap<>();
        params = new HashMap<>();
        mStringPart = new HashMap<>();
        this.mFilePart = file;
        this.fileArrayList=fileArrayList;
        progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

    }

    public void addStringBody(String key, String value) {
        bodyVlaues.put(key, value);
    }

    public void addHeader(String key, String value) {
        headerValues.put(key, value);
    }

    public void addParameter(String key, String value) {
        //Log.d(GlobalVariable.TAG, key + value);
        paramsValues.put(key, value);
    }

    public void progressDialogVisibility(boolean value) {
        this.visiblity = value;
    }


    private void buildMultipartEntity() {
       // Log.d(GlobalVariable.TAG, "buildMultipartEntity");
        /*for(int i=0;i < fileArrayList.size();i++) {
            mFilePart=fileArrayList.get(i);
            entity.addPart(ATTACHMENTS, new FileBody(mFilePart));
        }*/

        entity.addPart(ATTACHMENTS, new FileBody(mFilePart));
        try {
            for (Map.Entry<String, Object> params : paramsValues.valueSet()) {
                String key = params.getKey();
                String value = params.getValue().toString();
                entity.addPart(key, new StringBody(value));
                Log.e("value", "value: " + value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public void executeRequest() {
        buildMultipartEntity();
       // entity.setBoundary("----WebKitFormBoundary"+generateBoundary());
        httpentity = entity.build();
        if (!visiblity) {
            if ((progressDialog != null)) {
                progressDialog.show();
            }
        }

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final StringRequest request = new StringRequest(method, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.d(GlobalVariable.TAG, "response: " + response);
                        callBack.processCompleted(response);
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(NoConnectionError error) {
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d(GlobalVariable.TAG, "error" + error.getMessage());
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                String obj = new JSONObject(res).getString("detail");
                                Toast.makeText(mContext, obj, Toast.LENGTH_LONG).show();
                                //Log.d(GlobalVariable.TAG, "obj: " + obj);
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
                    //Log.d(GlobalVariable.TAG, "getHeaders  " + key + value);
                  //  header.put("Content-Type","application/x-www-form-urlencoded");
                    header.put(key, value);
                }
                return header;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //Log.d(GlobalVariable.TAG, "getBody");
                ByteArrayOutputStream bos = new ByteArrayOutputStream(Global_variable.maxread_size);


                try {

                    httpentity.writeTo(bos);
                   // Log.e("BAOS size",""+bos.size());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bos.toByteArray();
            }

            @Override
            public String getBodyContentType() {
                Log.e("BodyContentType", "getBodyContentType " + httpentity.getContentType().getValue());
                return httpentity.getContentType().getValue();
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statuscode = response.statusCode;
               // Log.d("Response code volley", "" + statuscode);
                return super.parseNetworkResponse(response);
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}
