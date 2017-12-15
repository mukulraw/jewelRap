package com.quixomtbx.jewelrap.Badge.providers;

import android.content.Context;

import com.quixomtbx.jewelrap.Badge.providers.collaborators.HomePackageIdentify;

import java.util.HashMap;
public class BadgeProviderFactory {

    private Context context;
    private HashMap<String, BadgeProvider> providers;

    public BadgeProviderFactory(Context context) {
        this.context = context;
        providers = new HashMap<String, BadgeProvider>();
        providers.put(SamsungBadgeProvider.HOME_PACKAGE, new SamsungBadgeProvider(context));
        providers.put(LGBadgeProvider.HOME_PACKAGE, new LGBadgeProvider(context));
        providers.put(SonyBadgeProvider.HOME_PACKAGE, new SonyBadgeProvider(context));
        providers.put(HtcBadgeProvider.HOME_PACKAGE, new HtcBadgeProvider(context));
    }

    public BadgeProvider getBadgeProvider() {
        String currentPackage = getHomePackage(context);

        if (providers.containsKey(currentPackage)) {
            return providers.get(currentPackage);
        }

        return new NullBadgeProvider();
    }

    private String getHomePackage(Context context) {
        HomePackageIdentify identify = new HomePackageIdentify();
        return identify.getHomePackage(context);
    }
}
