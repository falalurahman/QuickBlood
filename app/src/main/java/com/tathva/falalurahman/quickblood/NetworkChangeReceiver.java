package com.tathva.falalurahman.quickblood;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            Intent intent1 = new Intent(context,DownloadDirectoryService.class);
            context.startService(intent1);
            Intent intent2 = new Intent(context,DownloadRequestService.class);
            context.startService(intent2);
            Intent intent3 = new Intent(context,NewPostService.class);
            context.startService(intent3);
            Intent intent4 = new Intent(context,UploadBloodDonorService.class);
            context.startService(intent4);
            Intent intent5 = new Intent(context,UploadRequestService.class);
            context.startService(intent5);
        }
    }
}
