package com.quixomtbx.jewelrap.Utils;

import android.app.Activity;
import android.util.DisplayMetrics;

import android.widget.TextView;

public class Display {

    int width, height;

    public Display() {

    }

    public Display(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }

    public int iconSizeHeight() {
        return width;
    }

    public int iconSizeWidth() {
        return height;
    }

    public void changeColor(TextView textView, int color) {
        textView.setTextColor(color);
    }
}
