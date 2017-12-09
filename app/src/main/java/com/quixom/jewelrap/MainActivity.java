package com.quixom.jewelrap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blunderer.materialdesignlibrary.interfaces.NavigationDrawer;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixom.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixom.jewelrap.Badge.Badges;
import com.quixom.jewelrap.Badge.BadgesNotSupportedException;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    public static int id_pos = 0;
    static String id;
    Toolbar toolbar;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    String name, firmname, firstname, lastname;
    String uniqueId;
    String token;
    String userrole;
    TextView tv_name;
    TextView tv_uniqueid;
    static TextView tv_username;
    String status = "";
    Context context;
    LayoutInflater inflater;
    int pos;
    String TAG = "JEWELRAP";
    TextView toolbartitle;
    DrawerLayout drawerLayout;
    String url = Global_variable.logout_api;
    private FragmentDrawer drawerFragment;
    String gcmID;
    static CircularImageView iv_profile_image;
    String profileImage;
    FirebaseAnalytics firebaseAnalytics;

    RelativeLayout relativeLayoutdrawer;
    TextView navigationdrawer;
    ImageView navigationdrawericon;
    LinearLayout navigationdrawerlinearlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        userrole = sharedPreferences.getString(SessionManager.USERROLE, null);
        uniqueId = sharedPreferences.getString(SessionManager.UNIQUEID, "");
        gcmID = sharedPreferences.getString(SessionManager.GCMID, "");
        firmname = sharedPreferences.getString(SessionManager.FIRM_NAME, null);
        firstname = sharedPreferences.getString(SessionManager.FIRST_NAME, null);
        lastname = sharedPreferences.getString(SessionManager.LAST_NAME, null);
        profileImage = sharedPreferences.getString(SessionManager.PROFILE_IMAGE, null);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (Global_variable.logEnabled) {
            Log.e("uniqueid", uniqueId);
        }

        if (uniqueId.equals("null") || uniqueId == null || uniqueId.equals("")) {
            uniqueId = "";
        } else {
            uniqueId = "" + uniqueId;
        }
        if (Global_variable.logEnabled) {
            Log.e("name", name);
            Log.e("token", token);
            Log.e("id", id);
            Log.e("userrole", userrole);
        }
        sessionManager = new SessionManager(getApplicationContext());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        getSupportActionBar().setTitle("");


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);
        drawerFragment.setDrawerListener(this);
        final Intent in = getIntent();

        if (in.hasExtra("fragment_id")) {
            id_pos = in.getIntExtra("fragment_id", 0);
        }
        displayView(id_pos);
        if (Global_variable.logEnabled) {
            Log.e("**first anem", "" + firstname);
            Log.e("**last name", "" + lastname);
        }
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(firmname);
        tv_uniqueid = (TextView) findViewById(R.id.tv_uniqueId);
        tv_uniqueid.setText(uniqueId);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(firstname + " " + lastname);
        iv_profile_image = (CircularImageView) findViewById(R.id.profile_image_drawer);
        relativeLayoutdrawer = (RelativeLayout) findViewById(R.id.nav_header_container);
        navigationdrawer = (TextView) findViewById(R.id.title);
        navigationdrawericon = (ImageView) findViewById(R.id.titleicon);
        navigationdrawerlinearlayout = (LinearLayout) findViewById(R.id.drawerlayout);
        Glide.with(getApplicationContext())
                .load(profileImage)
                .asBitmap()
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(iv_profile_image);
        relativeLayoutdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationDrawerAdapter.selected_item=8;
                drawerFragment.adapterNotify();
                displayView(8);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    public static void setTextUsername(String username) {
        tv_username.setText(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (pos == 0) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this, R.style.DesignDemo);
            dlgAlert.setMessage("Are you sure you want to exit?");
            dlgAlert.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mainActivity = new Intent(Intent.ACTION_MAIN);
                            mainActivity.addCategory(Intent.CATEGORY_HOME);
                            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainActivity);
                            overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                        }
                    });
            dlgAlert.setNegativeButton("No", null);
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        } else {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            in.putExtra("fragment_id", 0);
            startActivity(in);
            overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
            NavigationDrawerAdapter.selected_item = 0;
        }
    }

    @Override
    public void onDrawerItemSelected(View view, final int position) {

        drawerLayout.closeDrawer(Gravity.LEFT);
        displayView(position);

    }

    public void displayView(int position) {
        Fragment fragment = null;
        Intent i;
        int delaytime = 250;
        int positionToMove = 0, searchPos = 0;
        Bundle b = null;

        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                pos = 0;

                break;
            case 1:
                Intent in = getIntent();
                delaytime = 300;
                if (in.hasExtra("pos")) {
                    positionToMove = in.getIntExtra("pos", 0);
                    b = new Bundle();
                    b.putInt("pos", positionToMove);
                }
                fragment = new InquiryFragment();
                fragment.setArguments(b);
                InquiryFragment.Comefrom = "CURRENT";
                InquiryFragment.comeinquiry = "";
                if (userrole.equals("retailer")) {

                    InquiryFragment.Comefrom = "MYIN";
                }

                title = getString(R.string.title_inquiry);
                pos = 1;
                in.removeExtra("pos");
                break;
            case 2:
                //   Glide.get(MainActivity.this).clearMemory();
                Intent intent = getIntent();
                delaytime = 300;

                if (intent.hasExtra("searchpos")) {
                    searchPos = intent.getIntExtra("searchpos", 0);
                    b = new Bundle();
                    b.putInt("searchpos", searchPos);
                }
                if (intent.hasExtra("universalgems")) {
                    b = new Bundle();
                    b.putBoolean("universalgems", intent.getBooleanExtra("universalgems", false));
                    b.putString("shortcut_unique_id", intent.getStringExtra("shortcut_unique_id"));

                }
                fragment = new SearchFragment();
                fragment.setArguments(b);
                SearchFragment.Comefrom = "CURRENT";
                title = getString(R.string.title_search);
                //ShapeAdapter.flag="false";

                pos = 2;
                intent.removeExtra("searchpos");
                intent.removeExtra("universalgems");
                break;
            case 3:
                fragment = new InvetoryFragment();
                title = getString(R.string.title_solitaire_sale);
                pos = 3;
                break;
            case 4:
                fragment = new NewsFragment();
                title = getString(R.string.title_news);
                pos = 4;
                break;
            case 5:
                fragment = new FeedBackFragment();
                title = getString(R.string.title_feedbak);
                pos = 5;
                break;
            case 6:
                fragment = new AboutUsFragment();
                title = getString(R.string.title_aboutus);
                pos = 6;
                break;
            case 7:
                fragment = new ContactUsFragment();
                title = getString(R.string.title_Contactus);
                pos = 7;
                break;
            case 8:
                fragment = new ProfileFragment();
                title = getString(R.string.title_profile);
                pos = 8;
                break;
            case 9:

                title = getString(R.string.title_logout);

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this, R.style.DesignDemo);
                dlgAlert.setMessage("Are you sure you want to logout?");
                dlgAlert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                logoutUser();
                                overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                                pos = 9;
                            }
                        });
                dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
                        NavigationDrawerAdapter.selected_item = 0;
//                        id_pos = 0;
                    }
                });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
                break;
            default:
                break;
        }

        Handler handler = new Handler();

        if (fragment != null) {
            final Fragment finalFragment = fragment;
            final String finalTitle = title;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, finalFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    getSupportActionBar().setTitle("");
                    toolbartitle.setText(finalTitle);
                }
            };
            handler.postDelayed(runnable, delaytime);
        }
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void logoutUser() {

        RestClientVolley restClientVolley = new RestClientVolley(MainActivity.this, Request.Method.POST, url, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                JSONObject jsonObject = null;
                String status, message;
                try {
                    jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equals("ok")) {
                        sessionManager.logoutUser();
                        clearBadgeCount();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        restClientVolley.addParameter("id", id);
        restClientVolley.addParameter("GCM_Token", "gcm");
        if (Global_variable.logEnabled) {
            Log.e("**logout device id", gcmID);
        }
        restClientVolley.executeRequest();

        Bundle bundle = new Bundle();
        bundle.putString("user_name", name);
        bundle.putString("jewelrap_id", uniqueId);
        firebaseAnalytics.logEvent("logout_user", bundle);
    }

    public void clearBadgeCount() {
        RestClientVolley restClientVolley = new RestClientVolley(getApplicationContext(), Request.Method.POST, Global_variable.badge_api, new VolleyCallBack() {
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
            Badges.removeBadge(getApplicationContext());

        } catch (BadgesNotSupportedException e) {
            e.printStackTrace();
        }

    }
}
