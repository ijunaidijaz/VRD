package com.vrd.gsaf.home.whereBy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;

public class CustomWebChromeClient extends WebChromeClient {
    private final Activity activity;

    public CustomWebChromeClient(Activity parentActivity) {
        super();
        activity = parentActivity;
    }

    @Override
    public void onPermissionRequest(final PermissionRequest request) {
        activity.runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                request.grant(request.getResources());
            }
        });
    }
}