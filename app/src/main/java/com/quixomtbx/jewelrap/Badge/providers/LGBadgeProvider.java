
package com.quixomtbx.jewelrap.Badge.providers;

import android.content.Context;
import android.content.Intent;

class LGBadgeProvider extends BadgeProvider {

    public static final String HOME_PACKAGE = "com.lge.launcher2";

    public LGBadgeProvider(Context context) {
        super(context);
    }

    @Override
    public void setBadge(int count) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count_package_name", getPackageName());
        intent.putExtra("badge_count_class_name", getMainActivityClassName());
        intent.putExtra("badge_count", count);

        mContext.sendBroadcast(intent);
    }

    @Override
    public void removeBadge() {
        setBadge(0);
    }
}
