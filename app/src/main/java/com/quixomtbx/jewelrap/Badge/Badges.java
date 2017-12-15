package com.quixomtbx.jewelrap.Badge;

import android.content.Context;

import com.quixomtbx.jewelrap.Badge.providers.BadgeProvider;
import com.quixomtbx.jewelrap.Badge.providers.BadgeProviderFactory;


public class Badges {

    public static void setBadge(Context context, int count) throws BadgesNotSupportedException {
        if (context == null) {
            throw new BadgesNotSupportedException();
        }

        BadgeProviderFactory badgeFactory = new BadgeProviderFactory(context);
        BadgeProvider badgeProvider = badgeFactory.getBadgeProvider();
        try {
            badgeProvider.setBadge(count);
        } catch (UnsupportedOperationException exception) {
            throw new BadgesNotSupportedException();
        }
    }


    public static void removeBadge(Context context) throws BadgesNotSupportedException {
        Badges.setBadge(context, 0);
    }
}
