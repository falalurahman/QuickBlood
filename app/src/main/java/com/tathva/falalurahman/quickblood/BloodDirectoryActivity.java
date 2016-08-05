package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BloodDirectoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout Kasargod;
    LinearLayout Kannur;
    LinearLayout Wayanad;
    LinearLayout Kozhikode;
    LinearLayout Malappuram;
    LinearLayout Palakkad;
    LinearLayout Thrissur;
    LinearLayout Ernakulam;
    LinearLayout Idukki;
    LinearLayout Kottayam;
    LinearLayout Alappuzha;
    LinearLayout Pathanamthitta;
    LinearLayout Kollam;
    LinearLayout Thiruvananthapuram;

    ImageView KasargodPic;
    ImageView KannurPic;
    ImageView WayanadPic;
    ImageView KozhikodePic;
    ImageView MalappuramPic;
    ImageView PalakkadPic;
    ImageView ThrissurPic;
    ImageView ErnakulamPic;
    ImageView IdukkiPic;
    ImageView KottayamPic;
    ImageView AlappuzhaPic;
    ImageView PathanamthittaPic;
    ImageView KollamPic;
    ImageView ThiruvananthapuramPic;


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

        Kasargod = (LinearLayout) findViewById(R.id.Kasargod);
        Kannur = (LinearLayout) findViewById(R.id.Kannur);
        Wayanad = (LinearLayout) findViewById(R.id.Wayanad);
        Kozhikode = (LinearLayout) findViewById(R.id.Kozhikode);
        Malappuram = (LinearLayout) findViewById(R.id.Malappuram);
        Palakkad = (LinearLayout) findViewById(R.id.Palakkad);
        Thrissur = (LinearLayout) findViewById(R.id.Thrissur);
        Ernakulam = (LinearLayout) findViewById(R.id.Ernakulam);
        Idukki = (LinearLayout) findViewById(R.id.Idukki);
        Kottayam = (LinearLayout) findViewById(R.id.Kottayam);
        Alappuzha = (LinearLayout) findViewById(R.id.Alappuzha);
        Pathanamthitta = (LinearLayout) findViewById(R.id.Pathanamthitta);
        Kollam = (LinearLayout) findViewById(R.id.Kollam);
        Thiruvananthapuram = (LinearLayout) findViewById(R.id.Thiruvananthapuram);

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

        KasargodPic = (ImageView) findViewById(R.id.kasargodPic);
        KannurPic = (ImageView) findViewById(R.id.kannurPic);
        WayanadPic = (ImageView) findViewById(R.id.wayanadPic);
        KozhikodePic = (ImageView) findViewById(R.id.kozhikodePic);
        MalappuramPic = (ImageView) findViewById(R.id.malappuramPic);
        PalakkadPic = (ImageView) findViewById(R.id.palakkadPic);
        ThrissurPic = (ImageView) findViewById(R.id.thrissurPic);
        ErnakulamPic = (ImageView) findViewById(R.id.ernakulamPic);
        IdukkiPic = (ImageView) findViewById(R.id.idukkiPic);
        KottayamPic = (ImageView) findViewById(R.id.kottayamPic);
        AlappuzhaPic = (ImageView) findViewById(R.id.alappuzhaPic);
        PathanamthittaPic = (ImageView) findViewById(R.id.pathanamthittaPic);
        KollamPic = (ImageView) findViewById(R.id.kollamPic);
        ThiruvananthapuramPic = (ImageView) findViewById(R.id.thiruvananthapuramPic);

        ImageLoader.getInstance().displayImage("assets://kasargod.png", KasargodPic);
        ImageLoader.getInstance().displayImage("assets://kannur.png", KannurPic);
        ImageLoader.getInstance().displayImage("assets://wayanad.png", WayanadPic);
        ImageLoader.getInstance().displayImage("assets://kozhikode.png", KozhikodePic);
        ImageLoader.getInstance().displayImage("assets://malappuram.png", MalappuramPic);
        ImageLoader.getInstance().displayImage("assets://palakkad.png", PalakkadPic);
        ImageLoader.getInstance().displayImage("assets://thrissur.png", ThrissurPic);
        ImageLoader.getInstance().displayImage("assets://ernakulam.png", ErnakulamPic);
        ImageLoader.getInstance().displayImage("assets://idukki.png", IdukkiPic);
        ImageLoader.getInstance().displayImage("assets://kottayam.png", KottayamPic);
        ImageLoader.getInstance().displayImage("assets://alappuzha.png", AlappuzhaPic);
        ImageLoader.getInstance().displayImage("assets://pathanamthitta.png", PathanamthittaPic);
        ImageLoader.getInstance().displayImage("assets://kollam.png", KollamPic);
        ImageLoader.getInstance().displayImage("assets://thiruvananthapuram.png", ThiruvananthapuramPic);

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
