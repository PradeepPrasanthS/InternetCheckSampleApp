package com.example.internetchecksampleapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyService extends Service {

    static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (CONNECTIVITY_CHANGE_ACTION.equals(action)) {
                    if (!ConnectivityReceiver.isConnectedviaWifi(context)) {
                        if (context != null) {
                            boolean show = false;
                            if (ConnectivityReceiver.lastNoConnectionTs == -1) {//first time
                                show = true;
                                ConnectivityReceiver.lastNoConnectionTs = System.currentTimeMillis();
                            } else {
                                if (System.currentTimeMillis() - ConnectivityReceiver.lastNoConnectionTs > 1000) {
                                    show = true;
                                    ConnectivityReceiver.lastNoConnectionTs = System.currentTimeMillis();
                                }
                            }

                            if (show && ConnectivityReceiver.isOnline) {
                                ConnectivityReceiver.isOnline = false;
                                Log.i("NETWORK123", "Connection lost");
                                showNotificationsForMobile();
                            }
                        }
                    } else {
                        Log.i("NETWORK123", "Connected");
                        showNotificationsForWifi();
                        ConnectivityReceiver.isOnline = true;
                    }
                }
            }
        };
        registerReceiver(receiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void showNotificationsForWifi() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "WifiStatus")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Security Status")
                .setContentText("Security profile enabled")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }

    public void showNotificationsForMobile() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "WifiStatus")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Security Status")
                .setContentText("Security profile disabled")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }
}
