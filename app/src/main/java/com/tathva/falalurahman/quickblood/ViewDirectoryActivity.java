package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

public class ViewDirectoryActivity extends AppCompatActivity{

    ListView listView;
    BloodDirectoryAdapter bloodDirectoryAdapter;
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean isAp = true, isAn = true, isBp = true, isBn = true, isOp = true, isOn = true, isABp = true, isABn = true;
    public Context context = this;
    LinearLayout FilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_directory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String District = getIntent().getStringExtra("District");

        listView = (ListView) findViewById(R.id.listview);
        bloodDirectoryAdapter = new BloodDirectoryAdapter(this,null);
        final SwipeActionAdapter swipeActionAdapter = new SwipeActionAdapter(bloodDirectoryAdapter);
        swipeActionAdapter.setListView(listView);
        listView.setAdapter(swipeActionAdapter);
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projections = new String[]{TableBloodDirectory.COLUMN_ID,TableBloodDirectory.COLUMN_NAME,TableBloodDirectory.COLUMN_PHONENUMBER, TableBloodDirectory.COLUMN_EMAIL};
                String BloodNeeded = "";
                if(isAp){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isAn){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isBp){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isBn){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isOp){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isOn){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isABp){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                if(isABn){
                    BloodNeeded = BloodNeeded.concat("1");
                }else {
                    BloodNeeded = BloodNeeded.concat("0");
                }
                CursorLoader cursorLoader = new CursorLoader(ViewDirectoryActivity.this,
                        Uri.parse(DatabaseContentProvider.BLOODDIRECTORY_URI + "/" + BloodNeeded), projections,TableBloodDirectory.COLUMN_DISTRICT + "='" + District +"'",null,null);
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                listView.setEmptyView(listView);
                bloodDirectoryAdapter.changeCursor(data);
                swipeActionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                bloodDirectoryAdapter.changeCursor(null);
                swipeActionAdapter.notifyDataSetChanged();
            }
        };
        getLoaderManager().initLoader(0,null,loaderCallbacks);

        swipeActionAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT,R.layout.background_message)
                .addBackground(SwipeDirection.DIRECTION_FAR_RIGHT,R.layout.background_email)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT,R.layout.background_message)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT,R.layout.background_email);

        swipeActionAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int position, SwipeDirection direction) {
                Cursor cursor = bloodDirectoryAdapter.getCursor();
                cursor.moveToPosition(position);
                if(direction.isLeft() && !cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_PHONENUMBER)).equals("1111"))
                    return true;
                if(direction.isRight() && !cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_PHONENUMBER)).equals("1111")
                        && !cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_EMAIL)).equals(""))
                    return true;
                return false;
            }

            @Override
            public boolean shouldDismiss(int position, SwipeDirection direction) {
                return false;
            }

            @Override
            public void onSwipe(int[] positions, SwipeDirection[] directions) {
                for (int i=0;i<positions.length ; i++){
                    SwipeDirection direction = directions[i];
                    int position = positions[i];
                    Cursor cursor = bloodDirectoryAdapter.getCursor();
                    cursor.moveToPosition(position);
                    switch (direction){
                        case DIRECTION_FAR_LEFT:
                        case DIRECTION_NORMAL_LEFT:
                            sendSMS(cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_PHONENUMBER)));
                            break;
                        case DIRECTION_FAR_RIGHT:
                        case DIRECTION_NORMAL_RIGHT:
                            sendEmail(cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_EMAIL)));
                            break;
                    }
                    swipeActionAdapter.notifyDataSetChanged();
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(ViewDirectoryActivity.this,DownloadDirectoryService.class);
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

        FilterButton = (LinearLayout) findViewById(R.id.filterButton);
        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ViewDirectoryActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_filter);

                final CheckBox Ap = (CheckBox) dialog.findViewById(R.id.Ap);
                final CheckBox An = (CheckBox) dialog.findViewById(R.id.An);
                final CheckBox Bp = (CheckBox) dialog.findViewById(R.id.Bp);
                final CheckBox Bn = (CheckBox) dialog.findViewById(R.id.Bn);
                final CheckBox Op = (CheckBox) dialog.findViewById(R.id.Op);
                final CheckBox On = (CheckBox) dialog.findViewById(R.id.On);
                final CheckBox ABp = (CheckBox) dialog.findViewById(R.id.ABp);
                final CheckBox ABn = (CheckBox) dialog.findViewById(R.id.ABn);

                if(isAp){
                    Ap.setChecked(true);
                }else {
                    Ap.setChecked(false);
                }
                if(isAn){
                    An.setChecked(true);
                }else {
                    An.setChecked(false);
                }
                if(isBp){
                    Bp.setChecked(true);
                }else {
                    Bp.setChecked(false);
                }
                if(isBn){
                    Bn.setChecked(true);
                }else {
                    Bn.setChecked(false);
                }
                if(isOp){
                    Op.setChecked(true);
                }else {
                    Op.setChecked(false);
                }
                if(isOn){
                    On.setChecked(true);
                }else {
                    On.setChecked(false);
                }if(isABp){
                    ABp.setChecked(true);
                }else {
                    ABp.setChecked(false);
                }
                if(isABn){
                    ABn.setChecked(true);
                }else {
                    ABn.setChecked(false);
                }


                TextView Filter = (TextView) dialog.findViewById(R.id.Filter);
                Filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAp = Ap.isChecked();
                        isAn = An.isChecked();
                        isBp = Bp.isChecked();
                        isBn = Bn.isChecked();
                        isOp = Op.isChecked();
                        isOn = On.isChecked();
                        isABp = ABp.isChecked();
                        isABn = ABn.isChecked();
                        bloodDirectoryAdapter.changeCursor(null);
                        getLoaderManager().restartLoader(0,null,loaderCallbacks);
                        dialog.dismiss();
                    }
                });
                TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        Tutorial();
    }

    private void sendSMS(String PhoneNumber){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("text/plain");
        intent.setData(Uri.parse("sms:" + PhoneNumber));
        startActivity(Intent.createChooser(intent, "Complete Action Using"));
    }

    private void sendEmail(String Email){
        String[] Emails = new String[]{Email};
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,Emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,"QuickBlood");
        startActivity(Intent.createChooser(intent, "Complete Action Using"));
    }

    void Tutorial(){
        SharedPreferences sharedPreferences = getSharedPreferences("QBTutorial",MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("Tutorial8",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(ViewDirectoryActivity.this)
                    .setTarget(Target.NONE)
                    .setContentTitle("Blood Directory")
                    .setContentText("Contact all Blood Donors in Kerala here. \nSlide a contact Right to send Email.\n Slide left to Message")
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
            editor.putBoolean("Tutorial8",true);
            editor.apply();
        }else if (!sharedPreferences.getBoolean("Tutorial9",false)){
            ShowcaseView showcaseView = new ShowcaseView.Builder(ViewDirectoryActivity.this)
                    .setTarget(new ViewTarget(R.id.filterButton, ViewDirectoryActivity.this))
                    .setContentTitle("Filter Blood Directory")
                    .setContentText("Click Filter Button to show only the Blood Donors with specific Blood Groups")
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
            editor.putBoolean("Tutorial9",true);
            editor.apply();
        }
    }

    public int convertdptopx(int dp) {
        Resources resources = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return px;
    }
}
