package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BloodDirectoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView Kasargod;
    TextView Kannur;
    TextView Wayanad;
    TextView Kozhikode;
    TextView Malappuram;
    TextView Palakkad;
    TextView Thrissur;
    TextView Ernakulam;
    TextView Idukki;
    TextView Kottayam;
    TextView Alappuzha;
    TextView Pathanamthitta;
    TextView Kollam;
    TextView Thiruvananthapuram;

    LinearLayout KasargodPic;
    LinearLayout KannurPic;
    LinearLayout WayanadPic;
    LinearLayout KozhikodePic;
    LinearLayout MalappuramPic;
    LinearLayout PalakkadPic;
    LinearLayout ThrissurPic;
    LinearLayout ErnakulamPic;
    LinearLayout IdukkiPic;
    LinearLayout KottayamPic;
    LinearLayout AlappuzhaPic;
    LinearLayout PathanamthittaPic;
    LinearLayout KollamPic;
    LinearLayout ThiruvananthapuramPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_directory);
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
        navigationView.setCheckedItem(R.id.nav_blood_directory);

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

        Kasargod = (TextView) findViewById(R.id.Kasargod);
        Kannur = (TextView) findViewById(R.id.Kannur);
        Wayanad = (TextView) findViewById(R.id.Wayanad);
        Kozhikode = (TextView) findViewById(R.id.Kozhikode);
        Malappuram = (TextView) findViewById(R.id.Malappuram);
        Palakkad = (TextView) findViewById(R.id.Palakkad);
        Thrissur = (TextView) findViewById(R.id.Thrissur);
        Ernakulam = (TextView) findViewById(R.id.Ernakulam);
        Idukki = (TextView) findViewById(R.id.Idukki);
        Kottayam = (TextView) findViewById(R.id.Kottayam);
        Alappuzha = (TextView) findViewById(R.id.Alappuzha);
        Pathanamthitta = (TextView) findViewById(R.id.Pathanamthitta);
        Kollam = (TextView) findViewById(R.id.Kollam);
        Thiruvananthapuram = (TextView) findViewById(R.id.Thiruvananthapuram);

        Kasargod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Kasargod");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Kannur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Kannur");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Wayanad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Wayanad");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Kozhikode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Kozhikode");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Malappuram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Malappuram");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Palakkad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Palakkad");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Thrissur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Thrissur");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Ernakulam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Ernakulam");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Idukki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Idukki");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Kottayam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Kottayam");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Alappuzha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Alapppuzha");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Pathanamthitta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Pathanamthitta");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Kollam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Kollam");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
        Thiruvananthapuram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodDirectoryActivity.this,ViewDirectoryActivity.class);
                intent.putExtra("District","Thiruvananthapuram");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });

        KasargodPic = (LinearLayout) findViewById(R.id.kasargodPic);
        KannurPic = (LinearLayout) findViewById(R.id.kannurPic);
        WayanadPic = (LinearLayout) findViewById(R.id.wayanadPic);
        KozhikodePic = (LinearLayout) findViewById(R.id.kozhikodePic);
        MalappuramPic = (LinearLayout) findViewById(R.id.malappuramPic);
        PalakkadPic = (LinearLayout) findViewById(R.id.palakkadPic);
        ThrissurPic = (LinearLayout) findViewById(R.id.thrissurPic);
        ErnakulamPic = (LinearLayout) findViewById(R.id.ernakulamPic);
        IdukkiPic = (LinearLayout) findViewById(R.id.idukkiPic);
        KottayamPic = (LinearLayout) findViewById(R.id.kottayamPic);
        AlappuzhaPic = (LinearLayout) findViewById(R.id.alappuzhaPic);
        PathanamthittaPic = (LinearLayout) findViewById(R.id.pathanamthittaPic);
        KollamPic = (LinearLayout) findViewById(R.id.kollamPic);
        ThiruvananthapuramPic = (LinearLayout) findViewById(R.id.thiruvananthapuramPic);

        ImageLoader.getInstance().loadImage("assets://kasargod.jpg", new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                KasargodPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://kannur.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                KannurPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://wayanad.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                WayanadPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://kozhikode.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                KozhikodePic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://malappuram.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                MalappuramPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://palakkad.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                PalakkadPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://thrissur.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ThrissurPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://ernakulam.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ErnakulamPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://idukki.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                IdukkiPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://kottayam.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                KottayamPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://alappuzha.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                AlappuzhaPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://pathanamthitta.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                PathanamthittaPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://kollam.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                KollamPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage("assets://thiruvananthapuram.jpg",  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ThiruvananthapuramPic.setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }

    @Override
    protected void onPostResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_blood_directory);
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
            intent.putExtra("PreviousActivity", "BloodDirectoryActivity");
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
            navigationView.setNavigationItemSelectedListener(BloodDirectoryActivity.this);
            Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin",false);
                    editor.apply();
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
                    navigationView.setCheckedItem(R.id.nav_blood_directory);
                    dialog.dismiss();
                }
            });
            TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
            Cancel.setText("NO");
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationView.setCheckedItem(R.id.nav_blood_directory);
                    dialog.cancel();
                }
            });
            dialog.show();
        } else if (id == R.id.nav_blood_donors){
            Intent intent = new Intent(this, BloodDonorsActivity.class);
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
        } else if (id == R.id.nav_share){
            Intent intent = new Intent(this, ShareActivity.class);
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
}
