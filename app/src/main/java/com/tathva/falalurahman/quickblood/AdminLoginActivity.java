package com.tathva.falalurahman.quickblood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rengwuxian.materialedittext.MaterialEditText;

public class AdminLoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MaterialEditText UsernameEditText;
    MaterialEditText PasswordEditText;
    Button LoginButton;
    String PreviousActivity;
    ImageView AdminPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        PreviousActivity = getIntent().getStringExtra("PreviousActivity");

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_admin_login);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);

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

        AdminPic = (ImageView) findViewById(R.id.adminloginPic);
        ImageLoader.getInstance().displayImage("assets://admin_login.png",AdminPic);

        UsernameEditText = (MaterialEditText) findViewById(R.id.username_edit_text);
        PasswordEditText = (MaterialEditText) findViewById(R.id.password_edit_text);
        LoginButton = (Button) findViewById(R.id.login_button);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UsernameEditText.getText().toString();
                String password = PasswordEditText.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if( username.equals("tathva16") && password.equals("aavishkar16") ){
                    editor.putBoolean("isAdmin",true);
                    editor.putString("Username","Tathva 16");
                    editor.apply();
                    GoToPreviousActivity();
                } else if( username.equals("blood_donors_kerala") && password.equals("kerala16") ){
                    editor.putBoolean("isAdmin",true);
                    editor.putString("Username","Blood Donors Kerala");
                    editor.apply();
                    GoToPreviousActivity();
                } else {
                    Toast.makeText(AdminLoginActivity.this,"Invalid Username Or Password",Toast.LENGTH_LONG).show();
                    UsernameEditText.setError("Invalid Username/ Password");
                    PasswordEditText.setError("Invalid Username/ Password");
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_admin_login);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
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

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_blood_donors) {
            Intent intent = new Intent(this, BloodDonorsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_blood_directory){
            Intent intent = new Intent(this, BloodDirectoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }  else if (id == R.id.nav_blood_requests){
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

    private void GoToPreviousActivity(){
        if(PreviousActivity.equals("Home")){
            Intent intent = new Intent(AdminLoginActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("BloodDonorsActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,BloodDonorsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("BloodDirectoryActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,BloodDirectoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("BloodRequestsActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,BloodRequestsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("NotificationsActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,NotificationsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("ShareActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,ShareActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("BloodDonationActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,BloodDonationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(PreviousActivity.equals("AboutUsActivity")){
            Intent intent = new Intent(AdminLoginActivity.this,AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
