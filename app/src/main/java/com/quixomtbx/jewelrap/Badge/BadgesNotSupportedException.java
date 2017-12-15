package com.quixomtbx.jewelrap.Badge;

public class BadgesNotSupportedException extends Exception {

    public BadgesNotSupportedException() {
        super("Current home launcher is not supported by Badges library");
    }
    public BadgesNotSupportedException(String homePackage) {
        super(String.format("The home launcher with package '%s' is not supported by Badges library", homePackage));
    }
}
