package com.tathva.falalurahman.quickblood;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LandingActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 111;
    final int MY_PERMISSIONS_CAMERA = 112;
    final int MY_PERMISSIONS_INTERNET = 113;
    final int MY_PERMISSIONS_ACCESS_NETWORK_STATE = 114;
    final int MY_PERMISSIONS_ACCESS_WIFI_STATE = 115;
    final int MY_PERMISSIONS_VIBRATE = 116;

    ImageView Background;
    TextView Quote;
    String quotes[] = {"Tears of a mother cannot save her Child. But your Blood can!!!",
            "The Blood Donor of today may be recipient of tomorrow.",
            "To the young and healthy it’s no loss. To sick it’s hope of life. Donate Blood to give back life.",
            "From me to you—a gift of life.",
            "Your Droplets Of Blood May Create Ocean Of Happiness.",
            "A few minutes of your life,A lifetime for someone else"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPoolSize(5)
                .diskCacheExtraOptions(480,320,null)
                .build();
        ImageLoader.getInstance().init(configuration);

        Background = (ImageView) findViewById(R.id.background);
        Quote = (TextView) findViewById(R.id.quote);

        int index = (int )(Math.random() * 6);
        Quote.setText(quotes[index]);


        ImageLoader.getInstance().displayImage("assets://background_landing.jpg", Background);

        Runnable runnable = new Runnable() {

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ImageLoader.getInstance().clearMemoryCache();
                    checkPermission();
                }
            };

            @Override
            public void run() {
                long futureTime = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() < futureTime){
                    synchronized (this){
                        try{
                            wait(futureTime - System.currentTimeMillis());
                            Intent intent1 = new Intent(LandingActivity.this,DownloadDirectoryService.class);
                            startService(intent1);
                            Intent intent2 = new Intent(LandingActivity.this,DownloadRequestService.class);
                            startService(intent2);
                            Intent intent3 = new Intent(LandingActivity.this,NewPostService.class);
                            startService(intent3);
                            Intent intent4 = new Intent(LandingActivity.this,UploadBloodDonorService.class);
                            startService(intent4);
                            Intent intent5 = new Intent(LandingActivity.this,UploadRequestService.class);
                            startService(intent5);
                        }catch (Exception exception){
                            Log.i("Errors","Error In Waiting");
                        }
                    }
                }
                handler.sendEmptyMessage(0);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_CAMERA);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_INTERNET);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    MY_PERMISSIONS_ACCESS_NETWORK_STATE);
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    MY_PERMISSIONS_ACCESS_WIFI_STATE);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.VIBRATE},
                    MY_PERMISSIONS_VIBRATE);
        }else{
            Intent intent = new Intent(LandingActivity.this , HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    showDialog("Permission to WRITE to EXTERNAL STORAGE is required to run this application. Please grant this permission.");
                }
                break;
            case MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    showDialog("Permission to START CAMERA is required to run this application. Please grant this permission.");
                }
                break;
            case MY_PERMISSIONS_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    showDialog("Permission to INTERNET is required to run this application. Please grant this permission.");
                }
                break;
            case MY_PERMISSIONS_ACCESS_NETWORK_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    showDialog("Permission to ACCESS NETWORK STATE is required to run this application. Please grant this permission.");
                }
                break;
            case MY_PERMISSIONS_ACCESS_WIFI_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    showDialog("Permission to ACCESS WIFI STATE is required to run this application. Please grant this permission.");
                }
                break;
            case MY_PERMISSIONS_VIBRATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    showDialog("Permission to VIBRATE is required to run this application. Please grant this permission.");
                }
                break;
        }
    }

    public void showDialog( String message ){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);

        TextView DialogTitle = (TextView) dialog.findViewById(R.id.DialogTitle);
        TextView DialogMessage = (TextView) dialog.findViewById(R.id.DialogMessage);
        TextView Confirm = (TextView) dialog.findViewById(R.id.Confirm);
        DialogTitle.setText("Permission Denied");
        DialogMessage.setText(message);
        Confirm.setText("RETRY");
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                checkPermission();
            }
        });
        TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
        Cancel.setVisibility(View.GONE);
        dialog.show();
    }

}
