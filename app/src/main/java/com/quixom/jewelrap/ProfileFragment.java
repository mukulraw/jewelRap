package com.quixom.jewelrap;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.Utils.RestClientMultipartRequest;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.Utils;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 112;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = Global_variable.google_key;
    Button btn_update;
    EditText et_pwd, et_conf_pwd, et_email, et_phone, et_address, et_pincode, et_landline;
    String password, email, phone, address, city, state, country, pincode, landline;

    SharedPreferences sharedPreferences;
    String name, token, id, userrole,uniqueid;
    Context mcontext;
    String url = Global_variable.profile_api;
    SessionManager sessionManager;
    Spinner spinnerState, spinnerCity;
    String stateUrl = Global_variable.state_api;
    String cityUrl = Global_variable.city_api;
    ArrayList<JewelRapItem> stateArrayList, cityArrayList;
    ArrayList<String> stateList, cityList;
    String stateId = "", cityId = "", sId = "";
    String stateName = "", cityName = "";
    TextView changepasswordTextview;
    String changepasswordUrl = Global_variable.chagepassword_api;
    Dialog dialog;
    EditText et_newpassword, et_repassword, et_oldpassword;
    String rePassword, newPassword, oldPassword;
    ImageView iv_profile_edit;
    CircularImageView iv_profile_view;
    public final int REQUEST_CODE_FROM_GALLERY = 1;
    String imagePath;
    Uri imageUri;
    File file;
    String changeProfileUrl = Global_variable.change_profile_api;
    Uri mCropImagedUri;
    String profileUrl;
    String[] filePathColumn = {MediaStore.Images.Media.DATA};
    FirebaseAnalytics firebaseAnalytics;

    LinearLayout linearLayoutprofile;

    public ProfileFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getViews(view);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mcontext = getActivity();
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, mcontext.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        userrole = sharedPreferences.getString(SessionManager.USERROLE, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        sessionManager = new SessionManager(getActivity());

        getProfileDetail();

        btn_update.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = et_pwd.getText().toString();
                landline = et_landline.getText().toString();
                checkEmptyStringValidationPhone(et_phone);
                checkEmptyStringValidation(et_address);
                checkEmptyStringValidation(et_pincode);

                if (!isValidEmail(et_email.getText().toString())) {
                    et_email.setError("Invalid email");
                }

                if (et_phone.getError() == null
                        && et_address.getError() == null
                        && et_email.getError() == null
                        && et_pincode.getError() == null) {
                    if (!stateName.equals("") && cityName.equals("")) {
                        Toast.makeText(getActivity(), "Select city", Toast.LENGTH_LONG).show();
                    } else {
                        if (landline.length() > 0) {
                            checkEmptyTextValidatiotoLandlinenumber(et_landline);
                            if (et_landline.getError() == null) {
                                updateProfileDetails();
                            }
                        } else {
                            landline = "";
                            updateProfileDetails();
                        }
                    }

                }
            }
        });

        colorFilter(getContext(),R.drawable.ic_mail_ru_black_24dp);
        colorFilter(getContext(),R.drawable.ic_phone_black_24dp);
        colorFilter(getContext(),R.drawable.ic_landline);
        colorFilter(getContext(),R.drawable.ic_map_marker_black_24dp);
        colorFilter(getContext(),R.drawable.ic_crosshairs_gps_black_24dp);
        colorFilter(getContext(),R.drawable.ic_map_marker_black_24dp);
        return view;
    }



    private void getAllState()
    {
        RestClientVolley restClientVolley = new RestClientVolley(mcontext, Request.Method.GET, stateUrl, new VolleyCallBack() {
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
                    JewelRapItem jewelRapItem = new JewelRapItem();
                    jewelRapItem.setId(stateid);
                    jewelRapItem.setName(statename);
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
            stateList.add(state);
            for (int i = 0; i < stateArrayList.size(); i++) {
                model = stateArrayList.get(i);
                String district = model.getName();
                stateList.add(district);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position == 0) {
                        ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(position));
                        ((TextView) v.findViewById(android.R.id.text1)).setHint("");
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

            getAllCity(sId);
        }
        linearLayoutprofile.setVisibility(View.VISIBLE);

    }


    private void updateProfileDetails() {
        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.POST, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                profileUpdated(response);
            }
        });
        restClientVolley.addParameter("user", id);
        restClientVolley.addParameter("address", et_address.getText().toString());
        restClientVolley.addParameter("city", cityName);
        restClientVolley.addParameter("state", stateName);
        if (Global_variable.logEnabled) {
            Log.e("**city", cityId);
            Log.e("**state", stateId);
        }
        restClientVolley.addParameter("pin", et_pincode.getText().toString());
        restClientVolley.addParameter("email", et_email.getText().toString());
        restClientVolley.addParameter("phone_num", et_phone.getText().toString());
        if (Global_variable.logEnabled) {
            Log.e("landline", "value " + landline);
        }
        restClientVolley.addParameter("landline", landline);
        restClientVolley.executeRequest();
    }

    private void profileUpdated(String response) {


        if (Global_variable.logEnabled) {
            Log.e("Profile Update response", response);
        }
        JSONObject jsonObj = null;
        String status, message;
        try {
            jsonObj = new JSONObject(response);
            status = jsonObj.getString("status");

            if (status.equals("ok")) {
                message = jsonObj.optString("message");
                Intent in = new Intent(getActivity(), MainActivity.class);
                 startActivity(in);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            } else {

                if (jsonObj.has("multiple_login")) {
                    if (Global_variable.logEnabled) {
                        Log.e("Message : ", jsonObj.getString("messages"));
                    }
                    String msg = jsonObj.optString("messages");


                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.DesignDemo);
                    dlgAlert.setMessage(msg);
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sessionManager.logoutUser();
                                    getActivity().overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                                }
                            });
                    dlgAlert.setCancelable(false);
                    dlgAlert.create().show();

                } else {
                    message = jsonObj.optString("message");
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProfileDetail() {

        String urlToSend = Global_variable.get_profile_api + id + File.separator;
        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, urlToSend, new VolleyCallBack() {

            @Override
            public void processCompleted(String response) {
                setProfileDetail(response);
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllState();

                    }
                },3000);*/

            }
        });
        restClientVolley.executeRequest();
    }

    private void setProfileDetail(String response) {
        if (Global_variable.logEnabled) {
            Log.e("profile get response", response);
        }
        JSONObject responseObject = null;
        try {
            responseObject = new JSONObject(response);
            if (responseObject.has("multiple_login")) {
                if (Global_variable.logEnabled) {
                    Log.e("Message : ", responseObject.getString("messages"));
                }
                String msg = responseObject.optString("messages");


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.DesignDemo);
                dlgAlert.setMessage(msg);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.logoutUser();
                                getActivity().overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();

            } else {
                email = responseObject.getString("email");
                address = responseObject.getJSONObject("user_profile").getString("address");
                phone = responseObject.getJSONObject("user_profile").getString("phone_num");
                city = responseObject.getJSONObject("user_profile").getString("city");
                state = responseObject.getJSONObject("user_profile").getString("state");
                sId = responseObject.getJSONObject("user_profile").getString("state_id");
                if (Global_variable.logEnabled) {
                    Log.e("city ", city + " state " + state + " id " + sId);
                }
                country = responseObject.getJSONObject("user_profile").getString("country");
                pincode = responseObject.getJSONObject("user_profile").getString("pin");
                landline = responseObject.getJSONObject("user_profile").getString("landline");
                if (Global_variable.logEnabled) {
                    Log.e("landline frm setprofile", "" + landline);
                }
                et_email.setText(email);
                et_address.setText(address);
                et_phone.setText(phone);
            /*auto_city.setText(city);*/
            /*et_state.setText(state);
            et_country.setText(country);*/
                String url = Global_variable.base_url + responseObject.getJSONObject("user_profile").getString("profile_image");
                Log.e("** profile image url", " " + url);
                Glide.with(mcontext)
                        .load(url)
                        .asBitmap()
                        .error(R.drawable.ic_launcher)
                        .placeholder(R.drawable.ic_launcher)
                        .into(iv_profile_view);
                et_pincode.setText(pincode);

                if (!landline.equals("null")) {
                    et_landline.setText(landline);
                }
                getAllState();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void colorFilter(Context context, int resource)
    {
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

    private void getViews(View view) {
        btn_update = (Button) view.findViewById(R.id.btnupdate);
        et_pwd = (EditText) view.findViewById(R.id.edit_text_password);
        et_email = (EditText) view.findViewById(R.id.edit_text_email_id);
        et_phone = (EditText) view.findViewById(R.id.edit_text_phone);
        et_address = (EditText) view.findViewById(R.id.edit_text_address);
        et_address.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et_landline = (EditText) view.findViewById(R.id.edit_text_landline);
        et_pincode = (EditText) view.findViewById(R.id.edit_text_pincode);
        spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        spinnerState = (Spinner) view.findViewById(R.id.spinnerState);

        linearLayoutprofile=(LinearLayout)view.findViewById(R.id.linearlayoutprofile);

        spinnerState.setOnItemSelectedListener(this);
        spinnerCity.setOnItemSelectedListener(this);
        stateArrayList = new ArrayList<JewelRapItem>();
        cityArrayList = new ArrayList<JewelRapItem>();
        stateList = new ArrayList<String>();
        cityList = new ArrayList<String>();
        changepasswordTextview = (TextView) view.findViewById(R.id.changepasswordtextview);
        changepasswordTextview.setOnClickListener(this);
        iv_profile_edit = (ImageView) view.findViewById(R.id.profileeditimage);
        iv_profile_view = (CircularImageView) view.findViewById(R.id.profileview);
        iv_profile_view.setOnClickListener(this);


    }

    void checkEmptyStringValidation(final EditText et) {
        String str = et.getText().toString();
        if (str.length() == 0) {
            et.setError("Required");
        } else {
            et.setError(null);

        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                et.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void checkEmptyTextValidatiotoLandlinenumber(EditText et) {

        String st = et.getText().toString();
        if (st.length() == 11) {
            et.requestFocus();
            et.setError(null);
        } else {
            et.setError("Invalid number");
            et.requestFocus();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        switch (spinner.getId()) {
            case R.id.spinnerState: {
                if (i > 0) {
                    stateId = stateArrayList.get(i - 1).getId();
                    stateName = stateArrayList.get(i - 1).getName();
                    if (Global_variable.logEnabled) {
                        Log.e("**State ID", stateId);
                        Log.e("**State Name", stateName);
                    }
                    getAllCity(stateId);
                } else {
                    stateId = "";
                    stateName = "";
                }
            }
            break;

            case R.id.spinnerCity:
                if (i > 0) {
                    cityId = cityArrayList.get(i - 1).getId();
                    cityName = cityArrayList.get(i - 1).getName();
                    if (Global_variable.logEnabled) {
                        Log.e("**city ID", cityId);
                        Log.e("**city ID", cityName);
                    }
                    //getAllCity(stateId);
                    //  getTalukas(district_id);
                } else {
                    cityId = "";
                    cityName = "";
                }
                break;
        }
    }


    private void getAllCity(String stateId) {
        String url = cityUrl + "=" + stateId;
        if (!stateId.equals(sId)) {
            city = "--Select City--";
        }
        RestClientVolley restClientVolley = new RestClientVolley(mcontext, Request.Method.GET, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                populateCity(response);
            }
        });
        restClientVolley.progressDialogVisibility(true
        );
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
                    JewelRapItem jewelRapItem = new JewelRapItem();
                    jewelRapItem.setId(stateid);
                    jewelRapItem.setName(statename);
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
            cityList.add(city);
            for (int i = 0; i < cityArrayList.size(); i++) {
                model = cityArrayList.get(i);
                String district = model.getName();
                cityList.add(district);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position == 0) {
                        ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(position));
                        ((TextView) v.findViewById(android.R.id.text1)).setHint(""); //"Hint to be displayed"
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    void checkEmptyStringValidationPhone(final EditText et) {
        String str = et.getText().toString();
        if (str.length() != 10) {
            et.setError("Invalid contact number");
            et.requestFocus();
        } else {
            et.setError(null);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profileview:
                if (Utils.isMarshMallowOrLater()) {
                    int permission = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Permission to access the STORAGE is required for this app to set profile picture.")
                                    .setTitle("Permission required");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    makeRequest();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            makeRequest();
                        }
                    } else {
                        Log.d("jewelrap", "PERMISSION ALREAYD GRANTED");
                        openGallery();
                    }
                } else {
                    openGallery();
                }

                /*intent.setType("image/png");
                startActivityForResult(intent, REQUEST_CODE_FROM_GALLERY);*/
                break;
            case R.id.changepasswordtextview:
                Intent intent =new Intent(getContext(),ChangePassword.class);
                startActivity(intent);
              //  openDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FROM_GALLERY:
                    imageUri = data.getData();
                    //Log.e("  image uri ", " " + imageUri);
                    if (imageUri == null) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        Uri tempUri = getImageUri(getContext(), imageBitmap);
                        Log.e("JEWELRAP", " " + imageBitmap.getHeight());
                        Log.d("JEWELRAP", "width : " + imageBitmap.getWidth());
                        profileUrl = getFileNameByUri(getContext(), tempUri);
                        //  Log.e(" jewelrap image temp uri ", " " +profileUrl);
                    } else {
                        Log.e(" jewelrap image ", " " + getFileNameByUri(getContext(), imageUri));
                        profileUrl = getFileNameByUri(getContext(), imageUri);
                    }

                    Glide.with(mcontext)
                            .load(profileUrl)
                            .asBitmap()
                            .error(R.drawable.ic_launcher)
                            .placeholder(R.drawable.ic_launcher)
                            .into(iv_profile_view);
                    // Log.e("**image path ", "get " + profileUrl);
                    //iv_profile_view.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    file = new File(profileUrl);
                    // Log.e("**file", " " + file);
                    // Log.e("**id", " " + id);


                    //  Log.e("** change profile ", " " + changeProfileUrl);
                    RestClientMultipartRequest restClientMultipartRequest = new RestClientMultipartRequest(getContext(), Request.Method.POST, changeProfileUrl, file, new VolleyCallBack() {
                        @Override
                        public void processCompleted(String response) {
                            Log.e(" *** response ", " " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")) {
                                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                    String url = Global_variable.base_url + object.getString("profile_image");
                                    Log.e("respo url", "" + url);

                                    sessionManager.getProfileImage(url);
                                    Glide.with(mcontext)
                                            .load(url)
                                            .asBitmap()
                                            .error(R.drawable.ic_launcher)
                                            .placeholder(R.drawable.ic_launcher)
                                            .into(MainActivity.iv_profile_image);
                                    /*Intent intent=new Intent(mcontext,MainActivity.class);
                                    intent.putExtra("pos", 0);
                                    startActivity(intent);
                                    MainActivity.id_pos = 0;
                                    NavigationDrawerAdapter.selected_item = 0;*/

                                    //firebase analytics for change profile
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_name", name);
                                    bundle.putString("jewelrap_id", uniqueid);
                                    firebaseAnalytics.logEvent("change_profile", bundle);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    restClientMultipartRequest.addHeader("Authorization", "ApiKey " + name + ":" + token);
                    restClientMultipartRequest.addParameter("user_id", id);
                    restClientMultipartRequest.executeRequest();
                    //setImage(data);
                    //imageViewaddImage.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getFileNameByUri(Context context, Uri uri) {
        String fileName = "unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.toString();
            }
        } else if (uri.getScheme().compareTo("file") == 0) {
            fileName = filePathUri.getPath();
        } else {
            fileName = filePathUri.toString();
        }
        return fileName;
    }

    private File createNewFile(String prefix) {
        Log.d("JEWELRAP", "prefix : " + prefix);
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }

//        String dataDirectory = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + mcontext.getPackageName() + File.separator + "files" + File.separator + "Download" + File.separator;
//        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/mypics/");
        File newDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + mcontext.getPackageName() + File.separator);

        if (!newDirectory.exists()) {
            if (newDirectory.mkdirs()) {
                //Log.e(mcontext.getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }


        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));

        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Log.e("** crop file ", "file path : " + file);
        return file;
    }

    protected void makeRequest() {
        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("JEWELRAP", "request code----- : " + requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Please grant permission to set profile pic", Toast.LENGTH_LONG).show();
                } else {
                    openGallery();
                }
                return;
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        // indicate aspect of desired crop
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        // indicate output X and Y
                /*intent.putExtra("outputX", 512);
                intent.putExtra("outputY", 512);*/
        try {
            intent.putExtra("return-data", true);
            File f = createNewFile("JEWELRAP_");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            mCropImagedUri = Uri.fromFile(f);
            // Log.e("** image url", " " + mCropImagedUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
            startActivityForResult(intent, REQUEST_CODE_FROM_GALLERY);

        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getContext(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT).show();

        }
    }
}
