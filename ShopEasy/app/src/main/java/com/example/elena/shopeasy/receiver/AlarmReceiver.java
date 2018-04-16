package com.example.elena.shopeasy.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.ui.MainActivity;

import java.util.Calendar;

/**
 * Created by Absolute on 3/16/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context, "SHOPEASY_CH_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Time to cook!")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)//timestamp displayed
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{200,200,200,200});//array of millis to turn on-off the vibration
        if (notificationManager != null) {
            notificationManager.notify(100,mNotifyBuilder.build());
        }

    }

}