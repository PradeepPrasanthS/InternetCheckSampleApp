package com.example.internetchecksampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private boolean wifi;
    private boolean mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView connectivity = findViewById(R.id.connetivityCheck);
        createNotificationChannel();
        if (isConnectedViaWifi()){
            Toast.makeText(this, "Wifi connection detected", Toast.LENGTH_LONG);
            connectivity.setText("Security profile enabled");
        } else if(isConnectedViaMobile()){
            Toast.makeText(this, "Mobile connection detected", Toast.LENGTH_LONG);
            connectivity.setText("Security profile disabled");
        }

        startService(new Intent(getBaseContext(), MyService.class));

    }

    private boolean isConnectedViaWifi() {
        boolean isConnected = ConnectivityReceiver.isConnectedviaWifi(this);
        return isConnected;
    }

    public boolean isConnectedViaMobile() {
        boolean isConnected = ConnectivityReceiver.isConnectedviaMobile(this);
        return isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isConnectedViaWifi()){
            wifi = true;
        }

        if (isConnectedViaMobile()){
            mobile = true;
        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Security Status";
            String description = "Provides the status of the network";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("WifiStatus", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
