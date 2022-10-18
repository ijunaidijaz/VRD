package com.vrd.gsaf.api;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    static String deviceName = android.os.Build.MODEL;
    static String version = "unknown";
    static String packageName = "";
    static String deviceType = "Android";
    static String deviceOs = Build.VERSION.RELEASE;
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        try {
            PackageInfo pInfo = MainApp.getAppContext().getPackageManager().getPackageInfo(MainApp.getAppContext().getPackageName(), 0);
            version = pInfo.versionName;
            packageName = pInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String agent = version + " (" + packageName + ") " + deviceName + " " + deviceType + " " + deviceOs;
        Log.d("Agent", agent);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    if (Singleton.getInstance().getLanguage() != null) {
                        Request request = chain.request().newBuilder().addHeader("requested-lang", Singleton.getInstance().getLanguage())
                                .addHeader("user-agent", agent)
                                .build();
                        return chain.proceed(request);
                    } else {
                        Request request = chain.request().newBuilder().addHeader("user-agent", agent)
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
