package com.quixomtbx.jewelrap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quixomtbx.jewelrap.Adapter.PagerAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;


public class InquiryFragment extends Fragment {


    public  static String Comefrom="";
    public static  int POS=0;
     ViewPager viewPager;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    String id,userrole;
    Context context;
    public  static int i;
    String status="";
    public static  String comeinquiry="";
    PagerAdapter adapter;
    public InquiryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_inquiry, container, false);
        final TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Current demand"));
        tabLayout.addTab(tabLayout.newTab().setText("Old demand"));
        tabLayout.addTab(tabLayout.newTab().setText("My Enquiry"));

        sharedPreferences=getActivity().getSharedPreferences(SessionManager.MyPREFERENCES,context.MODE_PRIVATE);

        userrole=sharedPreferences.getString(SessionManager.USERROLE,null);


        sessionManager=new SessionManager(getActivity());
        int f=1;
        if(userrole.equals("retailer"))
        {
           tabLayout.removeAllTabs();
           tabLayout.addTab(tabLayout.newTab().setText("My Enquiry"));

        }
        i=  tabLayout.getTabCount();

      

        viewPager = (ViewPager)view.findViewById(R.id.pager);

        adapter = new PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switchFragment(position);
            }
        });
   
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       // tabLayout.setupWithViewPager(viewPager);
        Bundle b = getArguments();


        int position = 0;
        if (b  != null && b.containsKey("pos")){
            position=b.getInt("pos");

            switchFragment(position);

        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(Global_variable.LOG_TAG,"**inquiry tab  pos :" + tab.getPosition());
                      switchFragment(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
    void switchFragment(int target){

        viewPager.setCurrentItem(target);
        POS=target;
        if(target==0)
        {
            Comefrom="CURRENT";
        }
        else if(target==1)
        {
            Comefrom="IN";
        }
        else if(target==2)
        {
            Comefrom="MYIN";
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

}
