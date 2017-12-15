package com.quixomtbx.jewelrap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;
import java.util.Locale;

import static com.quixomtbx.jewelrap.R.id.tv_certificatenum_value_SrchItmDetail;


public class SearchItemDetails extends AppCompatActivity implements View.OnClickListener {
    TextView tvshape, tvCerti, tvPurity, tvColor, tvSize, tvCut, tvPriceDis, tvDept, tvFlour, tvSymm, tvMeasure, tvPolish,
            tvTa, tvUserAddress, tvUserName, tvUserFirmname, toolbartitle, tvtextpricedis, tvstockno, tvCertiNum;
    ImageView IVshape;
    LinearLayout certificateLinear, linearlayoutSolitaire, linearlayoutLoosedimaond, layoutPersonal, layoutLandline, layoutMessage, layoutEmail;
    Toolbar toolbar;
    String username, url, userPersonal, userLandline, userEmail;
    CircularImageView circularImageView;
    TextView tvldshape, tvldPurity, tvldColor, tvldSize, tvldPriceDis, tvstate, tvldshade;
    String categoryId = "";
    FirebaseAnalytics firebaseAnalytics;
    String name, uniqueid, inquiry_id;
    SharedPreferences sharedPreferences;
    String categoryName = "";
    String TAG = SearchItemDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_details);
        initializeAllComponent();
        setValueToAllComponnt();

    }

    void initializeAllComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        tvshape = (TextView) findViewById(R.id.tv_shape_SrchDetail);
        tvCerti = (TextView) findViewById(R.id.tv_certification_value_SrchItmDetail);
        tvSize = (TextView) findViewById(R.id.tv_size_value_SrchItmDetail);
        tvCut = (TextView) findViewById(R.id.tv_cutgrade_value_SrchItmDetail);
        tvPriceDis = (TextView) findViewById(R.id.tv_price_SrchItmDetail);
        tvDept = (TextView) findViewById(R.id.tv_depth_value_SrchItmDetail);
        tvFlour = (TextView) findViewById(R.id.tv_flo_value_SrchItmDetail);
        tvSymm = (TextView) findViewById(R.id.tv_sym_value_SrchItmDetail);
        tvMeasure = (TextView) findViewById(R.id.tv_measurement_value_SrchItmDetail);
        tvPolish = (TextView) findViewById(R.id.tv_polish_value_SrchItmDetail);
        tvTa = (TextView) findViewById(R.id.tv_ta_value_SrchItmDetail);
        tvtextpricedis = (TextView) findViewById(R.id.tv_textpricedis);
        tvUserAddress = (TextView) findViewById(R.id.tv_userAddress_SrchItmDetail);

        tvPurity = (TextView) findViewById(R.id.tv_purity_value_SrchItmDetail);
        tvColor = (TextView) findViewById(R.id.tv_color_value_SrchItmDetail);
        tvUserName = (TextView) findViewById(R.id.tv_userName_SrchItmDetail);
        tvUserFirmname = (TextView) findViewById(R.id.tv_user_FirmName_SrchItmDetail);
        tvstockno = (TextView) findViewById(R.id.textview_stockno);
        tvCertiNum = (TextView) findViewById(tv_certificatenum_value_SrchItmDetail);
//        IVphone = (ImageView) findViewById(R.id.iv_inquiry_detail_phone);
//        IVmessage = (ImageView) findViewById(R.id.iv_inquiry_detail_message);
        layoutPersonal = (LinearLayout) findViewById(R.id.ll_search_personal);
        layoutLandline = (LinearLayout) findViewById(R.id.ll_search_landline);
        layoutMessage = (LinearLayout) findViewById(R.id.ll_search_message);
        layoutEmail = (LinearLayout) findViewById(R.id.ll_search_email);
        IVshape = (ImageView) findViewById(R.id.iv_shape_search_detail);
        layoutEmail = (LinearLayout) findViewById(R.id.ll_search_email);


        // tvldshape = (TextView) findViewById(R.id.tv_ld_shape_SrchDetail);
        tvldshade = (TextView) findViewById(R.id.tv_ld_shade_value_SrchItmDetail);
        tvldSize = (TextView) findViewById(R.id.tv_ld_size_value_SrchItmDetail);
        // tvldstate = (TextView) findViewById(R.id.tv_ld_state_value_SrchItmDetail);
        // tvldPriceDis = (TextView) findViewById(R.id.tv_ld_price_SrchItmDetail);
        tvldPurity = (TextView) findViewById(R.id.tv_ld_purity_value_SrchItmDetail);
        tvldColor = (TextView) findViewById(R.id.tv_ld_color_value_SrchItmDetail);


        layoutPersonal.setOnClickListener(this);
        layoutLandline.setOnClickListener(this);
        layoutMessage.setOnClickListener(this);
        layoutEmail.setOnClickListener(this);

        circularImageView = (CircularImageView) findViewById(R.id.serach_profile_image);
        circularImageView.setOnClickListener(this);
        linearlayoutLoosedimaond = (LinearLayout) findViewById(R.id.loosediamond_item_details_layout);
        linearlayoutSolitaire = (LinearLayout) findViewById(R.id.solitaire_item_details_layout);
        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);

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


        tvCertiNum.setOnClickListener(this);
        Log.d(Global_variable.LOG_TAG, "color : " + ContextCompat.getColor(this, R.color.icon_gray));
    }

    void setValueToAllComponnt() {
        toolbartitle.setText("Diamond Details");
        Intent in = getIntent();
        String getjsonobject = in.getStringExtra("JsonObject");
        categoryId = in.getStringExtra("categoryid");
        categoryName = in.getStringExtra("categoryName");
        inquiry_id = in.getStringExtra("inquiry_id");
        // Log.e("JEWELRAP ","category name : "+categoryName);
        try {
            JSONObject jsonObject = new JSONObject(getjsonobject);
            String shapename = null, certificatename = null;
            if (categoryId.equals("1")) {
                linearlayoutSolitaire.setVisibility(View.VISIBLE);

                populateData(jsonObject, "certification", "certified_name", tvCertiNum);
                populateData(jsonObject, "certification", "certified_name", tvCerti);
                populateData(jsonObject, "purity", "purity_name", tvPurity);
                populateData(jsonObject, "color", "color_name", tvColor);
                populateStringData(jsonObject, "cut_grade", tvCut);
                populateStringData(jsonObject, "ta", tvTa);
                populateStringData(jsonObject, "td", tvDept);
                populateStringData(jsonObject, "fluorescence", tvFlour);
                populateStringData(jsonObject, "symmetry", tvSymm);
                populateStringData(jsonObject, "polish", tvPolish);
                //  populateStringData(jsonObject, "measurement", tvMeasure);
                if (!jsonObject.optString("measurement").isEmpty()) {
                    tvMeasure.setText(jsonObject.optString("measurement"));
                } else {
                    tvMeasure.setText("-NA-");
                }

                String price = jsonObject.optString("price");
                String discount = jsonObject.optString("discount");
                String certificatenum = jsonObject.optString("certificate_num");
                if (!certificatenum.equals("")) {
                    tvCertiNum.setText(certificatenum);
                    tvCertiNum.setPaintFlags(tvCertiNum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    tvCertiNum.setText("-NA-");
                }
                if (!price.equals("null") && price != null) {
                    tvPriceDis.setText(Currency.getInstance(Locale.US).getSymbol() + " " + price);
                } else if (!discount.equals("null") && discount != null) {
                    tvPriceDis.setText(discount + " %");
                } /*else {
                    tvPriceDis.setText("-NA-");
                }*/

                if (!jsonObject.optString("size").equals("null")) {
                    tvSize.setText(jsonObject.optString("size") + " ct");
                } else {
                    tvSize.setText("-NA-");
                }
            } else if (categoryId.equals("2")) {
                linearlayoutLoosedimaond.setVisibility(View.VISIBLE);
                tvCerti.setVisibility(View.INVISIBLE);
               /* populateArrayData(jsonObject,"purity",tvldPurity);
                populateArrayData(jsonObject,"color",tvldColor);*/
                populateStringData(jsonObject, "color_rng", tvldColor);
                populateStringData(jsonObject, "purity_rng", tvldPurity);
                populateData(jsonObject, "shade", "shade_name", tvldshade);
                populateData(jsonObject, "size", "size_name", tvldSize);
                String price = jsonObject.optString("price");

                if (!price.equals("null") && price != null) {
                    tvPriceDis.setText(getResources().getString(R.string.rs) + " " + price);
                } /*else {
                    tvPriceDis.setText("-NA-");
                }*/
            }
            if (!jsonObject.isNull("shape")) {
                shapename = jsonObject.getJSONObject("shape").optString("shape_name");
                tvshape.setText(shapename);
                Glide.with(getApplicationContext())
                        .load(Global_variable.base_url + jsonObject.getJSONObject("shape").optString("shape_image"))
                        .into(IVshape);
            }

            if (!jsonObject.optString("stock").equals("")) {
                tvstockno.setText(Html.fromHtml("<b>Stock No: </b>" + jsonObject.optString("stock")));
            } else {
                tvstockno.setText(Html.fromHtml("<b>Stock No: </b>" + " -NA-"));
            }


            username = jsonObject.getJSONObject("user").getString("first_name") + " " + jsonObject.getJSONObject("user").getString("last_name");
            tvUserName.setText(jsonObject.getJSONObject("user").getString("first_name") + " " + jsonObject.getJSONObject("user").getString("last_name") + " (" + jsonObject
                    .optJSONObject("user").optJSONObject("user_profile").getString("unique_id") + ")");
            tvUserFirmname.setText(jsonObject.getJSONObject("user").getJSONObject("user_profile").getString("firm_name"));
            userPersonal = jsonObject.getJSONObject("user").getJSONObject("user_profile").getString("phone_num");
            userLandline = jsonObject.getJSONObject("user").getJSONObject("user_profile").getString("landline");
            userEmail = jsonObject.getJSONObject("user").getString("email");
            tvUserAddress.setText(jsonObject.getJSONObject("user").getJSONObject("user_profile").getString("address") +
                    ", " + jsonObject.getJSONObject("user").getJSONObject("user_profile").getString("city") +
                    ", " + jsonObject.getJSONObject("user").getJSONObject("user_profile").getString("state")
            );

            url = Global_variable.base_url + jsonObject.optJSONObject("user").optJSONObject("user_profile").getString("profile_image");
            Log.e("** url ", " " + url);
            Glide.with(getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .error(R.drawable.ic_launcher)
                    .placeholder(R.drawable.ic_launcher)
                    .into(circularImageView);

            // firebase Analytics for view view stock details..
            Bundle bundle = new Bundle();
            bundle.putString("jewelrap_id", uniqueid);
            bundle.putString("inquiry_id", inquiry_id);
            bundle.putString("user_name", name);
            bundle.putString("category_name", categoryName);
            bundle.putString("contact_person_name", username);
            bundle.putString("firm_name", jsonObject.optJSONObject("user").optJSONObject("user_profile").optString("firm_name"));
            firebaseAnalytics.logEvent("viewstock_detail", bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void populateData(JSONObject jsonObject, String key, String itemName, TextView view) {
        String name;
        try {
            if (!jsonObject.optString(key).equals("null")) {
                name = jsonObject.getJSONObject(key).optString(itemName);
                view.setText(name);
            } else {
                view.setText("-NA-");
            }
        } catch (JSONException e) {

        }
    }

    private void populateArrayData(JSONObject jsonObject, String key, TextView view) {
        String name;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            if (jsonArray.length() > 0) {
                name = jsonArray.getString(0);
                view.setText(name);
            } else {
                view.setText("-NA-");
            }
        } catch (JSONException e) {

        }
    }

    private void populateStringData(JSONObject jsonObject, String jsonObjString, TextView view) {
        String value = jsonObject.optString(jsonObjString);
       /* if(!jsonObject.optString(jsonObjString).equals("null")) {
            view.setText(jsonObject.optString(jsonObjString));
        }else{
            view.setText("-NA-");
        }*/
        if (value.equals("null") || value == null || value.isEmpty()) {
            view.setText("-NA-");
        } else {
            view.setText(value);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search_message:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", userPersonal);
                smsIntent.putExtra("sms_body", "I got your enquiry through JewelRap:- \n ");
                startActivity(smsIntent);
                Bundle bundleMsg = new Bundle();
                bundleMsg.putString("user_name", name);
                bundleMsg.putString("jewelrap_id", uniqueid);
                bundleMsg.putString("category_name", categoryName);
                bundleMsg.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundleMsg.putString("phone_number", userPersonal);
                bundleMsg.putString("inquiry_id", inquiry_id);
                firebaseAnalytics.logEvent("viewstock_message", bundleMsg);
                break;
            case R.id.ll_search_personal:
                String message = "tel:" + userPersonal;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(message));
                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("user_name", name);
                bundle.putString("jewelrap_id", uniqueid);
                bundle.putString("inquiry_id", inquiry_id);
                bundle.putString("category_name", categoryName);
                bundle.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundle.putString("phone_number", userPersonal);
                firebaseAnalytics.logEvent("viewstock_call", bundle);
                break;
            case R.id.ll_search_email:
                String email = userEmail;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "JewelRap");
                String signatureName = sharedPreferences.getString(SessionManager.FIRST_NAME, "") + " " + sharedPreferences.getString(SessionManager.LAST_NAME, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "I got your enquiry through JewelRap. + \n \n \n \n \n \n " + signatureName + "\n" + uniqueid);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                Bundle bundleMail = new Bundle();
                bundleMail.putString("user_name", name);
                bundleMail.putString("jewelrap_id", uniqueid);
                bundleMail.putString("inquiry_id", inquiry_id);
                bundleMail.putString("category_name", categoryName);
                bundleMail.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundleMail.putString("contact_person_emailid", userEmail);
                firebaseAnalytics.logEvent("viewstock_mail", bundleMail);
                break;
            case R.id.serach_profile_image:
               /*circularImageView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.image_click));
                Intent in=new Intent(getApplicationContext(),ProfileView.class);
                in.putExtra("username",username);
                in.putExtra("profileview",url);
                startActivity(in);*/
                Intent in = new Intent(this, ProfileView.class);
                in.putExtra("username", username);
                in.putExtra("profileview", url);
                String transitionName = getString(R.string.transition_name);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                circularImageView,   // The view which starts the transition
                                transitionName    // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity(this, in, options.toBundle());
                break;
            case R.id.ll_search_landline:

                String phone = "tel:" + userLandline;
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse(phone));
                startActivity(intent1);
                Bundle bundle1 = new Bundle();
                bundle1.putString("user_name", name);
                bundle1.putString("jewelrap_id", uniqueid);
                bundle1.putString("inquiry_id", inquiry_id);
                bundle1.putString("category_name", categoryName);
                bundle1.putString("contact_person_name", tvUserName.getText().toString().trim());
                bundle1.putString("landline", userPersonal);
                firebaseAnalytics.logEvent("enquiry_call_landline", bundle1);
                break;
            case tv_certificatenum_value_SrchItmDetail:
                if (tvCerti.getText().toString().trim().equalsIgnoreCase("GIA")) {
                    String gia_url = "https://www.gia.edu/report-check?reportno=" + tvCertiNum.getText().toString().trim();
                    openURL(gia_url);
                    Log.d(TAG, "certification " + tvCertiNum.getText());
                } else if (tvCerti.getText().toString().trim().equalsIgnoreCase("IGI")) {
                    String igi_uri = "http://www.igiworldwide.com/verify.php?r=" + tvCertiNum.getText().toString().trim();
                    openURL(igi_uri);
                    Log.d(TAG, "certification " + tvCertiNum.getText());
                } else if (tvCerti.getText().toString().trim().equalsIgnoreCase("HRD")) {
                    RestClientVolley restClientVolley = new RestClientVolley(this, Request.Method.POST, "https://my.hrdantwerp.com/", new VolleyCallBack() {
                        @Override
                        public void processCompleted(String response) {
                            Intent newIntent = new Intent(SearchItemDetails.this, HRDActivity.class);
                            newIntent.putExtra("response", response);
                            startActivity(newIntent);
                            overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                        }
                    });
                    restClientVolley.addParameter("record_number", tvCertiNum.getText().toString().trim());
                    restClientVolley.executeRequest();
                }

                break;
        }
    }

    void openURL(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
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
