package com.quixomtbx.jewelrap.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.quixomtbx.jewelrap.Inquiries;
import com.quixomtbx.jewelrap.LooseDiamondSearchFragment;
import com.quixomtbx.jewelrap.SolitaireSearchFragment;


public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    Inquiries tab1;
    Bundle bundle;

    public SearchPagerAdapter(FragmentManager fm, int NumOfTabs, Bundle bundle) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        /*if (InquiryFragment.i == 1) {
            MyInquiries tab2 = new MyInquiries();
            return tab2;
        } else if (InquiryFragment.i == 3) {*/

        switch (position) {
            case 0:
                SolitaireSearchFragment tab1 = new SolitaireSearchFragment();
                if (bundle != null && bundle.containsKey("universalgems")) {
                    tab1.setArguments(bundle);
                }
                return tab1;
            case 1:
                LooseDiamondSearchFragment tab2 = new LooseDiamondSearchFragment();
                return tab2;
            default:
                return null;
        }


        //  }
        // return null;
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
