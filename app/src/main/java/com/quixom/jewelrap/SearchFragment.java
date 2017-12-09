package com.quixom.jewelrap;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quixom.jewelrap.Adapter.SearchPagerAdapter;
import com.quixom.jewelrap.Adapter.SolitaireShapeAdapter;
import com.quixom.jewelrap.Adapter.Support.SelectableAdapterShape;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Model.JewelRapItem;

import java.util.ArrayList;


public class SearchFragment extends Fragment {


    ViewPager searchViewPager;
    SearchPagerAdapter searchPagerAdapter;
    Bundle bundle;

    /*SolitaireShapeAdapter.ViewHolder.ClickListener clicklistener;
    static ArrayList<JewelRapItem> listShape = new ArrayList<>();
    int categoryid;
    float textsize;*/

    public static String Comefrom = "";
    public static int POS = 0;
    int[] tabIcons = {
            R.drawable.ic_about_solitarier,
            R.drawable.ic_about_loose
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final TabLayout searchTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        searchTabLayout.addTab(searchTabLayout.newTab().setText("Solitaire"));
        searchTabLayout.addTab(searchTabLayout.newTab().setText("Loose Diamonds"));

        searchViewPager = (ViewPager) view.findViewById(R.id.search_viewpager);
        searchPagerAdapter = new SearchPagerAdapter(getChildFragmentManager(), searchTabLayout.getTabCount(), bundle);
        searchViewPager.setAdapter(searchPagerAdapter);
        searchViewPager.setOffscreenPageLimit(2);

        searchViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchTabLayout) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e(Global_variable.LOG_TAG, "**search page +selection  pos :" + position);
                switchFragment(position);

            }
        });
        searchTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        int position = 0;

        if (bundle != null && bundle.containsKey("searchpos")) {
            position = bundle.getInt("searchpos");
            Log.e(Global_variable.LOG_TAG, "**search pos :" + position);
            switchFragment(position);
        }


        searchTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(Global_variable.LOG_TAG, "**search tab pos :" + tab.getPosition());
                switchFragment(tab.getPosition());
//                searchViewPager.setCurrentItem(tab.getPosition());

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

    void switchFragment(int target) {

        searchViewPager.setCurrentItem(target);
        POS = target;
        if (target == 0) {
            Comefrom = "SOLI";
        } else if (target == 1) {
            Comefrom = "LOOSE";
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
