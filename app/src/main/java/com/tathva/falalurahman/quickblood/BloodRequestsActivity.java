package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import java.util.Objects;

public class BloodRequestsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_requests);
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
        navigationView.setCheckedItem(R.id.nav_blood_requests);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        if(getIntent().getBooleanExtra("newRequest",false)){
            mViewPager.setCurrentItem(1);
        }else {
            mViewPager.setCurrentItem(0);
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    protected void onPostResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_blood_requests);
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
            intent.putExtra("PreviousActivity", "BloodRequestsActivity");
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
            navigationView.setNavigationItemSelectedListener(BloodRequestsActivity.this);
            Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin",false);
                    editor.apply();
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_admin_login).setVisible(true);
                    navigationView.setCheckedItem(R.id.nav_blood_requests);
                    dialog.dismiss();
                }
            });
            TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
            Cancel.setText("NO");
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationView.setCheckedItem(R.id.nav_blood_requests);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //To Show Fragment At the desired position
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new NewRequestTab();
                    break;
                case 1:
                    fragment = new MyRequestTab();
                    break;
            }
            return fragment;
        }

        // Show 2 total pages.
        @Override
        public int getCount() {
            return 2;
        }

        //Show Page Titles
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "New Blood Request";
                case 1:
                    return "My Blood Requests";
            }
            return null;
        }
    }

    public static class NewRequestTab extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View customView = inflater.inflate(R.layout.fragment_tab1,container,false);
            ListView listView = (ListView) customView.findViewById(R.id.listview1);
            LayoutInflater layoutInflater = getLayoutInflater(null);
            ViewGroup headerNewPost = (ViewGroup) layoutInflater.inflate(R.layout.header_new_request,listView,false);
            listView.addHeaderView(headerNewPost,null,true);
            headerNewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),BloodDetails.class);
                    intent.putExtra("Name","");
                    intent.putExtra("PhoneNumber","");
                    intent.putExtra("Email","");
                    startActivity(intent);
                }
            });
            final NewBloodRequestAdapter newBloodRequestAdapter = new NewBloodRequestAdapter(getContext(),null);
            listView.setAdapter(newBloodRequestAdapter);

            LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    if(id == 0) {
                        String[] projection = {TableBloodDonor.COLUMN_ID, TableBloodDonor.COLUMN_NAME, TableBloodDonor.COLUMN_PHONENUMBER, TableBloodDonor.COLUMN_EMAIL,TableBloodDonor.COLUMN_BLOODGROUP};
                        CursorLoader cursorLoader = new CursorLoader(getActivity(), DatabaseContentProvider.BLOODDONORS_URI, projection, TableBloodDonor.COLUMN_ISUPLOADED + "!=2", null, null);
                        return cursorLoader;
                    }else {
                        return null;
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    newBloodRequestAdapter.changeCursor(data);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    newBloodRequestAdapter.changeCursor(null);
                }
            };
            getActivity().getLoaderManager().initLoader(0,null,loaderCallbacks);
            return customView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if(isVisibleToUser)
                Tutorial();
        }

        void Tutorial(){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("QBTutorial",MODE_PRIVATE);
            if (!sharedPreferences.getBoolean("Tutorial10",false)){
                ShowcaseView showcaseView = new ShowcaseView.Builder(getActivity())
                        .setTarget(Target.NONE)
                        .setContentTitle("New Blood Request")
                        .setContentText("Create a new Blood Request by clicking any of the Blood Donors or by clicking New Contact Address if address is not known.")
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
                editor.putBoolean("Tutorial10",true);
                editor.apply();
            }
        }

        public int convertdptopx(int dp) {
            Resources resources = getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
            return px;
        }
    }

    public static class MyRequestTab extends Fragment{

        Cursor cursor = null;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View customView = inflater.inflate(R.layout.fragment_tab2,container,false);
            ListView listView = (ListView) customView.findViewById(R.id.listview2);
            final MyBloodRequestAdapter myBloodRequestAdapter = new MyBloodRequestAdapter(getContext(),null);
            final SwipeActionAdapter swipeActionAdapter = new SwipeActionAdapter(myBloodRequestAdapter);
            swipeActionAdapter.setListView(listView);
            listView.setAdapter(swipeActionAdapter);

            LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    if( id == 1 ) {
                        CursorLoader cursorLoader = new CursorLoader(getActivity(), DatabaseContentProvider.BLOODREQUESTS_URI, null, TableBloodRequests.COLUMN_ISUPLOADED + "!=2", null, TableBloodRequests.COLUMN_ID + " DESC");
                        return cursorLoader;
                    }else {
                        return null;
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    if(cursor != null){
                        cursor.close();
                        cursor = null;
                    }
                    myBloodRequestAdapter.changeCursor(data);
                    swipeActionAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    myBloodRequestAdapter.changeCursor(null);
                    swipeActionAdapter.notifyDataSetChanged();
                }
            };
            getActivity().getLoaderManager().initLoader(1,null,loaderCallbacks);

            swipeActionAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
                @Override
                public boolean hasActions(int position, SwipeDirection direction) {
                    return direction.isLeft();
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
                        cursor = myBloodRequestAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int id = cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_ID));
                        switch (direction){
                            case DIRECTION_FAR_LEFT:
                            case DIRECTION_NORMAL_LEFT:
                                CursorDeleteProxy cursorDeleteProxy = new CursorDeleteProxy(cursor,position);
                                myBloodRequestAdapter.swapCursor(cursorDeleteProxy);
                                final ContentValues contentValues = new ContentValues();
                                contentValues.put("isUploaded",2);
                                getContext().getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODREQUESTS_URI + "/" + id),contentValues,null,null);
                                contentValues.put(TableBloodRequests.COLUMN_ID,cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_ID)));
                                contentValues.put(TableBloodRequests.COLUMN_TIMESTAMP,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_TIMESTAMP)));
                                contentValues.put(TableBloodRequests.COLUMN_NAME,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_NAME)));
                                contentValues.put(TableBloodRequests.COLUMN_PHONENUMBER,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_PHONENUMBER)));
                                contentValues.put(TableBloodRequests.COLUMN_EMAIL,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_EMAIL)));
                                contentValues.put(TableBloodRequests.COLUMN_BLOODGROUP,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_BLOODGROUP)));
                                contentValues.put(TableBloodRequests.COLUMN_DISTRICT,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_DISTRICT)));
                                contentValues.put(TableBloodRequests.COLUMN_VOLUME,cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_VOLUME)));
                                contentValues.put(TableBloodRequests.COLUMN_ADDRESS,cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_ADDRESS)));
                                contentValues.put(TableBloodRequests.COLUMN_ISUPLOADED,0);
                                Snackbar.make(customView.findViewById(R.id.full_page),"Blood Request Deleted",Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getContext().getContentResolver().insert(DatabaseContentProvider.BLOODREQUESTS_URI,contentValues);
                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                        .show();
                                break;
                        }
                        swipeActionAdapter.notifyDataSetChanged();
                    }
                }
            });
            return customView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if(isVisibleToUser)
                Tutorial();
        }

        void Tutorial(){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("QBTutorial",MODE_PRIVATE);
            if (!sharedPreferences.getBoolean("Tutorial11",false)){
                ShowcaseView showcaseView = new ShowcaseView.Builder(getActivity())
                        .setTarget(Target.NONE)
                        .setContentTitle("My Blood Requests")
                        .setContentText("All your Blood Requests can be seen here. If you do not want any Blood Request anymore, \n Slide Left to Delete that Blood Request.")
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
                editor.putBoolean("Tutorial11",true);
                editor.apply();
            }
        }

        public int convertdptopx(int dp) {
            Resources resources = getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
            return px;
        }

        public class CursorDeleteProxy extends AbstractCursor{
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this,UploadRequestService.class);
        startService(intent);
    }
}
