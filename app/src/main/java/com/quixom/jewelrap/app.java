package com.quixom.jewelrap;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by c31 on 6/30/2016.
 */
public class app extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
