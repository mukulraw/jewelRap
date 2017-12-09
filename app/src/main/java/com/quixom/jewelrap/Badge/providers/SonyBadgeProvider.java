
package com.quixom.jewelrap.Badge.providers;

import android.content.Context;
import android.content.Intent;

class SonyBadgeProvider extends BadgeProvider {

    public static final String HOME_PACKAGE = "com.sonyericsson.home";

    public SonyBadgeProvider(Context context) {
        super(context);
    }

    @Override
    public void setBadge(int count) {
        Intent intent = new Intent();

        intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", getPackageName());
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", getMainActivityClassName());
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", count > 0);
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));

        mContext.sendBroadcast(intent);
    }

    @Override
    public void removeBadge() {
        setBadge(0);
    }
}
