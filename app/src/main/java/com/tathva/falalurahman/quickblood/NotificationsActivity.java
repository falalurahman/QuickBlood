package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    NotificationsAdapter notificationsAdapter;
    SwipeActionAdapter swipeActionAdapter;
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    SwipeRefreshLayout swipeRefreshLayout;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
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
        navigationView.setCheckedItem(R.id.nav_notification);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("NotificationCount",0);
        editor.apply();
        ShortcutBadger.removeCount(NotificationsActivity.this);

        listView = (ListView) findViewById(R.id.listview);
        notificationsAdapter = new NotificationsAdapter(this,null);
        swipeActionAdapter = new SwipeActionAdapter(notificationsAdapter);
        swipeActionAdapter.setListView(listView);
        listView.setAdapter(swipeActionAdapter);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = new CursorLoader(NotificationsActivity.this,DatabaseContentProvider.NOTIFICATIONS_URI,null,null ,null,TableNotifications.COLUMN_ID + " DESC");
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(cursor != null){
                    cursor.close();
                    cursor = null;
                }
                notificationsAdapter.changeCursor(data);
                swipeActionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                notificationsAdapter.changeCursor(null);
                swipeActionAdapter.notifyDataSetChanged();
            }
        };
        getLoaderManager().initLoader(0,null,loaderCallbacks);

        swipeActionAdapter.addBackground(SwipeDirection.DIRECTION_FAR_RIGHT,R.layout.background_message)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT,R.layout.background_message);

        swipeActionAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int position, SwipeDirection direction) {
                return direction.isLeft() || direction.isRight();
            }

            @Override
            public boolean shouldDismiss(int position, SwipeDirection direction) {
                return (direction == SwipeDirection.DIRECTION_NORMAL_LEFT || direction == SwipeDirection.DIRECTION_FAR_LEFT);
            }

            @Override
            public void onSwipe(int[] positions, SwipeDirection[] directions) {
                for (int i=0;i<positions.length ; i++){
                    SwipeDirection direction = directions[i];
                    int position = positions[i];
                    cursor = notificationsAdapter.getCursor();
                    cursor.moveToPosition(position);
                    int id = cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_ID));
                    String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_PHONENUMBER));
                    switch (direction){
                        case DIRECTION_FAR_LEFT:
                        case DIRECTION_NORMAL_LEFT:
                            CursorDeleteProxy cursorDeleteProxy = new CursorDeleteProxy(cursor,position);
                            notificationsAdapter.swapCursor(cursorDeleteProxy);
                            getContentResolver().delete(Uri.parse(DatabaseContentProvider.NOTIFICATIONS_URI + "/" + id),null,null);
                            break;
                        case DIRECTION_FAR_RIGHT:
                        case  DIRECTION_NORMAL_RIGHT:
                            sendSMS(PhoneNumber);
                    }
                    swipeActionAdapter.notifyDataSetChanged();
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(NotificationsActivity.this, DownloadRequestService.class);
                startService(intent);
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                };

                //Waiting For 3 seconds in another thread
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        long futureTime = System.currentTimeMillis() + 5000;
                        while (System.currentTimeMillis() < futureTime){
                            synchronized (this){
                                try{
                                    wait(futureTime - System.currentTimeMillis());
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
        });
        Tutorial();
    }

    public class CursorDeleteProxy extends AbstractCursor {
        private Cursor cursor;
        private int posToIgnore;

        public CursorDeleteProxy(Cursor cursor, int posToIgnore) {
            this.cursor = cursor;
            this.posToIgnore = posToIgnore;
        }

        @Override
        public boolean onMove(int oldPosition, int newPosition) {
            if(newPosition < posToIgnore){
                cursor.moveToPosition(newPosition);
            }else {
                cursor.moveToPosition(newPosition+1);
            }
            return true;
        }

        @Override
        public int getCount() {
            return cursor.getCount()-1;
        }

        @Override
        public String[] getColumnNames() {
            return cursor.getColumnNames();
        }

        @Override
        public String getString(int column) {
            return cursor.getString(column);
        }

        @Override
        public short getShort(int column) {
            return cursor.getShort(column);
        }

        @Override
        public int getInt(int column) {
            return cursor.getInt(column);
        }

        @Override
        public long getLong(int column) {
            return cursor.getLong(column);
        }

        @Override
        public float getFloat(int column) {
            return cursor.getFloat(column);
        }

        @Override
        public double getDouble(int column) {
            return cursor.getDouble(column);
        }

        @Override
        public int getColumnIndex(String columnName) {
            return cursor.getColumnIndex(columnName);
        }

        @Override
        public boolean isNull(int column) {
            return cursor.isNull(column);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSeen",1);
        getContentResolver().update(DatabaseContentProvider.NOTIFICATIONS_URI,contentValues,null,null);
    }

    private void sendSMS(String PhoneNumber){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("text/plain");
        intent.setData(Uri.parse("sms:" + PhoneNumber));
        startActivity(Intent.createChooser(intent, "Complete Action Using"));
    }

    @Override
    protected void onPostResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_notification);
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
            intent.putExtra("PreviousActivity", "NotificationsActivity");
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
            navigationView.setNavigationItemSelectedListener(NotificationsActivity.this);
            Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin",false);
                    editor.apply();
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
                    navigationView.setCheckedItem(R.id.nav_notification);
                    dialog.dismiss();
                }
            });
            TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
            Cancel.setText("NO");
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationView.setCheckedItem(R.id.nav_notification);
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

    void Tutorial(){
        SharedPreferences sharedPreferences = getSharedPreferences("QBTutorial",MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("Tutorial12",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(NotificationsActivity.this)
                    .setTarget(Target.NONE)
                    .setContentTitle("Notification")
                    .setContentText("All Notifications and Blood Requests can be seen here. \nClick on the notification to call the person who has requested blood.\nSlide Right to Message the requesting person.\n Slide Left To Delete the Notification.")
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
            editor.putBoolean("Tutorial12",true);
            editor.apply();
        }
    }

    public int convertdptopx(int dp) {
        Resources resources = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return px;
    }
}
