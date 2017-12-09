package com.quixom.jewelrap.Model;

import com.quixom.jewelrap.R;

/**
 * Created by C32 on 9/21/2016.
 */
public enum   ViewPagerModel {
    VIEWSTOCK(R.layout.viewpager_viewstock_item),
    SALES(R.layout.viewpager_sales_item);
    private int viewpager_layout;

     ViewPagerModel(int viewpager_layout) {
        this.viewpager_layout = viewpager_layout;
    }

    public int getViewpager_layout() {
        return viewpager_layout;
    }

    public void setViewpager_layout(int viewpager_layout) {
        this.viewpager_layout = viewpager_layout;
    }
}

