package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.nineoldandroids.animation.Animator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsfeedAdapter.FacebookShareListener{

    ListView listView;
    LinearLayout statusButton;
    LinearLayout photoButton;
    LinearLayout footer;
    TextView statusTextView;
    TextView UsernameTextView;
    TextView NotificationBadge;
    ImageView ProfilePicture;
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    NewsfeedAdapter newsfeedAdapter;
    SwipeRefreshLayout swipeRefresh;
    ViewGroup headerNewPost;
    CallbackManager callbackManager;
    private BroadcastReceiver broadcastReceiver;

    TintableImageView AboutUsButton;
    TintableImageView BloodDonorButton;
    TintableImageView BloodDirectoryButton;
    TintableImageView BloodRequestButton;
    TintableImageView NotificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

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

        ImageLoader.getInstance().loadImage("assets://main_background.jpg", new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                findViewById(R.id.background).setBackground(new BitmapDrawable(getResources(),loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        footer = (LinearLayout) findViewById(R.id.footer);
        listView = (ListView) findViewById(R.id.newsfeed);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    YoYo.with(Techniques.SlideInUp)
                            .duration(200)
                            .playOn(footer);
                }
                else {
                    YoYo.with(Techniques.SlideOutDown)
                            .duration(200)
                            .playOn(footer);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        if(sharedPreferences.getBoolean("isAdmin",false)){
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(false);
            LayoutInflater layoutInflater = getLayoutInflater();
            headerNewPost = (ViewGroup) layoutInflater.inflate(R.layout.header_new_post,listView,false);
            listView.addHeaderView(headerNewPost,null,true);

            statusButton = (LinearLayout) findViewById(R.id.statusButton);
            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,NewPostActivity.class);
                    startActivity(intent);
                }
            });

            photoButton = (LinearLayout) findViewById(R.id.photoButton);
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,NewPostActivity.class);
                    intent.putExtra("Photo",true);
                    startActivity(intent);
                }
            });

            statusTextView = (TextView) findViewById(R.id.statusTextView);
            statusTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,NewPostActivity.class);
                    startActivity(intent);
                }
            });

            UsernameTextView = (TextView) findViewById(R.id.username);
            final String username = sharedPreferences.getString("Username","");
            UsernameTextView.setText(username);

            ProfilePicture = (ImageView) findViewById(R.id.profile_pic);
            if(username.equals("Tathva 16")){
                ImageLoader.getInstance().displayImage("assets://tathva16.jpg",ProfilePicture);
            }else if(username.equals("Blood Donors Kerala")){
                ImageLoader.getInstance().displayImage("assets://blood_donor.jpg",ProfilePicture);
            }

            UsernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(username.equals("Tathva 16")){
                        Intent intent = new Intent(HomeActivity.this,AboutUsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if(username.equals("Blood Donors Kerala")){
                        Intent intent = new Intent(HomeActivity.this,BloodDonationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
            ProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(username.equals("Tathva 16")){
                        Intent intent = new Intent(HomeActivity.this,AboutUsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if(username.equals("Blood Donors Kerala")){
                        Intent intent = new Intent(HomeActivity.this,BloodDonationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });

        }else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
        }
        navigationView.setCheckedItem(R.id.nav_home);


        newsfeedAdapter = new NewsfeedAdapter(this,null);
        listView.setAdapter(newsfeedAdapter);
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = {TableQBPosts.COLUMN_ID , TableQBPosts.COLUMN_USERNAME , TableQBPosts.COLUMN_TIMESTAMP,
                TableQBPosts.COLUMN_STATUS,TableQBPosts.COLUMN_LINK,TableQBPosts.COLUMN_IMAGE};
                CursorLoader cursorLoader;
                if(sharedPreferences.getBoolean("ShowNotifications",true)) {
                    cursorLoader = new CursorLoader(HomeActivity.this, DatabaseContentProvider.QBPOST_URI, projection, null, null, TableQBPosts.COLUMN_ID + " DESC");
                }else {
                    cursorLoader = new CursorLoader(HomeActivity.this, DatabaseContentProvider.QBPOST_URI, projection, TableQBPosts.COLUMN_USERNAME + " <> 'QuickBlood Blood Request'", null, TableQBPosts.COLUMN_ID + " DESC");
                }
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                newsfeedAdapter.changeCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                newsfeedAdapter.changeCursor(null);
            }
        };
        getLoaderManager().initLoader(0,null,loaderCallbacks);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.get_post_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(TableQBPosts.COLUMN_ID, jsonObject.getLong("PostId"));
                                        contentValues.put(TableQBPosts.COLUMN_USERNAME, jsonObject.getString("Username"));
                                        contentValues.put(TableQBPosts.COLUMN_TIMESTAMP, jsonObject.getString("TimeStamp"));
                                        contentValues.put(TableQBPosts.COLUMN_STATUS, jsonObject.getString("Status"));
                                        contentValues.put(TableQBPosts.COLUMN_LINK, jsonObject.getString("Link"));
                                        contentValues.put(TableQBPosts.COLUMN_IMAGE, jsonObject.getString("Image"));
                                        Uri uri = DatabaseContentProvider.QBPOST_URI;
                                        getContentResolver().insert(uri, contentValues);

                                        if(i == 0){
                                            editor.putLong("QBPostsStartId",jsonObject.getLong("PostId"));
                                            editor.apply();
                                        }
                                        if(sharedPreferences.getLong("QBPostsEndId",0)==0 && i == jsonArray.length()-1){
                                            editor.putLong("QBPostsEndId",jsonObject.getLong("PostId"));
                                            editor.apply();
                                        }
                                    }
                                    swipeRefresh.setRefreshing(false);
                                } catch (JSONException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                swipeRefresh.setRefreshing(false);
                                if (error == null || error.getMessage() == null || error.getMessage().equals("") || error instanceof NoConnectionError ) {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error In Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<>();
                        SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                        params.put("startId", Long.toString( sharedPreferences.getLong("QBPostsStartId",0)));
                        return params;
                    }
                };
                SingletonClass.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
            }
        });
        NotificationBadge = (TextView) findViewById(R.id.notification_badge);
        int notificationCount = sharedPreferences.getInt("NotificationCount",0);
        if(notificationCount == 0){
            NotificationBadge.setVisibility(View.GONE);
        }else {
            NotificationBadge.setVisibility(View.VISIBLE);
            NotificationBadge.setText(Integer.toString(notificationCount));
        }

        Intent intent = new Intent(this,NewPostService.class);
        startService(intent);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int NotificationCount = intent.getIntExtra("NotificationCount",0);
                if(NotificationCount == 0){
                    NotificationBadge.setVisibility(View.GONE);
                }else {
                    NotificationBadge.setVisibility(View.VISIBLE);
                    NotificationBadge.setText(Integer.toString(NotificationCount));
                }
            }
        };

        AboutUsButton = (TintableImageView) findViewById(R.id.aboutUsButton);
        AboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        BloodDonorButton = (TintableImageView) findViewById(R.id.bloodDonorButton);
        BloodDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BloodDonorsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        BloodDirectoryButton = (TintableImageView) findViewById(R.id.bloodDirectoryButton);
        BloodDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BloodDirectoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        BloodRequestButton = (TintableImageView) findViewById(R.id.bloodRequestButton);
        BloodRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BloodRequestsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        NotificationButton = (TintableImageView) findViewById(R.id.notificationButton);
        NotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        Tutorial();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter(DownloadRequestService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPostResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("ShowNotifications",true)){
            menu.findItem(R.id.action_show_notifications).setVisible(false);
            menu.findItem(R.id.action_hide_notifications).setVisible(true);
        }else {
            menu.findItem(R.id.action_show_notifications).setVisible(true);
            menu.findItem(R.id.action_hide_notifications).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (id == R.id.action_hide_notifications) {
            editor.putBoolean("ShowNotifications",false);
            editor.apply();
            invalidateOptionsMenu();
            newsfeedAdapter.changeCursor(null);
            getLoaderManager().restartLoader(0,null,loaderCallbacks);
            return true;
        }else if (id == R.id.action_show_notifications){
            editor.putBoolean("ShowNotifications",true);
            editor.apply();
            invalidateOptionsMenu();
            newsfeedAdapter.changeCursor(null);
            getLoaderManager().restartLoader(0,null,loaderCallbacks);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_admin_login) {
            Intent intent = new Intent(this, AdminLoginActivity.class);
            intent.putExtra("PreviousActivity", "Home");
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
            navigationView.setNavigationItemSelectedListener(HomeActivity.this);
            Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin",false);
                    editor.apply();
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
                    navigationView.setCheckedItem(R.id.nav_home);
                    listView.removeHeaderView(headerNewPost);
                    dialog.dismiss();
                }
            });
            TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
            Cancel.setText("NO");
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationView.setCheckedItem(R.id.nav_home);
                    dialog.cancel();
                }
            });
            dialog.show();
        } else if (id == R.id.nav_blood_donors) {
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
        } else if (id == R.id.nav_share){
            Intent intent = new Intent(this, ShareActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_emergency){
            Intent intent = new Intent(this, EmergencyContacts.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.nav_blood_donation_forum){
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
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().delete(Uri.parse(DatabaseContentProvider.QBPOST_URI + "/Limit/10") ,null,null);
        Cursor cursor = getContentResolver().query(Uri.parse(DatabaseContentProvider.QBPOST_URI + "/Limit/1"),
                new String[] {TableQBPosts.COLUMN_ID},null,null,TableQBPosts.COLUMN_ID + " ASC ");
        SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(cursor!=null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            editor.putLong("QBPostsEndId", cursor.getLong(cursor.getColumnIndex(TableQBPosts.COLUMN_ID)));
            editor.apply();
        }
        if(cursor!=null) {
            Log.i("QB","Reached Here");
            cursor.close();
        }
    }

    @Override
    public void onShare(String Status,final String Link,final Uri ImageURL, final String WhatsappText, final boolean isRequest) {
        Status = Status.replaceAll("<b>","");
        Status = Status.replaceAll("</b>","");
        Status = Status.replaceAll("<br>","\n");
        final String newStatus = Status;
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_share);
        final LinearLayout Facebook = (LinearLayout) dialog.findViewById(R.id.Facebook);
        if(Facebook != null) {
            if (ImageURL == null && Link.equals(""))
                Facebook.setVisibility(View.GONE);
            Facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImageURL == null) {
                        if (!Link.equals("")) {
                            ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
                            builder.setContentTitle("QuickBlood");
                            builder.setImageUrl(Uri.parse("http://tathva.org/tathva.org/tathva16/aavishkar16/quickblood/imageurl.jpg"));
                            builder.setShareHashtag(new ShareHashtag.Builder()
                                    .setHashtag("#QuickBlood")
                                    .build());
                            if (!newStatus.equals("")) {
                                builder.setContentDescription(newStatus);
                            }
                            if (!Link.equals("")) {
                                builder.setContentUrl(Uri.parse(Link));
                            }
                            ShareLinkContent shareLinkContent = builder.build();
                            ShareDialog shareDialog = new ShareDialog(HomeActivity.this);
                            shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                        }
                    } else {
                        SharePhoto.Builder builder = new SharePhoto.Builder();
                        builder.setImageUrl(ImageURL);
                        if (!newStatus.equals("") && !Link.equals(""))
                            builder.setCaption(newStatus + "\nLink: " + Link);
                        else if (!newStatus.equals(""))
                            builder.setCaption(newStatus);
                        else if (!Link.equals(""))
                            builder.setCaption("Link: " + Link);
                        if(isRequest)
                            builder.setCaption("Download QuickBlood at: " + Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood")  +". And Start Donating Your Blood.");
                        SharePhoto photo = builder.build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .setShareHashtag(new ShareHashtag.Builder()
                                        .setHashtag("#QuickBlood")
                                        .build())
                                .addPhoto(photo)
                                .build();
                        ShareDialog shareDialog = new ShareDialog(HomeActivity.this);
                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                    }
                    dialog.dismiss();
                }
            });
        }
        LinearLayout Whatsapp = (LinearLayout) dialog.findViewById(R.id.Whatsapp);
        if(Whatsapp != null){
            Whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( isAppInstalled("com.whatsapp") ) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.setPackage("com.whatsapp");
                        if(ImageURL != null && !isRequest) {
                            Uri uri = ImageURL;
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                        }
                        if (!newStatus.equals("") && !Link.equals("") && !isRequest)
                            intent.putExtra(Intent.EXTRA_TEXT, newStatus + "\nLink: " + Link);
                        else if (!newStatus.equals("") && !isRequest)
                            intent.putExtra(Intent.EXTRA_TEXT, newStatus);
                        else if (!Link.equals("") && !isRequest)
                            intent.putExtra(Intent.EXTRA_TEXT, "Link: " + Link);
                        if(isRequest)
                            intent.putExtra(Intent.EXTRA_TEXT,WhatsappText );
                        startActivity(Intent.createChooser(intent, "Share App..."));
                    }else {
                        Toast.makeText(HomeActivity.this,"Whatsapp Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }

        LinearLayout Instagram = (LinearLayout) dialog.findViewById(R.id.Instagram);
        if(Instagram!=null){
            if (ImageURL == null)
                Instagram.setVisibility(View.GONE);
            Instagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( isAppInstalled("com.instagram.android") ) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.setPackage("com.instagram.android");
                        if(ImageURL != null) {
                            Uri uri = ImageURL;
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                        }
                        if (!newStatus.equals("") && !Link.equals(""))
                            intent.putExtra(Intent.EXTRA_TEXT, newStatus + "\nLink: " + Link);
                        else if (!newStatus.equals(""))
                            intent.putExtra(Intent.EXTRA_TEXT, newStatus);
                        else if (!Link.equals(""))
                            intent.putExtra(Intent.EXTRA_TEXT, "Link: " + Link);
                        if(isRequest)
                            intent.putExtra(Intent.EXTRA_TEXT, "Download QuickBlood at: " + Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood")  +". And Start Donating Your Blood.");
                        startActivity(Intent.createChooser(intent, "Share App..."));
                    }else {
                        Toast.makeText(HomeActivity.this,"Instagram Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }
        LinearLayout Email = (LinearLayout) dialog.findViewById(R.id.Email);
        if(Email!=null){
            Email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("text/plain");
                    intent.setData(Uri.parse("mailto:"));
                    if(ImageURL != null) {
                        Uri uri = ImageURL;
                        intent.setType("image/*");
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    if (!newStatus.equals("") && !Link.equals(""))
                        intent.putExtra(Intent.EXTRA_TEXT, newStatus + "\nLink: " + Link);
                    else if (!newStatus.equals(""))
                        intent.putExtra(Intent.EXTRA_TEXT, newStatus);
                    else if (!Link.equals(""))
                        intent.putExtra(Intent.EXTRA_TEXT, "Link: " + Link);
                    if(isRequest) {
                        intent.putExtra(Intent.EXTRA_SUBJECT,"QuickBlood");
                        intent.putExtra(Intent.EXTRA_TEXT, "Download QuickBlood at: " + Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood") + ". And Start Donating Your Blood.");
                    }
                    startActivity(Intent.createChooser(intent, "Complete Action Using"));
                    dialog.dismiss();
                }
            });
        }
        LinearLayout Twitter = (LinearLayout) dialog.findViewById(R.id.Twitter);
        if(Twitter!=null){
            Twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( isAppInstalled("com.twitter.android") ) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.setPackage("com.twitter.android");
                        if(ImageURL != null) {
                            Uri uri = ImageURL;
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                        }
                        if (!newStatus.equals("") && !Link.equals(""))
                            intent.putExtra(Intent.EXTRA_TEXT, newStatus + "\nLink: " + Link);
                        else if (!newStatus.equals(""))
                            intent.putExtra(Intent.EXTRA_TEXT, newStatus);
                        else if (!Link.equals(""))
                            intent.putExtra(Intent.EXTRA_TEXT, "Link: " + Link);
                        if(isRequest)
                            intent.putExtra(Intent.EXTRA_TEXT, "Download QuickBlood at: " + Uri.parse("http://play.google.com/store/apps/details?id=com.tathva.falalurahman.quickblood")  +". And Start Donating Your Blood.");
                        startActivity(Intent.createChooser(intent, "Share App..."));
                    }else {
                        Toast.makeText(HomeActivity.this,"Twitter Is Not Installed In Your Mobile",Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(ImageURL != null){
                    File file = new File(ImageURL.getPath());
                    file.delete();
                }
            }
        });
        dialog.show();
    }

    private boolean isAppInstalled(String PackageName){
        PackageManager packageManager = getPackageManager();
        boolean appInstalled;
        try{
            packageManager.getPackageInfo(PackageName , PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        }catch (PackageManager.NameNotFoundException exception){
            appInstalled = false;
        }
        return appInstalled;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    void Tutorial(){
        SharedPreferences sharedPreferences = getSharedPreferences("QBTutorial",MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("Tutorial1",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .setTarget(new ViewTarget(R.id.bloodDonorButton, HomeActivity.this))
                    .setContentTitle("Blood Donors")
                    .setContentText("Register yourself and your friends as Blood Donors and start donating blood.")
                    .hideOnTouchOutside()
                    .build();
            RelativeLayout.LayoutParams ButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ButtonParams.setMargins(0, 0, convertdptopx(15),convertdptopx(15));
            showcaseView.setButtonPosition(ButtonParams);
            showcaseView.setStyle(R.style.CustomShowcaseTheme);
            Button ShowcaseButton = (Button) showcaseView.findViewById(R.id.showcase_button);
            ShowcaseButton.setBackground(getResources().getDrawable(R.drawable.button_background));
            showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    Tutorial();
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }
            });
            showcaseView.show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Tutorial1",true);
            editor.apply();
        }
        else if(!sharedPreferences.getBoolean("Tutorial2",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .setTarget(new ViewTarget(R.id.bloodDirectoryButton, HomeActivity.this))
                    .setContentTitle("Blood Directory")
                    .setContentText("Contact all Blood Donors in Kerala. To see yourself in blood directory register yourself as a public blood donor. If private, you will not be shown in blood directory")
                    .hideOnTouchOutside()
                    .build();
            RelativeLayout.LayoutParams ButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ButtonParams.setMargins(0, 0, convertdptopx(15),convertdptopx(15));
            showcaseView.setButtonPosition(ButtonParams);
            showcaseView.setStyle(R.style.CustomShowcaseTheme);
            Button ShowcaseButton = (Button) showcaseView.findViewById(R.id.showcase_button);
            ShowcaseButton.setBackground(getResources().getDrawable(R.drawable.button_background));
            showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    Tutorial();
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }
            });
            showcaseView.show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Tutorial2",true);
            editor.apply();

        }
        else if(!sharedPreferences.getBoolean("Tutorial3",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .setTarget(new ViewTarget(R.id.bloodRequestButton, HomeActivity.this))
                    .setContentTitle("Blood Request")
                    .setContentText("Request Blood Here. This Blood Request will be sent to all the Blood Donors in you respective district.")
                    .hideOnTouchOutside()
                    .build();
            RelativeLayout.LayoutParams ButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ButtonParams.setMargins(convertdptopx(15), 0, 0, convertdptopx(15));
            showcaseView.setButtonPosition(ButtonParams);
            showcaseView.setStyle(R.style.CustomShowcaseTheme);
            Button ShowcaseButton = (Button) showcaseView.findViewById(R.id.showcase_button);
            ShowcaseButton.setBackground(getResources().getDrawable(R.drawable.button_background));
            showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    Tutorial();
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }
            });
            showcaseView.show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Tutorial3",true);
            editor.apply();
        }
        else if(!sharedPreferences.getBoolean("Tutorial4",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .setTarget(new ViewTarget(R.id.notificationButton, HomeActivity.this))
                    .setContentTitle("Notification")
                    .setContentText("See all your notifications and blood requests here.")
                    .hideOnTouchOutside()
                    .build();
            RelativeLayout.LayoutParams ButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ButtonParams.setMargins(convertdptopx(15), 0, 0, convertdptopx(15));
            showcaseView.setButtonPosition(ButtonParams);
            showcaseView.setStyle(R.style.CustomShowcaseTheme);
            Button ShowcaseButton = (Button) showcaseView.findViewById(R.id.showcase_button);
            ShowcaseButton.setBackground(getResources().getDrawable(R.drawable.button_background));
            showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    Tutorial();
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }
            });
            showcaseView.show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Tutorial4",true);
            editor.apply();
        }
        else if(!sharedPreferences.getBoolean("Tutorial5",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .setTarget(new ViewTarget(R.id.aboutUsButton, HomeActivity.this))
                    .setContentTitle("About Us")
                    .setContentText("To know more about Tathva'16 and its social initiative Aavishkar, Visit Here.")
                    .hideOnTouchOutside()
                    .build();
            RelativeLayout.LayoutParams ButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ButtonParams.setMargins(convertdptopx(15), 0, 0, convertdptopx(15));
            showcaseView.setButtonPosition(ButtonParams);
            showcaseView.setStyle(R.style.CustomShowcaseTheme);
            Button ShowcaseButton = (Button) showcaseView.findViewById(R.id.showcase_button);
            ShowcaseButton.setBackground(getResources().getDrawable(R.drawable.button_background));
            showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    Tutorial();
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }
            });
            showcaseView.show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Tutorial5",true);
            editor.apply();
        }
    }


    public int convertdptopx(int dp) {
        Resources resources = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return px;
    }
}
