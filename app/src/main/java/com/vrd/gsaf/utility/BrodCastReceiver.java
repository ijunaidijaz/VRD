package com.vrd.gsaf.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.singleton.Singleton;

public class BrodCastReceiver extends BroadcastReceiver {
    int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {


        Ringtone ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        ringtone.play();
        Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
        //  SystemClock.sleep(2000);
        //  ringtone.stop();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Singleton.getInstance().getContext(), "1000")
                .setSmallIcon(R.drawable.calendar_alt)
                .setContentTitle("waqas")
                .setContentText("webinar is going to start")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Singleton.getInstance().getContext());

        notificationManagerCompat.notify(200, builder.build());

        createNotification();


    }

    private void createNotification() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotificationChannel();
                handler.postDelayed(this, 5000);

            }
        }, 5000);  //the time is in miliseconds
    }

    private void setNotificationChannel() {
//        Singleton.getInstance().setContext(this.getApplicationContext());
//        Intent intent = new Intent(this, BrodCastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//                40000,
//                pendingIntent);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Singleton.getInstance().getContext(), "notify_001");
        Intent ii = new Intent(Singleton.getInstance().getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Singleton.getInstance().getContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("addasd");
        bigText.setBigContentTitle("Today's Quran Verse");
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.app_icon_round);
        mBuilder.setContentTitle("Your Title" + id);
        id++;
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) Singleton.getInstance().getContext().getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(id, mBuilder.build());
    }

}
