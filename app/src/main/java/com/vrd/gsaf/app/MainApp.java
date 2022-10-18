package com.vrd.gsaf.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.fairDetail.ChatApiDetails;
import com.vrd.gsaf.singleton.Singleton;

//import us.zoom.sdk.ZoomSDK;
//import us.zoom.sdk.ZoomSDKInitParams;
//import us.zoom.sdk.ZoomSDKInitializeListener;

public class MainApp extends Application implements LifecycleObserver {
    private static final String TAG = "UIKitApplication";
    public static boolean isForeground;
    static MainApp instance;
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MainApp app = (MainApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    public static MainApp getAppContext() {
        if (instance == null) {
            instance = new MainApp();
        }
        return instance;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(Singleton.getInstance().getContext().getCacheDir())
                .maxCacheFilesCount(11180)
                .maxCacheSize(1024 * 1024 * 1024)
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        comitChat();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

//    private void zoomInitialization() {
//        ZoomSDK zoomSDK = ZoomSDK.getInstance();
//        ZoomSDKInitParams params = new ZoomSDKInitParams();
//        params.appKey = AppConstants.SDK_KEY_ZOOM;
//        params.appSecret = AppConstants.SDK_SECRET_ZOOM;
//        params.domain = "https://zoom.us"; // Required
//        params.enableLog = true; // Optional for debugging
//
//        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
//            @Override
//            public void onZoomSDKInitializeResult(int i, int i1) {
//                // Helper.showToast("zoom initialization success");
//            }
//
//            @Override
//            public void onZoomAuthIdentityExpired() {
//                //  Helper.showToast("zoom initialization failure");
//
//            }
//        };
//        zoomSDK.initialize(getApplicationContext(), listener, params);
//    }


    public void comitChat() {
        ChatApiDetails chatApiDetails = Singleton.getChatDetails();
        if (chatApiDetails != null) {
            AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(chatApiDetails.getRegion()).build();
            CometChat.init(this, chatApiDetails.getAppId(), appSettings, new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String successMessage) {
                    UIKitSettings.setAppID(chatApiDetails.getAppId());
                    UIKitSettings.setAuthKey(chatApiDetails.getApiAuthKey());
                    //  UIKitSettings.setAuthKey(AppConstants.AUTH_KEY_CHAT);
                    Log.d("TAG", "Initialization completed successfully");
                    //  UIKitSettings.setAuthKey(authKey);
                    CometChat.setSource("ui-kit", "android", "java");

                    //  loginUser();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("TAG", "Initialization failed with exception: " + e.getMessage());
                }
            });

            UIKitSettings uiKitSettings = new UIKitSettings(this);
            uiKitSettings.addConnectionListener(TAG);
//            CometChatCallListener.addCallListener("Comet Chat Init", this);
            createNotificationChannel();
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
//        Toast.makeText(this, "foreground", Toast.LENGTH_SHORT).show();
        isForeground = true;
        CometChat.removeCallListener(TAG);
        CometChatCallListener.addCallListener(TAG, this, isForeground);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onMoveToBackground() {
        isForeground = false;
        CometChat.removeCallListener(TAG);
        CometChatCallListener.addCallListener(TAG, this, isForeground);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = "VRD";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

