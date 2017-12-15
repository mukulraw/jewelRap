package com.quixomtbx.jewelrap.Badge.providers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

class HtcBadgeProvider extends BadgeProvider {

    public static final String HOME_PACKAGE = "com.htc.launcher";

    public HtcBadgeProvider(Context context) {
        super(context);
    }

    @Override
    public void setBadge(int count) {
        Intent intent = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
        intent.putExtra("packagename", getPackageName());
        intent.putExtra("count", count);
        mContext.sendBroadcast(intent);

        Intent setNotificationIntent = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
        ComponentName componentName = new ComponentName(getPackageName(), getMainActivityClassName());
        setNotificationIntent.putExtra("com.htc.launcher.extra.COMPONENT", componentName.flattenToShortString());
        setNotificationIntent.putExtra("com.htc.launcher.extra.COUNT", count);
        mContext.sendBroadcast(setNotificationIntent);
    }

    @Override
    public void removeBadge() {
        setBadge(0);
    }
}
