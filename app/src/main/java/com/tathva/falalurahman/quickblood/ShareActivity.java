package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

public class ShareActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout Facebook;
    LinearLayout Whatsapp;
    LinearLayout Bluetooth;
    LinearLayout Xender;
    LinearLayout Zapya;
    LinearLayout Shareit;
    LinearLayout Superbeam;
    LinearLayout Message;
    ImageView QrCode;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isAdmin",false)){
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(false);
        }else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
        }
        navigationView.setCheckedItem(R.id.nav_share);

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

        QrCode = (ImageView) findViewById(R.id.qrcode);
        ImageLoader.getInstance().displayImage("assets://qrcode.png",QrCode);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        Facebook = (LinearLayout) findViewById(R.id.Facebook);
        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood");
                    ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
                    builder.setContentUrl(uri);
                    builder.setImageUrl(Uri.parse("http://tathva.org/tathva.org/tathva16/aavishkar16/quickblood/imageurl.jpg"));
                    builder.setContentTitle("QuickBlood");
                    builder.setContentDescription("Download QuickBlood and start donating your Blood.");
                    builder.setShareHashtag(new ShareHashtag.Builder()
                    .setHashtag("#QuickBlood")
                    .build());
                    ShareLinkContent shareLinkContent = builder.build();
                    ShareDialog shareDialog = new ShareDialog(ShareActivity.this);
                    shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                }else {
                    Toast.makeText(ShareActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        Whatsapp = (LinearLayout) findViewById(R.id.Whatsapp);
        Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppInstalled("com.whatsapp") ) {
                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood");
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp");
                    intent.putExtra(Intent.EXTRA_TEXT, "*Download QuickBlood at:* " + uri  +". And Start Donating Your Blood.");
                    startActivity(Intent.createChooser(intent, "Share App..."));
                }else {
                    Toast.makeText(ShareActivity.this,"Whatsapp Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        Bluetooth = (LinearLayout) findViewById(R.id.Bluetooth);
        Bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppInstalled("com.android.bluetooth") ) {
                    ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                    String filePath = applicationInfo.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setPackage("com.android.bluetooth");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "Share App..."));
                }else {
                    Toast.makeText(ShareActivity.this,"Bluetooth Is Not Present In Your Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        Xender = (LinearLayout) findViewById(R.id.Xender);
        Xender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppInstalled("cn.xender") ) {
                    ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                    String filePath = applicationInfo.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setPackage("cn.xender");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "Share App..."));
                }else {
                    Toast.makeText(ShareActivity.this,"Xender Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        Shareit = (LinearLayout) findViewById(R.id.Shareit);
        Shareit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppInstalled("com.lenovo.anyshare.gps") ) {
                    ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                    String filePath = applicationInfo.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setPackage("com.lenovo.anyshare.gps");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "Share App..."));
                }else {
                    Toast.makeText(ShareActivity.this,"SHAREit Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        Zapya = (LinearLayout) findViewById(R.id.Zapya);
        Zapya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppInstalled("com.dewmobile.kuaiya") ) {
                    ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                    String filePath = applicationInfo.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setPackage("com.dewmobile.kuaiya");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "Share App..."));
                }else {
                    Toast.makeText(ShareActivity.this,"Zapya Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        Superbeam = (LinearLayout) findViewById(R.id.Superbeam);
        Superbeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppInstalled("com.majedev.superbeam") ) {
                    ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                    String filePath = applicationInfo.sourceDir;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setPackage("com.majedev.superbeam");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                    startActivity(Intent.createChooser(intent, "Share App..."));
                }else {
                    Toast.makeText(ShareActivity.this,"SuperBeam Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        Message = (LinearLayout) findViewById(R.id.Message);
        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("text/plain");
                intent.setData(Uri.parse("sms:"));
                intent.putExtra("sms_body", "Download QuickBlood at: " + uri  +". And Start Donating Your Blood.");
                startActivity(Intent.createChooser(intent, "Share App..."));
            }
        });

    }

    private boolean isAppInstalled(String PackageName){
        PackageManager packageManager = getPackageManager();
        boolean appInstalled = false;
        try{
            packageManager.getPackageInfo(PackageName , PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        }catch (PackageManager.NameNotFoundException exception){
            appInstalled = false;
        }
        return appInstalled;
    }

    @Override
    protected void onPostResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_share);
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_admin_login) {
            Intent intent = new Intent(this, AdminLoginActivity.class);
            intent.putExtra("PreviousActivity", "ShareActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_logout){
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alert_dialog);
            TextView DialogTitle = (TextView) dialog.findViewById(R.id.DialogTitle);
            DialogTitle.setText("Logout");
            TextView DialogMessage = (TextView) dialog.findViewById(R.id.DialogMessage);
            DialogMessage.setText("Are you sure you want to Logout?");
            TextView Confirm = (TextView) dialog.findViewById(R.id.Confirm);
            Confirm.setText("YES");
            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(ShareActivity.this);
            Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin",false);
                    editor.apply();
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
                    navigationView.setCheckedItem(R.id.nav_share);
                    dialog.dismiss();
                }
            });
            TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
            Cancel.setText("NO");
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationView.setCheckedItem(R.id.nav_share);
                    dialog.cancel();
                }
            });
            dialog.show();
        }else if (id == R.id.nav_blood_donors){
            Intent intent = new Intent(this, BloodDonorsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_blood_directory){
            Intent intent = new Intent(this, BloodDirectoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_blood_requests){
            Intent intent = new Intent(this, BloodRequestsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_notification){
            Intent intent = new Intent(this, NotificationsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_emergency){
            Intent intent = new Intent(this, EmergencyContacts.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_blood_donation_forum){
            Intent intent = new Intent(this, BloodDonationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_about_us){
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
