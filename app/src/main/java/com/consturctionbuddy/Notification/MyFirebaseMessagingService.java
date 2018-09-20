package com.consturctionbuddy.Notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.consturctionbuddy.Activity.LoginActivity;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.StorageUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        String message = "";
        String title = "";

        if (remoteMessage.getData().size() > 0) {
            message = remoteMessage.getData().get("message");
            title = remoteMessage.getData().get(Constant.NOTIFICATION_TITLE_KEY);
        }

        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
        }


        Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
        resultIntent.putExtra(Constant.INTENT_NOTIFICATION_MSG, message);
        resultIntent.putExtra(Constant.INTENT_NOTIFICATION_TITLE, title);

        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
            pushNotification.putExtra(Constant.NOTIFICATION_MSG_KEY, message);
            pushNotification.putExtra(Constant.NOTIFICATION_TITLE_KEY, title);
            int count = StorageUtils.getPrefForCount(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY);
            count++;
            StorageUtils.putPref(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY, count);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            playNotificationSound();

        } else {
            // app is in background, show the notification in notification tray
            //showBigNotification(message, title);
            int count = StorageUtils.getPrefForCount(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY);
            count++;
            StorageUtils.putPref(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY, count);
            sendNotification(message, title, resultIntent);
        }


    }


    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    @SuppressLint("InlinedApi")
    public void playNotificationSound() {
        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
            r.play();

            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (v != null && v.hasVibrator()) {
                v.vibrate(VibrationEffect.DEFAULT_AMPLITUDE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendNotification(String message, String title, Intent resultIntent) {


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] vibrate = {500, 1000};
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                //Vibration
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                //LED
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS) // requires VIBRATE permission
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setVibrate(vibrate);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}