package com.quixom.jewelrap.Badge.providers;

import android.content.Context;


public abstract class BadgeProvider {

    protected Context mContext;

    public BadgeProvider(Context context) {
        mContext = context;
    }

    public abstract void setBadge(int count) throws UnsupportedOperationException;
    public abstract void removeBadge() throws UnsupportedOperationException;

    protected String getPackageName() {
        return mContext.getPackageName();
    }

    protected String getMainActivityClassName() {
        return mContext.getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent().getClassName();
    }
}
