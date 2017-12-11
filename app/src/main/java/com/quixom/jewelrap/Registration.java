package com.quixom.jewelrap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class Registration extends AppCompatActivity {

    Button btn;
    EditText et_firstnm, et_lastnm, et_conf_pwd, et_pwd, et_email, et_phone, et_address,et_firmname,et_landline;
    String fnm, lnm, pwd, pwdconf, phone, address, email,firmname;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn = (Button) findViewById(R.id.buttonnextreg);
        et_firstnm = (EditText) findViewById(R.id.edit_text_firstname);
        et_firstnm.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        et_lastnm = (EditText) findViewById(R.id.edit_text_lastname);
        et_lastnm.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        et_pwd = (EditText) findViewById(R.id.edit_text_password);
        et_conf_pwd = (EditText) findViewById(R.id.edit_text_password_conform);
        et_email = (EditText) findViewById(R.id.edit_text_email_id);
        et_phone = (EditText) findViewById(R.id.edit_text_phone);
        et_address = (EditText) findViewById(R.id.edit_text_address);
        et_address.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        et_firmname=(EditText)findViewById(R.id.edit_text_firmName);
        et_firmname.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        et_landline=(EditText)findViewById(R.id.edit_text_landline);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        colorFilter(getApplicationContext(),R.drawable.ic_company_24dp);
        colorFilter(getApplicationContext(),R.drawable.ic_account_black_24dp);
        colorFilter(getApplicationContext(),R.drawable.ic_key_black_24dp);
        colorFilter(getApplicationContext(),R.drawable.ic_key_black_24dp);
        colorFilter(getApplicationContext(),R.drawable.ic_mail_ru_black_24dp);
        colorFilter(getApplicationContext(),R.drawable.ic_phone_gray);
        colorFilter(getApplicationContext(),R.drawable.ic_landline);
        colorFilter(getApplicationContext(),R.drawable.ic_map_marker_black_24dp);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fnm = et_firstnm.getText().toString();
                lnm = et_lastnm.getText().toString();

                pwd = et_pwd.getText().toString();
                pwdconf = et_conf_pwd.getText().toString();
                email = et_email.getText().toString();
                phone = et_phone.getText().toString();
                address = et_address.getText().toString();
                firmname=et_firmname.getText().toString();
                checkEmptyStringValidation(et_firstnm);
                checkEmptyStringValidation(et_firmname);
                checkEmptyStringValidation(et_lastnm);

                checkEmptyStringValidationPhone(et_phone);
                checkEmptyStringValidation(et_address);
                if (!isValidEmail(email)) {
                    et_email.setError("Invalid email");
                    et_email.requestFocus();
                }
                validatePasswordAndConfirmPassword(pwd, pwdconf);

                if (et_firstnm.getError() == null && et_lastnm.getError() == null && et_pwd.getError() == null &&
                        et_conf_pwd.getError() == null && et_email.getError() == null && et_phone.getError() == null
                        && et_address.getError() == null && et_firmname.getError()==null) {
                    Intent in = new Intent(getApplicationContext(), RegiNextActivity.class);
                    in.putExtra("firstnm", fnm);
                    in.putExtra("lastnm", lnm);
                    in.putExtra("pwd", pwd);
                    in.putExtra("conf_pwd", pwdconf);
                    in.putExtra("email", email);
                    in.putExtra("phone", phone);
                    in.putExtra("address", address);
                    in.putExtra("firmname",firmname);



                    if(et_landline.getText().length()>0){
                        checkEmptyTextValidatiotoLandlinenumber(et_landline);
                        if(et_landline.getError()==null) {
                            in.putExtra("landline", et_landline.getText().toString());
                            startActivity(in);
                        }
                    }else {
                        in.putExtra("landline", et_landline.getText().toString());
                        startActivity(in);
                    }

                    overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                }
            }
        });
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


    void checkEmptyStringValidation(final EditText et) {
        String str = et.getText().toString();
        if (str.length() == 0) {
            et.setError("Required");
            et.requestFocus();
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

    void checkEmptyStringValidationPhone(final EditText et) {
        String str = et.getText().toString();
        if (str.length() !=10) {
            et.setError("Invalid contact number");
            et.requestFocus();
        } else {
            et.setError(null);

        }
       /* et.addTextChangedListener(new TextWatcher() {
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
        });*/
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
    public void validatePasswordAndConfirmPassword(String psw, String psw1) {
        int c = 0;
        if (psw.length() < 6) {

            et_pwd.setError("Password should be of minimum 6 char");
            et_pwd.requestFocus();
        }
        if (psw1.length() < 6) {

            et_conf_pwd.setError("Password should be of minimum 6 char");
            et_conf_pwd.requestFocus();
        }

        if (psw.length() >= 6 && psw1.length() >= 6) {

            if (!psw.equals(psw1)) {

                et_pwd.setError("Password and confirm password should be same");
                et_conf_pwd.setError("Password and confirm password should be same");
                et_pwd.requestFocus();
                et_conf_pwd.requestFocus();
            } else {
                et_pwd.setError(null);
                et_conf_pwd.setError(null);
            }
        }
    }

    public boolean isValidEmail(String eml) {
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]{2,3}";
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
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
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);


        }


        return super.onOptionsItemSelected(item);
    }
}
