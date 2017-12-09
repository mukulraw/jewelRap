package com.quixom.jewelrap.Adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.quixom.jewelrap.CurrentDemand;
import com.quixom.jewelrap.Inquiries;
import com.quixom.jewelrap.InquiryFragment;
import com.quixom.jewelrap.MyInquiries;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    Inquiries tab1;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (InquiryFragment.i == 1) {
            MyInquiries tab2 = new MyInquiries();
            return tab2;
        } else if (InquiryFragment.i == 3) {
           
            switch (position) {
                case 0:
                    CurrentDemand tab1 = new CurrentDemand();

                    return tab1;
                case 1:
                    Inquiries tab2 = new Inquiries();
                    return tab2;
                case 2:
                    MyInquiries tab3 = new MyInquiries();
                    return tab3;
                default:
                    return null;
            }


        }
        return null;
    }



    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
