package com.quixom.jewelrap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixom.jewelrap.Badge.Badges;
import com.quixom.jewelrap.Badge.BadgesNotSupportedException;
import com.quixom.jewelrap.GCM.GCMNotificationIntentService;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.quixom.jewelrap.R.id.imageviewfillFavorite;

public class InquiryDetail extends AppCompatActivity implements View.OnClickListener {
    private static String name = "", token = "", id = "", uniqueid = "";
    String TAG = "JEWELRAP";
    TextView tvShape, tvSize, tvDesc, tvPrice, tvUserAdress, tvPurity, tvColor, tvCat, tvUserName, tvUserFirmname, tvCertification, tvWeight, tvQuantity;
    String shape = "", size = "", desc, price, useradd, usereml, usercontactno, userfirmname, purity = "", color = "", cat = "", uname, city, certi = "", weight = "", quantity =
            "";
    int SELECTED_CATEGORY;
    String inquiry_id, url = "", profileview, username, userPersonal, userLandline, userEmail;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    CardView cardContactInfo, cardEnquiryDetails;
    ImageView IVcategory;
    LinearLayout sizeLinearLayout, colorLinearLayout, certiLinearLayout, purityLinearLayout, quantityLinearlayout, weightLinearlayout, layoutPhone,
            layoutLandline, layoutMessage, layoutMail;
    TextView toolbartitle;
    private Toolbar toolbar;
    ImageView imageviewFavorite, imageViewfillFavorite;
    String favoriteUrl = Global_variable.inquiry_api;
    String farovite;
    String comeinquiry;
    String faroviteFlag = "false";
    FirebaseAnalytics firebaseAnalytics;
    CircularImageView circularImageView;
    String analyticsEnquiry = "";


    Context context;
    Inquiries inquiries;
    InquiryDetail inquiryDetail;
    static FavoriteCallback favoriteCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_inquiry_detail);


        Intent in = getIntent();
        inquiry_id = in.getStringExtra("inq_id");
        comeinquiry = in.getStringExtra("comeinquiry");
        analyticsEnquiry = in.getStringExtra("enquiry");
        cardContactInfo = (CardView) findViewById(R.id.cardViewcontact);
        cardEnquiryDetails = (CardView) findViewById(R.id.cardViewEnquiry);
        tvCat = (TextView) findViewById(R.id.tv_cat_value_ordDetail);


        tvShape = (TextView) findViewById(R.id.tv_shape_value_ordDetail);
        tvSize = (TextView) findViewById(R.id.tv_size_value_ordDetail);
        tvDesc = (TextView) findViewById(R.id.tv_desc_value_ordDetail);
        tvPrice = (TextView) findViewById(R.id.tvprice_ordDetail);
        tvUserAdress = (TextView) findViewById(R.id.tv_userAddress_ordDetail);
        tvPurity = (TextView) findViewById(R.id.tv_purity_value_ordDetail);
        tvColor = (TextView) findViewById(R.id.tv_color_value_ordDetail);
        tvUserName = (TextView) findViewById(R.id.tv_userName_ordDetail);
        tvUserFirmname = (TextView) findViewById(R.id.tv_user_FirmName);
        tvCertification = (TextView) findViewById(R.id.tv_certification_value_ordDetail);
        tvWeight = (TextView) findViewById(R.id.tv_weight_value_ordDetail);
        tvQuantity = (TextView) findViewById(R.id.tv_quantity_value_ordDetail);


        layoutPhone = (LinearLayout) findViewById(R.id.ll_inquiry_detail_personal);
        layoutLandline = (LinearLayout) findViewById(R.id.ll_inquiry_detail_landline);
        layoutMail = (LinearLayout) findViewById(R.id.ll_inquiry_detail_mail);
        layoutMessage = (LinearLayout) findViewById(R.id.ll_inquiry_detail_message);
        layoutPhone.setOnClickListener(this);
        layoutLandline.setOnClickListener(this);
        layoutMail.setOnClickListener(this);
        layoutMessage.setOnClickListener(this);

        int iColor = Color.parseColor("#747474");
        int red = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue = iColor & 0xFF;

        float[] matrix = {0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0};

        Drawable mDrawable = ContextCompat.getDrawable(this, R.drawable.ic_landline);
        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        mDrawable.setColorFilter(colorFilter);


        IVcategory = (ImageView) findViewById(R.id.iv_inquiry_detail_category);
        imageviewFavorite = (ImageView) findViewById(R.id.imageviewFavorite);
        imageViewfillFavorite = (ImageView) findViewById(imageviewfillFavorite);
        imageviewFavorite.setOnClickListener(this);
        imageViewfillFavorite.setOnClickListener(this);


        sizeLinearLayout = (LinearLayout) findViewById(R.id.sizeLayout);
        colorLinearLayout = (LinearLayout) findViewById(R.id.colorLayout);
        certiLinearLayout = (LinearLayout) findViewById(R.id.certiLayout);
        purityLinearLayout = (LinearLayout) findViewById(R.id.purityLayout);
        weightLinearlayout = (LinearLayout) findViewById(R.id.weightLayout);
        quantityLinearlayout = (LinearLayout) findViewById(R.id.quantityLayout);
        sessionManager = new SessionManager(InquiryDetail.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Enquiry Details");
        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        circularImageView = (CircularImageView) findViewById(R.id.enquiry_profile_image);
        circularImageView.setOnClickListener(this);
        if (InquiryFragment.Comefrom.equals("MYIN")) {
            url = Global_variable.myinquiry_details_api + inquiry_id + "/";
        } else if (InquiryFragment.Comefrom.equals("IN")) {
            url = Global_variable.inquiry_details_api + inquiry_id + "/";
        } else {
            url = Global_variable.inquiry_details_api + inquiry_id + "/?current_demand=yes";
        }

        getEnquiryDetail();
        badgeRemoveCall();
        if (Global_variable.logEnabled) {
            Log.e("** inquiry details api", url);
        }
        if (analyticsEnquiry != null) {
            Log.e("JEWELRAP", "notification");
            //firebase analytics for notification
            Bundle bundle = new Bundle();
            bundle.putString("jewelrap_id", uniqueid);
            bundle.putString("user_name", name);
            bundle.putString("inq_id", inquiry_id);
            firebaseAnalytics.logEvent("enquiry_clickon_notification", bundle);
        } else {
            Log.e("JEWELRAP", " not notification");
        }

        inquiries = new Inquiries();

        Log.e("JEWELRAP", "on create :: context value :" + context + "get context ::" + getApplicationContext());
        if(in.hasExtra("comeinquiry") && !comeinquiry.equals("currentDemand")) {
            favoriteCallback.getfavorite(faroviteFlag);
        }

    }

    private void getEnquiryDetail() {
        Log.d(TAG, "url : " + url);
        RestClientVolley restClientVolley = new RestClientVolley(this, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if (response != null) {
                    enquiryDetail(response);
                }
            }
        });

        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        restClientVolley.executeRequest();
    }

    private void enquiryDetail(String response) {
       /* Bundle bundle = new Bundle();
        bundle.putString("user id", id);
        bundle.putString("jewelrap unique id",uniqueid );
        bundle.putString("username",name);
        firebaseAnalytics.logEvent("Demands opened and Viewed", bundle);*/

        if (Global_variable.logEnabled) {
            Log.e("** response details", response);
        }
        Log.e("** response details", response);
        JSONObject jobj = null;
        try {
            jobj = new JSONObject(response);
            if (jobj.has("multiple_login")) {
                if (Global_variable.logEnabled) {
                    Log.e("Message : ", jobj.getString("messages"));
                }
                String msg = jobj.optString("messages");

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(InquiryDetail.this, R.style.DesignDemo);
                dlgAlert.setMessage(msg);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.logoutUser();
                                overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();

            } else {
                cat = jobj.optJSONObject("category").optString("category_name");

                SELECTED_CATEGORY = Integer.parseInt(jobj.optJSONObject("category").optString("id"));
                price = jobj.optString("price");
                desc = jobj.optString("extra_demand").replace("\n", "");
                farovite = jobj.optString("is_favorite");
                weight = jobj.optJSONObject("content_object").optString("weight").replace("\n", "");
                quantity = jobj.optJSONObject("content_object").optString("qty").replace("\n", "");

                // Analytics for view enquiry..........
                Bundle bundle = new Bundle();
                bundle.putString("jewelrap_id", uniqueid);
                bundle.putString("enquiry_id", inquiry_id);
                bundle.putString("user_name", name);
                bundle.putString("category", cat);
                bundle.putString("contact_person_name", jobj.optJSONObject("user").optString("first_name") + " " + jobj.optJSONObject("user").optString("last_name"));
                bundle.putString("firm_name", jobj.optJSONObject("user").optJSONObject("user_profile").optString("firm_name"));
                firebaseAnalytics.logEvent("enquiry_detail", bundle);

                if (Global_variable.logEnabled) {
                    Log.e("** favorite", farovite);
                }
                JSONArray shapearr = jobj.optJSONArray("selected_shape");
                for (int i = 0; i < shapearr.length(); i++) {
                    shape = shape + shapearr.optJSONObject(i).optString("shape_name");
                    if (i != shapearr.length() - 1) {
                        shape = shape + ", ";
                    }
                }
                if (cat.equals("Solitaire")) {
                    fetchDetails(jobj, "selected_certification", "certified_name", "certification");
                    fetchDetails(jobj, "selected_color", "color_name", "color");
                    fetchDetails(jobj, "selected_purity", "purity_name", "purity");
                    String from_size = jobj.optJSONObject("content_object").optJSONObject("size")
                            .optString("from_size");

                    String to_size = jobj.optJSONObject("content_object").optJSONObject("size")
                            .optString("to_size");
                    if (!from_size.isEmpty() && !to_size.isEmpty()) {
                        size = from_size + " to " + to_size;
                        boolean isPair = jobj.optJSONObject("content_object").getBoolean("is_pair");
                        if (Global_variable.logEnabled) {
                            Log.e("is_pair ", "" + isPair);
                        }
                        if (isPair) {
                            size = size + " ( Pair )";
                        }

                    } else {
                        size = "";
                    }

                } else if (cat.equals("Silver") || cat.equals("Color Stone")) {

                    String from_size = jobj.optJSONObject("content_object").optJSONObject("size")
                            .optString("from_size");

                    String to_size = jobj.optJSONObject("content_object").optJSONObject("size")
                            .optString("to_size");
                    if (!from_size.isEmpty() && !to_size.isEmpty()) {
                        size = from_size + " to " + to_size;
                       /* boolean isPair=jobj.optJSONObject("content_object").getBoolean("is_pair");
                        if(Global_variable.logEnabled) {
                            Log.e("is_pair ", "" + isPair);
                        }*/
                       /* if(isPair)
                        {
                            size=size+" ( Pair )";
                        }*/

                    } else {
                        size = "";
                    }

                } else {
                    JSONArray sizearr = jobj.optJSONObject("content_object").optJSONArray("selected_size");
                    if (sizearr.length() == 0) {
                        size = "";
                    } else {
                        for (int i = 0; i < sizearr.length(); i++) {
                            size = size + sizearr.optJSONObject(i).optString("size_name");
                            if (i != sizearr.length() - 1) {
                                size = size + ",";
                            }
                        }
                    }
                }
                username = jobj.optJSONObject("user").optString("first_name") + " " + jobj.optJSONObject("user").optString("last_name");
                uname = jobj.optJSONObject("user").optString("first_name") + " " + jobj.optJSONObject("user").optString("last_name") + " (" + jobj.optJSONObject("user")
                        .optJSONObject("user_profile").getString("unique_id") + ")";
                usereml = jobj.optJSONObject("user").optString("email");
                useradd = jobj.optJSONObject("user").optJSONObject("user_profile").optString("address");
                userfirmname = jobj.optJSONObject("user").optJSONObject("user_profile").optString("firm_name");
                city = jobj.optJSONObject("user").optJSONObject("user_profile").getString("city");
                usercontactno = jobj.optJSONObject("user").optJSONObject("user_profile").optString("phone_num");
                userPersonal = jobj.optJSONObject("user").optJSONObject("user_profile").optString("phone_num");
                userLandline = jobj.getJSONObject("user").getJSONObject("user_profile").optString("landline");
                userEmail = jobj.getJSONObject("user").getString("email");
                profileview = Global_variable.base_url + jobj.optJSONObject("user").optJSONObject("user_profile").getString("profile_image");
                Log.e("** url ", " " + profileview);
                Glide.with(getApplicationContext())
                        .load(profileview)
                        .asBitmap()
                        .error(R.drawable.ic_launcher)
                        .placeholder(R.drawable.ic_launcher)
                        .into(circularImageView);
                Glide.with(getApplicationContext())
                        .load(Global_variable.base_url + jobj.optJSONObject("category").optString("Inquiry_category_image"))
//                        .override(100,100)
                        .error(R.drawable.ic_placeholder)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(IVcategory);
                switch (SELECTED_CATEGORY) {
                    case 1:
//                        IVcategory.setImageDrawable(InquiryDetail.this.getResources().getDrawable(R.drawable.ic_solitaire));
                        sizeLinearLayout.setVisibility(View.VISIBLE);
                        colorLinearLayout.setVisibility(View.VISIBLE);
                        certiLinearLayout.setVisibility(View.VISIBLE);
                        purityLinearLayout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
//                        IVcategory.setImageDrawable(InquiryDetail.this.getResources().getDrawable(R.drawable.ic_loose));
                        sizeLinearLayout.setVisibility(View.VISIBLE);

                        break;
                    case 3:
//                        IVcategory.setImageDrawable(InquiryDetail.this.getResources().getDrawable(R.drawable.ic_dj));
                        break;
                    case 4:
//                        IVcategory.setImageDrawable(InquiryDetail.this.getResources().getDrawable(R.drawable.ic_gj));
                        break;
                    case 5:
//                        IVcategory.setImageDrawable(InquiryDetail.this.getResources().getDrawable(R.drawable.ic_gj));
                        sizeLinearLayout.setVisibility(View.VISIBLE);
                        weightLinearlayout.setVisibility(View.VISIBLE);
                        quantityLinearlayout.setVisibility(View.VISIBLE);
                        break;
                    case 6:
//                        IVcategory.setImageDrawable(InquiryDetail.this.getResources().getDrawable(R.drawable.ic_gj));
                        sizeLinearLayout.setVisibility(View.VISIBLE);
                        quantityLinearlayout.setVisibility(View.VISIBLE);
                        break;
                }
                setText(tvColor, color);
                setText(tvPurity, purity);
                setText(tvCertification, certi);
                setText(tvSize, size);
                setText(tvPrice, price.trim());
                setText(tvShape, shape);
                setText(tvDesc, desc.trim());
                setText(tvQuantity, quantity.trim());
                setText(tvWeight, weight.trim());
                tvCat.setText(cat);
                tvUserName.setText(uname);
                tvUserAdress.setText(useradd + ", " + city);
                tvUserFirmname.setText(userfirmname);
                if (farovite.equals("true")) {
                    imageViewfillFavorite.setVisibility(View.VISIBLE);
                    imageviewFavorite.setVisibility(View.GONE);
                } else {
                    imageViewfillFavorite.setVisibility(View.GONE);
                    imageviewFavorite.setVisibility(View.VISIBLE);
                }
                if (InquiryFragment.Comefrom == "MYIN") {
                    cardContactInfo.setVisibility(View.GONE);
                    cardEnquiryDetails.setVisibility(View.VISIBLE);
                    imageviewFavorite.setVisibility(View.GONE);
                    imageViewfillFavorite.setVisibility(View.GONE);
                } else {
                    cardContactInfo.setVisibility(View.VISIBLE);
                    cardEnquiryDetails.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void badgeRemoveCall() {
        RestClientVolley restClientVolley = new RestClientVolley(this, Request.Method.POST, Global_variable.badge_api, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                badgeRemoved(response);
            }
        });
        restClientVolley.addParameter("id", id);
        restClientVolley.progressDialogVisibility(true);
        restClientVolley.executeRequest();
    }

    private void badgeRemoved(String response) {
        try {
            Badges.removeBadge(InquiryDetail.this);
            GCMNotificationIntentService.CancelNotification(InquiryDetail.this, GCMNotificationIntentService.NOTIFICATION_ID, "inquiry");
        } catch (BadgesNotSupportedException e) {
            e.printStackTrace();
        }
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

        if (faroviteFlag.equals("true")) {
            if (comeinquiry.equals("currentDemand")) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.putExtra("pos", 0);
                startActivity(in);
                MainActivity.id_pos = 1;
            }
            if (comeinquiry.equals("oldinquiry")) {
               /* Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.putExtra("pos", 1);
                startActivity(in);
                MainActivity.id_pos = 1;*/
                finish();
            }
        }

        overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
        // overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageviewFavorite: {
                addFavorite("fav");

                imageviewFavorite.setVisibility(View.GONE);
                imageViewfillFavorite.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("user_name", name);
                bundle.putString("jewelrap_id", uniqueid);
                bundle.putString("enquiry_id", inquiry_id);
                bundle.putString("selected_category", String.valueOf(SELECTED_CATEGORY));
                bundle.putString("enquiry_person_name", uname);
                bundle.putString("firm_name", userfirmname);
                firebaseAnalytics.logEvent("enquiry_favorite", bundle);
            }
            break;
            case imageviewfillFavorite:
                addFavorite("non-fav");
                imageviewFavorite.setVisibility(View.VISIBLE);
                imageViewfillFavorite.setVisibility(View.GONE);
                break;
            case R.id.ll_inquiry_detail_message:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", userPersonal);
                smsIntent.putExtra("sms_body", "I got your enquiry through JewelRap:- \n ");
                startActivity(smsIntent);

                Bundle bundleMsg = new Bundle();
                bundleMsg.putString("user_name", name);
                bundleMsg.putString("jewelrap_id", uniqueid);
                bundleMsg.putString("enquiry_id", inquiry_id);
                bundleMsg.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundleMsg.putString("phone_number", userPersonal);
                firebaseAnalytics.logEvent("enquiry_message", bundleMsg);
                break;
            case R.id.ll_inquiry_detail_personal: {
                String message = "tel:" + userPersonal;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(message));
                startActivity(intent);

                Bundle bundle = new Bundle();
                bundle.putString("user_name", name);
                bundle.putString("jewelrap_id", uniqueid);
                bundle.putString("enquiry_id", inquiry_id);
                bundle.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundle.putString("phone_number", userPersonal);
                firebaseAnalytics.logEvent("enquiry_call", bundle);
            }
            break;
            case R.id.ll_inquiry_detail_mail:
                String email = userEmail;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "JewelRap");
                String signatureName = sharedPreferences.getString(SessionManager.FIRST_NAME, "") + " " + sharedPreferences.getString(SessionManager.LAST_NAME, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "I got your enquiry through JewelRap. \n \n \n \n \n \n \n \n " + signatureName + "\n JewelRap id: " + uniqueid);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                Bundle bundle = new Bundle();
                bundle.putString("user_name", name);
                bundle.putString("jewelrap_id", uniqueid);
                bundle.putString("enquiry_id", inquiry_id);
                bundle.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundle.putString("contact_person_emailid", userEmail);
                firebaseAnalytics.logEvent("enquiry_mail", bundle);
                break;

            case R.id.ll_inquiry_detail_landline:
                String message = "tel:" + userLandline;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(message));
                startActivity(intent);

                Bundle bundle1 = new Bundle();
                bundle1.putString("user_name", name);
                bundle1.putString("jewelrap_id", uniqueid);
                bundle1.putString("enquiry_id", inquiry_id);
                bundle1.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundle1.putString("phone_number", userPersonal);
                firebaseAnalytics.logEvent("enquiry_call", bundle1);
                break;

            case R.id.enquiry_profile_image:
                /*Intent in=new Intent(getApplicationContext(),ProfileView.class);
                in.putExtra("username",username);
                in.putExtra("profileview",profileview);
                startActivity(in);*/

                Intent in = new Intent(this, ProfileView.class);
                in.putExtra("username", username);
                in.putExtra("profileview", profileview);
                String transitionName = getString(R.string.transition_name);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                circularImageView,   // The view which starts the transition
                                transitionName    // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity(this, in, options.toBundle());
                break;
        }
    }

    private void fetchDetails(JSONObject jsonObject, String selectedArray, String key, String value) {
        JSONArray array = jsonObject.optJSONObject("content_object").optJSONArray(selectedArray);
        String displayValue = "";
        for (int i = 0; i < array.length(); i++) {
            displayValue += array.optJSONObject(i).optString(key);
            if (i != array.length() - 1) {
                displayValue += ",";
            }
        }

        switch (value) {
            case "certification":
                certi = displayValue;
                break;
            case "purity":
                purity = displayValue;
                break;
            case "color":
                color = displayValue;
                break;
        }
    }

    private void setText(TextView textView, String string) {
        if (Global_variable.logEnabled) {
            Log.e("** value ", "" + string);
        }
        if (string != null && !string.isEmpty() && !string.equals("null")) {
            textView.setText(string);
        } else {
            textView.setTextColor(InquiryDetail.this.getResources().getColor(R.color.mild_red));
            textView.setText("-NA-");
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(Global_variable.LOG_TAG,"CHECK");
        Log.e(Global_variable.LOG_TAG,"FavoriteFlag value:: "+faroviteFlag);
//        FavoriteCallback.getfavorite(faroviteFlag);
    }*/

    public void addFavorite(String favorite) {
        faroviteFlag = "true";
        favoriteCallback.getfavorite(faroviteFlag);
        Log.e(Global_variable.LOG_TAG, "FavoriteFlag value:: " + faroviteFlag);
       /* inquiries.Favorite="true";
        Log.e(Global_variable.LOG_TAG,"Favorite Value:: "+inquiries.Favorite);
*//*
        Bundle bundle = new Bundle();
        bundle.putBoolean("favorite", faroviteFlag);
        // set Fragmentclass Arguments
       Inquiries inquiriesobject=new Inquiries();
        inquiriesobject.setArguments(bundle);*/
        RestClientVolley restClientVolley = new RestClientVolley(InquiryDetail.this, Request.Method.POST, favoriteUrl, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if (Global_variable.logEnabled) {
                    Log.e("***fav response", response);
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (Global_variable.logEnabled) {
                        Log.e("status", status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        restClientVolley.addParameter("inq_id", inquiry_id);
        restClientVolley.addParameter("user_id", id);
        restClientVolley.addParameter("status", favorite);
        restClientVolley.progressDialogVisibility(true);
        restClientVolley.executeRequest();
    }

    public interface FavoriteCallback {
        public void getfavorite(String favorite);
    }

    public static void setOnFavoriteSelected(FavoriteCallback favoriteSelected) {
        favoriteCallback = favoriteSelected;
    }
}
