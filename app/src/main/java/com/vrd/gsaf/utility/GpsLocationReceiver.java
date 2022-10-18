package com.vrd.gsaf.utility;

import static android.content.Context.LOCATION_SERVICE;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.singleton.Singleton;

public class GpsLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {


            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //isGPSEnabled = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Singleton.getInstance().getFloatingButton().setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                        if (Singleton.getInstance().getIsLoggedIn() != null && Singleton.getInstance().getIsLoggedIn()) {
                            Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
                                    Toast.LENGTH_SHORT).show();
                            Activity activity = new MainActivity();
                            //        ((HomeActivity) activity).getLocation();

                            Helper.HomeAct(new MainActivity());
                        }
                    }
                }, 500);

            } else {
                //isGPSEnabled = false;
            }

//            Intent pushIntent = new Intent(context, LocalService.class);
//            context.startService(pushIntent);
        }
    }
}