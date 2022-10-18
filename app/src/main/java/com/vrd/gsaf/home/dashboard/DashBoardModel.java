package com.vrd.gsaf.home.dashboard;

import android.graphics.drawable.Drawable;

public class DashBoardModel {
    private final String name;
    private final Drawable icon;

    public DashBoardModel(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }
}
