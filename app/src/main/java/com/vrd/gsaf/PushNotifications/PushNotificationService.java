package com.vrd.gsaf.PushNotifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CallManager;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.singleton.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseService";
    private static final int REQUEST_CODE = 12;
    public static String token;
    CallManager callManager;
    private JSONObject json;
    private Intent intent;
    private int count = 0;
    private Call call;
    private boolean isCall;

    @Override
    public void onNewToken(String s) {
        token = s;
        Singleton.getInstance().setFirebaseToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("junaidsmessage", "onMessageReceived: ");
        try {
            if (!Singleton.getInstance().getActivityRunning()) {
                Intent intent = new Intent(Singleton.getInstance().getContext(), MainActivity.class);
                startActivity(intent);
            }
            count++;
            json = new JSONObject(remoteMessage.getData());
            Log.d("JunaidCalling", json.toString());
            Log.d(TAG, "JSONObject: " + json.toString());
            JSONObject messageData = new JSONObject(json.getString("message"));
            BaseMessage baseMessage = CometChatHelper.processMessage(new JSONObject(remoteMessage.getData().get("message")));
            if (baseMessage instanceof Call) {
                call = (Call) baseMessage;
                isCall = true;
            }
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    Helper.showToast("Call");
//
//                }
//            });

            showNotification(baseMessage);
//            if(!Singleton.getInstance().getActivityRunning()) {
//                if (json.getString("alert").equals("Incoming audio call")) {
//                    Intent intent = new Intent(this, HomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    getApplicationContext().startActivity(intent);
//
////                    Intent activtiyIntent = new Intent(Singleton.getInstance().getContext(), HomeActivity.class);
////                    startActivity(activtiyIntent);
//                }
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Bitmap getBitmapFromURL(String strURL) {
        if (strURL != null) {
            try {
                URL url = new URL(strURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private void showNotification(BaseMessage baseMessage) {

        try {
            int m = (int) ((new Date().getTime()));
            String GROUP_ID = "group_id";


//          //  waaqas
//
//            Intent notificationIntent = new Intent(Singleton.getInstance().getContext(), HomeActivity.class);
//
//            PendingIntent intent = PendingIntent.getActivity(Singleton.getInstance().getContext(), 0,
//                    notificationIntent, 0);
//          //  waqas


            Intent messageIntent = new Intent(getApplicationContext(), CometChatMessageListActivity.class);
            messageIntent.putExtra(UIKitConstants.IntentStrings.TYPE, baseMessage.getReceiverType());
            if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                messageIntent.putExtra(UIKitConstants.IntentStrings.NAME, baseMessage.getSender().getName());
                messageIntent.putExtra(UIKitConstants.IntentStrings.UID, baseMessage.getSender().getUid());
                messageIntent.putExtra(UIKitConstants.IntentStrings.AVATAR, baseMessage.getSender().getAvatar());
                messageIntent.putExtra(UIKitConstants.IntentStrings.STATUS, baseMessage.getSender().getStatus());
            } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                messageIntent.putExtra(UIKitConstants.IntentStrings.GUID, ((Group) baseMessage.getReceiver()).getGuid());
                messageIntent.putExtra(UIKitConstants.IntentStrings.NAME, ((Group) baseMessage.getReceiver()).getName());
                messageIntent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC, ((Group) baseMessage.getReceiver()).getDescription());
                messageIntent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE, ((Group) baseMessage.getReceiver()).getGroupType());
                messageIntent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, ((Group) baseMessage.getReceiver()).getOwner());
                messageIntent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT, ((Group) baseMessage.getReceiver()).getMembersCount());
            }
            PendingIntent messagePendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0123, messageIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(json.getString("title"))
                    .setContentText(json.getString("alert"))
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setLargeIcon(getBitmapFromURL(baseMessage.getSender().getAvatar()))
                    .setGroup(GROUP_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromURL(((MediaMessage) baseMessage).getAttachment().getFileUrl())));
            }
            NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(this, "2")
                    .setContentTitle("CometChat")
                    .setContentText(count + " messages")
                    .setSmallIcon(R.mipmap.app_icon)
                    .setGroup(GROUP_ID)
                    .setGroupSummary(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            if (isCall) {
                ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//                ActivityManager.RunningTaskInfo task = tasks.get(0); // current task
//                ComponentName rootActivity = task.baseActivity;
//                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

                if (MainApp.isForeground) {
                    //Do whatever here
                } else {
                    builder.setGroup(GROUP_ID + "Call");
                    if (json.getString("alert").equals("Incoming audio call") || json.getString("alert").equals("Incoming video call")) {
                        builder.setOngoing(true);
                        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                        builder.addAction(0, "Answer", PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, getCallIntent("Answer"), PendingIntent.FLAG_IMMUTABLE));
                        builder.addAction(0, "Decline", PendingIntent.getBroadcast(getApplicationContext(), 1, getCallIntent("Decline"), PendingIntent.FLAG_IMMUTABLE));
                    }
                    notificationManager.notify(5, builder.build());
                }
            } else {
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setContentIntent(messagePendingIntent);
                builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
//                Uri notification = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.incoming_message);
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                notificationManager.notify(baseMessage.getId(), builder.build());
                notificationManager.notify(0, summaryBuilder.build());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Intent getCallIntent(String title) {
        Intent callIntent = new Intent(getApplicationContext(), CallNotificationAction.class);
        callIntent.putExtra(UIKitConstants.IntentStrings.SESSION_ID, call.getSessionId());
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setAction(title);
        return callIntent;
    }
}
