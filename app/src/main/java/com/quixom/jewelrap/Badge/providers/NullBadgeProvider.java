package com.quixom.jewelrap.Badge.providers;

class NullBadgeProvider extends BadgeProvider {

    public NullBadgeProvider() {
        super(null);
    }

    @Override
    public void setBadge(int count) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeBadge() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
