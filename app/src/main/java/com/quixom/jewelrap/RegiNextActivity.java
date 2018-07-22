package com.quixom.jewelrap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RegiNextActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String REG_ID = "regId";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = Global_variable.google_key;
    private static final String APP_VERSION = "appVersion";
    List<String> lst_check;
    LinearLayout linearLayoutcheck, checkedlayout, linearQBC;
    CheckBox chkTeamName;
    List<String> selectvalue;
    int i;

    SharedPreferences pref;
    String code="";
    Button btnchecked;
    RadioGroup radioGroup;
    RadioButton radioButton_userrole, radioButton_broker;
    String userrole = "";
    Button btnregister;
    String str = "", cate_str = "";
    EditText et_pincode, et_tinnumber, et_qbcNumber;
    //AutoCompleteTextView auto_city;
    String fnm, lnm, pwd, pwd_conf, phone, address, state, country, pincode, email, city, firm, landline;
    String url = Global_variable.signup_api;
    SessionManager session;
    boolean[] selectedItems, clickedItems;
    String multiplestr;
    String deviceId = "";
    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    Dialog dialog;
    Button btn_so, btn_loss, btn_diaje, btn_goldje, btn_silver, btn_colorstone;
    String solvalue, lossvalue, diajevalue, goldjevalue, silverValue, colorstoneValue;
    TypedArray imagearray;
    RadioGroup radioGroupselectionRadioGroup1;
    RadioButton radioButtonssolitaire, radioButtonloose, radioButtongj, radioButtondj, radioButtonSilver, radioButtonColorstone;
    TextView tvsolitaire, tvloose, tvgj, tvdj, textViewSilver, textViewColorstone;
    Spinner spinnerCity, spinnerState;
    String stateUrl = Global_variable.state_api;
    String cityUrl = Global_variable.city_api;
    ArrayList<JewelRapItem> stateArrayList, cityArrayList;
    ArrayList<String> stateList, cityList;
    String stateId = "", cityId = "";
    String qbcnumber = "";
    LinearLayout linearSolitaire, linearLooseDiamond, linearDiamondJewellery, linearGoldJewellery, linearSilver, linearColorStone;
    boolean checked = true;
    Switch enterrefer;
    CheckBox checkBox;
    View terms_conditionsView;
    RelativeLayout relativeLayouttnc;
    Button buttontnc, buttontnccancel;
    TextView textViewtnc;
    AlertDialog builder;

    EditText reffercode;
    String urlterms_conditions = "http://jewelrap.com/user/terms_condition/";
    // String urlterms_conditions="http://developer.jewelrap.com/user/terms_condition/";

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //  boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.linearLooseDiamond:
                    cate_str = tvloose.getText().toString();

                    radioButtondj.setChecked(!checked);
                    radioButtongj.setChecked(!checked);
                    radioButtonssolitaire.setChecked(!checked);
                    radioButtonSilver.setChecked(!checked);
                    radioButtonColorstone.setChecked(!checked);
                    radioButtonloose.setChecked(checked);
                    break;
                case R.id.linearSolitaire:
                    cate_str = tvsolitaire.getText().toString();
                    radioButtonssolitaire.setChecked(checked);
                    radioButtondj.setChecked(!checked);
                    radioButtongj.setChecked(!checked);
                    radioButtonloose.setChecked(!checked);
                    radioButtonSilver.setChecked(!checked);
                    radioButtonColorstone.setChecked(!checked);
                    break;
                case R.id.linearGoldJewellery:
                    cate_str = tvgj.getText().toString();
                    radioButtongj.setChecked(checked);
                    radioButtondj.setChecked(!checked);
                    radioButtonssolitaire.setChecked(!checked);
                    radioButtonloose.setChecked(!checked);
                    radioButtonSilver.setChecked(!checked);
                    radioButtonColorstone.setChecked(!checked);
                    break;
                case R.id.linearDiamondJewellery:
                    cate_str = tvdj.getText().toString();
                    radioButtondj.setChecked(checked);
                    radioButtonssolitaire.setChecked(!checked);
                    radioButtongj.setChecked(!checked);
                    radioButtonloose.setChecked(!checked);
                    radioButtonSilver.setChecked(!checked);
                    radioButtonColorstone.setChecked(!checked);
                    break;
                case R.id.linearSilver:
                    cate_str = textViewSilver.getText().toString();
                    radioButtonSilver.setChecked(checked);
                    radioButtonssolitaire.setChecked(!checked);
                    radioButtongj.setChecked(!checked);
                    radioButtonloose.setChecked(!checked);
                    radioButtondj.setChecked(!checked);
                    radioButtonColorstone.setChecked(!checked);
                    break;
                case R.id.linearColorStone:
                    cate_str = textViewColorstone.getText().toString();
                    radioButtonColorstone.setChecked(checked);
                    radioButtonssolitaire.setChecked(!checked);
                    radioButtongj.setChecked(!checked);
                    radioButtonloose.setChecked(!checked);
                    radioButtondj.setChecked(!checked);
                    radioButtonSilver.setChecked(!checked);
                    break;

            }

        }
    };
    private Toolbar toolbar;

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {

        }

        return resultList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_regi_next);
        enterrefer=(Switch)findViewById(R.id.switch_btn);

        reffercode=(EditText)findViewById(R.id.refer_code);
        pref = getSharedPreferences("MyPrefs" , Context.MODE_PRIVATE);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        selectedItems = new boolean[]{false, false, false, false,
        };
        clickedItems = selectedItems;

        initView();
        getAllState();

        colorFilter(getApplicationContext(), R.drawable.ic_crosshairs_gps_black_24dp);
        colorFilter(getApplicationContext(), R.drawable.ic_map_marker_black_24dp);
        colorFilter(getApplicationContext(), R.drawable.tin_num);
        colorFilter(getApplicationContext(), R.drawable.i_am);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (radioGroup.getCheckedRadioButtonId() <= 0) {
                    //userrole = "";
                    userrole = "retailer";
                } else {
                    int radioid = radioGroup.getCheckedRadioButtonId();
                    radioButton_userrole = (RadioButton) findViewById(radioid);
                    userrole = radioButton_userrole.getText().toString();
                }
                if (Global_variable.logEnabled) {
                    Log.e(">>> UserListener", userrole);
                }

                switch (id) {
                    case R.id.radioButton2: {
                        categorySelectionDialog(userrole);
                    }
                    break;
                    case R.id.radioButtonBroker: {
                        categorySelectionDialog(userrole);
                    }
                    break;
                    case R.id.radioButton1:
                        checkedlayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        builder = new AlertDialog.Builder(RegiNextActivity.this, R.style.DesignDemo).create();
        buttontnc = (Button) terms_conditionsView.findViewById(R.id.buttontnc);
        buttontnccancel = (Button) terms_conditionsView.findViewById(R.id.buttontnccancel);
        textViewtnc = (TextView) terms_conditionsView.findViewById(R.id.textViewtnc);
        textViewtnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openURL(urlterms_conditions);
            }
        });
        buttontnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

        buttontnccancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setView(terms_conditionsView);
        enterrefer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    reffercode.setVisibility(View.VISIBLE);


                }
                else
                {
                    reffercode.setVisibility(View.GONE);


                }

            }
        });

        Log.e("pk",""+code);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = getIntent();
                fnm = in.getStringExtra("firstnm");
                lnm = in.getStringExtra("lastnm");
                pwd = in.getStringExtra("pwd");
                pwd_conf = in.getStringExtra("conf_pwd");
                email = in.getStringExtra("email");
                phone = in.getStringExtra("phone");
                address = in.getStringExtra("address");
                firm = in.getStringExtra("firmname");
                landline = in.getStringExtra("landline");
                pincode = et_pincode.getText().toString();
                qbcnumber = et_qbcNumber.getText().toString();
                checkEmptyTextValidation(et_pincode);
                if (Global_variable.logEnabled) {
                    Log.e(">>USERROLE ", userrole);
                }
                if (et_pincode.getError() == null) {
                    if (validateState() && validateCity()) {
                        if (!userrole.equals("")) {
                            if (userrole.equals("Wholesaler") || userrole.equals("Broker")) {
                                if (et_tinnumber.getText().length() > 0) {
                                    checkEmptyTextValidatiototinnumber(et_tinnumber);
                                    if (et_tinnumber.getError() == null) {
                                        if (Global_variable.logEnabled) {
                                            Log.e(">>>cate ", "" + cate_str);
                                        }
                                        if (TextUtils.isEmpty(regId)) {
                                            regId = registerGCM();
                                        }

                                       /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                            }
                                        });
*/
                                        builder.show();

                                    }
                                } else {
                                    if (Global_variable.logEnabled) {
                                        Log.e(">>>cate ", "" + cate_str);
                                    }
                                    if (TextUtils.isEmpty(regId)) {
                                        regId = registerGCM();
                                    }
                                   /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                        }
                                    });*/

                                    builder.show();

                                }

                            } else {
                                checkEmptyTextValidatiototinnumber(et_tinnumber);
                                if (et_tinnumber.getError() == null) {
                                    if (TextUtils.isEmpty(regId)) {
                                        regId = registerGCM();
                                    }
                                   /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                        }
                                    });*/

                                    builder.show();


                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select userrole", Toast.LENGTH_LONG).show();
                            radioGroup.requestFocus();
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "City and state are required", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Pincode required", Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        builder.cancel();

    }

    void openURL(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
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

    public void categorySelectionDialog(String role) {
        //String user_role=role;
        if (Global_variable.logEnabled) {
            Log.e("cate str val:ron2", cate_str);
            Log.e("userrole::radiobutton2", userrole);
        }
        dialog = new Dialog(RegiNextActivity.this, R.style.DesignDemo);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_checkbox_dialog);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        getView(dialog);
        linearSolitaire.setOnClickListener(clickListener);
        linearLooseDiamond.setOnClickListener(clickListener);
        linearDiamondJewellery.setOnClickListener(clickListener);
        linearGoldJewellery.setOnClickListener(clickListener);
        linearSilver.setOnClickListener(clickListener);
        linearColorStone.setOnClickListener(clickListener);
        /*radioButtonssolitaire.setOnClickListener(clickListener);
        radioButtonloose.setOnClickListener(clickListener);*/
       /* radioButtongj.setOnClickListener(clickListener);
        radioButtondj.setOnClickListener(clickListener);
        radioButtonSilver.setOnClickListener(clickListener);
        radioButtonColorstone.setOnClickListener(clickListener);*/
        final Button button = (Button) dialog.findViewById(R.id.buttonok);
        linearLayoutcheck.setVisibility(View.VISIBLE);
        lst_check = new ArrayList<String>();
        selectvalue = new ArrayList<String>();
        final String[] categoryJwl = getResources().getStringArray(
                R.array.CategoriesJewel);
        lst_check = Arrays.asList(categoryJwl);
        btn_so.setVisibility(View.GONE);
        btn_loss.setVisibility(View.GONE);
        btn_diaje.setVisibility(View.GONE);
        btn_goldje.setVisibility(View.GONE);
        cate_str = "";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Log.e(">>> cate str val :: radiobutton2", cate_str);
                if (!cate_str.equals("")) {
                    if (cate_str.contains(solvalue)) {
                        btn_so.setVisibility(View.VISIBLE);
                    } else {
                        btn_so.setVisibility(View.GONE);
                    }
                    if (cate_str.contains(lossvalue)) {
                        btn_loss.setVisibility(View.VISIBLE);
                    } else {
                        btn_loss.setVisibility(View.GONE);
                    }
                    if (cate_str.contains(diajevalue)) {
                        btn_diaje.setVisibility(View.VISIBLE);
                    } else {
                        btn_diaje.setVisibility(View.GONE);
                    }
                    if (cate_str.contains(goldjevalue)) {
                        btn_goldje.setVisibility(View.VISIBLE);
                    } else {
                        btn_goldje.setVisibility(View.GONE);
                    }
                    if (cate_str.contains(silverValue)) {
                        btn_silver.setVisibility(View.VISIBLE);
                    } else {
                        btn_silver.setVisibility(View.GONE);
                    }
                    if (cate_str.contains(colorstoneValue)) {
                        btn_colorstone.setVisibility(View.VISIBLE);
                    } else {
                        btn_colorstone.setVisibility(View.GONE);
                    }
                    checkedlayout.setVisibility(View.VISIBLE);
                    dialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "Category required", Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initView() {
        et_pincode = (EditText) findViewById(R.id.edit_text_pincode);
        et_tinnumber = (EditText) findViewById(R.id.edit_tin_number);
        et_qbcNumber = (EditText) findViewById(R.id.edit_qbc_number);
        linearQBC = (LinearLayout) findViewById(R.id.linearQbc);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroupreg);
        checkedlayout = (LinearLayout) findViewById(R.id.linearusercheckbox);
        btnregister = (Button) findViewById(R.id.btn_registration);
        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinnerState.setOnItemSelectedListener(this);
        spinnerCity.setOnItemSelectedListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");
        radioButton_broker = (RadioButton) findViewById(R.id.radioButtonBroker);
        btn_so = (Button) findViewById(R.id.btn_solitaire);
        btn_loss = (Button) findViewById(R.id.btn_loosediamonds);
        btn_diaje = (Button) findViewById(R.id.btn_diamondjewellery);
        btn_goldje = (Button) findViewById(R.id.btn_goldjewellery);
        btn_colorstone = (Button) findViewById(R.id.btn_colorstone);
        btn_silver = (Button) findViewById(R.id.btn_silver);
        solvalue = btn_so.getText().toString();
        lossvalue = btn_loss.getText().toString();
        diajevalue = btn_diaje.getText().toString();
        goldjevalue = btn_goldje.getText().toString();
        silverValue = btn_silver.getText().toString();
        colorstoneValue = btn_colorstone.getText().toString();
        imagearray = getResources().obtainTypedArray(R.array.random_imgs);
        stateArrayList = new ArrayList<JewelRapItem>();
        cityArrayList = new ArrayList<JewelRapItem>();
        stateList = new ArrayList<String>();
        cityList = new ArrayList<String>();
        session = new SessionManager(getApplicationContext());

        terms_conditionsView = View.inflate(this, R.layout.terms_conditions, null);
        checkBox = (CheckBox) terms_conditionsView.findViewById(R.id.checkbox);
        relativeLayouttnc = (RelativeLayout) findViewById(R.id.relativeLayouttnc);


    }

    private void getAllState() {
        RestClientVolley restClientVolley = new RestClientVolley(RegiNextActivity.this, Request.Method.GET, stateUrl, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                populateState(response);
            }
        });
        restClientVolley.executeRequest();
    }

    private void populateState(String response) {
        stateArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("objects");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String stateid = object.getString("id");
                    String statename = object.getString("state_name");
                    boolean has_broker = object.getBoolean("has_broker");
                    JewelRapItem jewelRapItem = new JewelRapItem();
                    jewelRapItem.setId(stateid);
                    jewelRapItem.setName(statename);
                    jewelRapItem.setHas_broker(has_broker);
                    stateArrayList.add(jewelRapItem);
                }

                Collections.sort(stateArrayList, new Comparator<JewelRapItem>() {
                    @Override
                    public int compare(JewelRapItem lhs, JewelRapItem rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (stateArrayList != null && stateArrayList.size() > 0) {
            stateList.clear();

            JewelRapItem model;
            stateList.add("--Select State--");
            for (int i = 0; i < stateArrayList.size(); i++) {
                model = stateArrayList.get(i);
                String district = model.getName();
                stateList.add(district);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegiNextActivity.this, android.R.layout.simple_spinner_item) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position == 0) {
                        ((TextView) v.findViewById(android.R.id.text1)).setText("");
                        ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(position)); //"Hint to be displayed"
                    }
                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {

                    if (position == 0) {
                        return new View(parent.getContext());
                    } else {
                        return super.getDropDownView(position, null, parent);
                    }
                }

            };

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            adapter.addAll(stateList);
            spinnerState.setAdapter(adapter);
            spinnerState.setSelection(0);


            cityList.clear();
            cityList.add("--Select City--");
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RegiNextActivity.this, android.R.layout.simple_spinner_item) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position == 0) {
                        ((TextView) v.findViewById(android.R.id.text1)).setText("");
                        ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(position)); //"Hint to be displayed"
                    }
                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {

                    if (position == 0) {
                        return new View(parent.getContext());
                    } else {
                        return super.getDropDownView(position, null, parent);
                    }
                }

            };

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            adapter1.addAll(cityList);
            spinnerCity.setAdapter(adapter1);
            spinnerCity.setSelection(0);


        }
    }

    private void registerUser() {
        code=reffercode.getText().toString();
        RestClientVolley restClientVolley = new RestClientVolley(RegiNextActivity.this, Request.Method.POST, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                userRegistered(response);
            }
        });
        restClientVolley.addParameter("firstname", fnm);
        restClientVolley.addParameter("firm_name", firm);
        restClientVolley.addParameter("lastname", lnm);
        restClientVolley.addParameter("userrole", userrole.toLowerCase());
        restClientVolley.addParameter("tin_num", et_tinnumber.getText().toString());
        if (Global_variable.logEnabled) {
            Log.e(">>categories", cate_str);
        }
        if (Global_variable.logEnabled) {
            Log.e("address", address);
            Log.e("cityid", cityId);
            Log.e("state", stateId);
            Log.e("pin", et_pincode.getText().toString());
            Log.e("device", deviceId);
            Log.e("qbcnumber", qbcnumber);
        }
        restClientVolley.addParameter("categories", cate_str);
        restClientVolley.addParameter("password", pwd);
        restClientVolley.addParameter("emailid", email);
        restClientVolley.addParameter("phone_num", phone);
        restClientVolley.addParameter("address", address);
        restClientVolley.addParameter("landline", landline);

        restClientVolley.addParameter("qbc", qbcnumber);
        restClientVolley.addParameter("city", cityId);
        restClientVolley.addParameter("state", stateId);
        restClientVolley.addParameter("pin", et_pincode.getText().toString());
        restClientVolley.addParameter("deviceId", deviceId);
        restClientVolley.addParameter("tnc", "1");
        restClientVolley.addParameter("referral_code",code);
        //pref.getString("refer" , "")
        Log.e("????",""+userrole+code);
        restClientVolley.executeRequest();

    }

    private void userRegistered(String response) {
        String message, status;
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            if (Global_variable.logEnabled) {
                Log.e("registrationresponse", jsonObj.toString());
            }
            status = jsonObj.getString("status");
            message = jsonObj.optString("message");
            if (status.equals("ok")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);

                /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RegiNextActivity.this);
                dlgAlert.setMessage(message);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(in);
                                overridePendingTransition(R.layout.push_right_out,R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();*/


            } else {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RegiNextActivity.this, R.style.DesignDemo);
                dlgAlert.setMessage(message);
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

    private void getView(Dialog dialog) {
        linearLayoutcheck = (LinearLayout) dialog.findViewById(R.id.customlinear);
        tvsolitaire = (TextView) dialog.findViewById(R.id.tvCategorySolitaire);
        tvloose = (TextView) dialog.findViewById(R.id.tvCategoryLooseDiamond);
        tvdj = (TextView) dialog.findViewById(R.id.tvCategoryDJ);
        tvgj = (TextView) dialog.findViewById(R.id.tvCategoryGJ);
        textViewColorstone = (TextView) dialog.findViewById(R.id.tvCategoryColorstone);
        textViewSilver = (TextView) dialog.findViewById(R.id.tvCategorySilver);
        radioButtonssolitaire = (RadioButton) dialog.findViewById(R.id.radioSolitaire);
        radioButtonloose = (RadioButton) dialog.findViewById(R.id.radioLooseDiamond);
        radioButtongj = (RadioButton) dialog.findViewById(R.id.radioGJ);
        radioButtondj = (RadioButton) dialog.findViewById(R.id.radioDJ);
        radioButtonColorstone = (RadioButton) dialog.findViewById(R.id.radioColorstone);
        radioButtonSilver = (RadioButton) dialog.findViewById(R.id.radioSilver);
        linearSolitaire = (LinearLayout) dialog.findViewById(R.id.linearSolitaire);
        linearLooseDiamond = (LinearLayout) dialog.findViewById(R.id.linearLooseDiamond);
        linearDiamondJewellery = (LinearLayout) dialog.findViewById(R.id.linearDiamondJewellery);
        linearGoldJewellery = (LinearLayout) dialog.findViewById(R.id.linearGoldJewellery);
        linearSilver = (LinearLayout) dialog.findViewById(R.id.linearSilver);
        linearColorStone = (LinearLayout) dialog.findViewById(R.id.linearColorStone);

    }

    public void onClick(View v) {

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();


        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {

            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {

            return "";
        }
        return registrationId;
    }

    private void registerInBackground() {
        new GmsGetId().execute();

    }

    public void checkEmptyTextValidation(EditText et) {
        String st = et.getText().toString();
        if (st.length() == 0) {
            et.setError("Please enter value");
            et.requestFocus();
        } else {
            et.setError(null);
        }

    }

    public void checkEmptyTextValidatiototinnumber(EditText et) {

        String st = et.getText().toString();
        if (st.length() == 15) {
            if (isAlphaNumeric(st)) {
                et.setError(null);
            } else {
                et.setError("invaild tin number");
                et.requestFocus();
            }
        } else {
            et.setError("Tinnumber should be of 15 digit");
            et.requestFocus();
        }

    }

    public boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";

        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        switch (spinner.getId()) {
            case R.id.spinnerState: {
                if (i > 0) {
                    stateId = stateArrayList.get(i - 1).getId();
                    if (Global_variable.logEnabled) {
                        Log.e("**has_broker", "" + stateArrayList.get(i - 1).isHas_broker());
                    }
                    if (stateArrayList.get(i - 1).isHas_broker()) {
                        radioButton_broker.setVisibility(View.VISIBLE);
                    } else {
                        btn_diaje.setVisibility(View.GONE);
                        btn_goldje.setVisibility(View.GONE);
                        btn_loss.setVisibility(View.GONE);
                        btn_so.setVisibility(View.GONE);
                        radioButton_broker.setVisibility(View.GONE);
                        if (userrole.equals("Broker")) {
                            cate_str = "";
                            userrole = "";
                        }
                    }

                    if (Global_variable.logEnabled) {
                        Log.e("**State ID", stateId);
                    }
                    getAllCity(stateId);
                } else {
                    stateId = "";

                }
            }
            break;

            case R.id.spinnerCity:
                if (i > 0) {
                    cityId = cityArrayList.get(i - 1).getId();
                    if (Global_variable.logEnabled) {
                        Log.e("**has_qbc", "" + cityArrayList.get(i - 1).isHas_qbc());
                    }
                    if (cityArrayList.get(i - 1).isHas_qbc()) {
                        linearQBC.setVisibility(View.VISIBLE);
                    } else {
                        linearQBC.setVisibility(View.GONE);
                        et_qbcNumber.setText("");
                    }

                    if (Global_variable.logEnabled) {
                        Log.e("**city ID", cityId);
                    }
                    //getAllCity(stateId);
                    //  getTalukas(district_id);
                } else {
                    cityId = "";
                    linearQBC.setVisibility(View.GONE);
                    et_qbcNumber.setText("");
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getAllCity(String stateId) {
        String url = cityUrl + "=" + stateId;
        RestClientVolley restClientVolley = new RestClientVolley(RegiNextActivity.this, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                populateCity(response);
            }
        });

        restClientVolley.executeRequest();
    }

    private void populateCity(String response) {
        cityArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("objects");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String stateid = object.getString("id");
                    String statename = object.getString("city_name");
                    boolean has_qbc = object.getBoolean("has_qbc");
                    JewelRapItem jewelRapItem = new JewelRapItem();
                    jewelRapItem.setId(stateid);
                    jewelRapItem.setName(statename);
                    jewelRapItem.setHas_qbc(has_qbc);
                    cityArrayList.add(jewelRapItem);
                }

                Collections.sort(cityArrayList, new Comparator<JewelRapItem>() {
                    @Override
                    public int compare(JewelRapItem lhs, JewelRapItem rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (cityArrayList != null && cityArrayList.size() > 0) {
            cityList.clear();

            JewelRapItem model;
            cityList.add("--Select City--");
            for (int i = 0; i < cityArrayList.size(); i++) {
                model = cityArrayList.get(i);
                String district = model.getName();
                cityList.add(district);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegiNextActivity.this, android.R.layout.simple_spinner_item) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position == 0) {
                        ((TextView) v.findViewById(android.R.id.text1)).setText("");
                        ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(position)); //"Hint to be displayed"
                    }
                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {

                    if (position == 0) {
                        return new View(parent.getContext());
                    } else {
                        return super.getDropDownView(position, null, parent);
                    }
                }

            };

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            adapter.addAll(cityList);
            spinnerCity.setAdapter(adapter);
            spinnerCity.setSelection(0);
        }
    }

    class GmsGetId extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

                regId = instanceID.getToken(Global_variable.GOOGLE_PROJECT_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                //regId = gcm.register(Global_variable.GOOGLE_PROJECT_ID);
                deviceId = regId;
            } catch (IOException ex) {
                deviceId = "";
                if (Global_variable.logEnabled) {
                    Log.e("", "Error :" + ex.getMessage());
                }
            }
            return deviceId;
        }

        @Override
        protected void onPostExecute(String msg) {
          /*  Toast.makeText(getApplicationContext(),
                    "Registered with GCM Server." + deviceId, Toast.LENGTH_LONG)
                    .show();*/
            if (Global_variable.logEnabled) {
                Log.e("GCMID", msg);
            }
        }
    }

    public boolean validateCity() {
        boolean isValid = false;
        if (cityId.equals("")) {

            isValid = false;

        } else {
            isValid = true;
        }

        return isValid;
    }

    public boolean validateState() {
        boolean isValid = false;
        if (stateId.equals("")) {

            isValid = false;

        } else {
            isValid = true;
        }

        return isValid;
    }
}
