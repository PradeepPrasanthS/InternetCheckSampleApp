package com.example.internetchecksampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    private ConnectivityReceiverListener mConnectivityReceiverListener;
    public static long lastNoConnectionTs = -1;
    public static boolean isOnline = true;

    ConnectivityReceiver(ConnectivityReceiverListener listener) {
        mConnectivityReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (mConnectivityReceiverListener != null) {
            mConnectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnectedviaWifi(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isConnectedviaMobile(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
