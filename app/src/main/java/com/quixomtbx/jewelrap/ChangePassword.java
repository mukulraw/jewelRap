package com.quixomtbx.jewelrap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixomtbx.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    EditText editText_currentpwd,editText_newpwd,editText_re_newpwd;
    Button button_changepassword;
    String newPassword,oldPassword,rePassword;
    String changepasswordUrl = Global_variable.chagepassword_api;
    String id,name,uniqueid;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    TextView toolbar_textview;
    FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }

    private void initView(){
        sharedPreferences=getSharedPreferences(SessionManager.MyPREFERENCES,MODE_PRIVATE);
        id=sharedPreferences.getString(SessionManager.Id,null);
        name = sharedPreferences.getString(SessionManager.Name, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar_textview=(TextView)findViewById(R.id.toolbar_title);
        toolbar_textview.setText("Change Password");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText_currentpwd=(EditText)findViewById(R.id.et_oldPassword);
        editText_newpwd=(EditText)findViewById(R.id.et_newPassword);
        editText_re_newpwd=(EditText)findViewById(R.id.et_rePassword);
        button_changepassword=(Button)findViewById(R.id.btn_submit);
        button_changepassword.setOnClickListener(this);
        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
    }

    public void validatePasswordAndConfirmPassword(String psw, String psw1) {
        if (Global_variable.logEnabled) {
            Log.e("***password", psw + " " + psw1 + "length " + psw.length() + " " + psw1.length());
        }
        int c = 0;
        if (psw.length() < 6) {

            editText_newpwd.setError("Password should be of minimum 6 char");
            editText_newpwd.requestFocus();
        }
        if (psw1.length() < 6) {

            editText_re_newpwd.setError("Password should be of minimum 6 char");
            editText_re_newpwd.requestFocus();
        }

        if (psw.length() >= 6 && psw1.length() >= 6) {

            if (!psw.equals(psw1)) {

                editText_newpwd.setError("Password and confirm password should be same");
                editText_re_newpwd.setError("Password and confirm password should be same");
                editText_newpwd.requestFocus();
                editText_re_newpwd.requestFocus();
            } else {
                editText_newpwd.setError(null);
                editText_re_newpwd.setError(null);
            }
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                    changePassword();
                break;
        }
    }

    private void changePassword(){
        newPassword = editText_newpwd.getText().toString();
        rePassword = editText_re_newpwd.getText().toString();
        oldPassword = editText_currentpwd.getText().toString();
        checkEmptyStringValidation(editText_currentpwd);
        if (Global_variable.logEnabled) {
            Log.e("oldpwd", "" + oldPassword);
            System.out.print("password" + newPassword + " " + rePassword + "length " + newPassword.length() + " " + rePassword.length());
        }
        validatePasswordAndConfirmPassword(newPassword, rePassword);
        if (editText_currentpwd.getError() == null && editText_newpwd.getError() == null && editText_re_newpwd.getError() == null) {
            RestClientVolley restClientVolley = new RestClientVolley(ChangePassword.this, Request.Method.POST, changepasswordUrl, new VolleyCallBack() {
                @Override
                public void processCompleted(String response) {
                    if (Global_variable.logEnabled) {
                        Log.e("response", response);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");
                        if (status.equals("ok")) {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            NavigationDrawerAdapter.selected_item = 7;
                            MainActivity.id_pos = 7;
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            //firebase analytics for change password
                            Bundle bundle = new Bundle();
                            bundle.putString("user_name", name);
                            bundle.putString("jewelrap_id", uniqueid);
                            firebaseAnalytics.logEvent("change_password", bundle);

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (Global_variable.logEnabled) {
                Log.e("newpwd", newPassword);
                Log.e("id", id);
                Log.e("oldpwd", oldPassword);
            }
            restClientVolley.addParameter("user_id", id);
            restClientVolley.addParameter("old_pass", oldPassword);
            restClientVolley.addParameter("new_pass", newPassword);
            restClientVolley.executeRequest();
        }

    }
}
