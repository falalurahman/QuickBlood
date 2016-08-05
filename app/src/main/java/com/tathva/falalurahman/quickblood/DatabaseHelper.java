package com.tathva.falalurahman.quickblood;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "QuickBlood.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TableQBPosts.onCreate(sqLiteDatabase);
        TableBloodDonor.onCreate(sqLiteDatabase);
        TableBloodDirectory.onCreate(sqLiteDatabase);
        TableBloodRequests.onCreate(sqLiteDatabase);
        TableNotifications.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        TableQBPosts.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        TableBloodDonor.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        TableBloodDirectory.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        TableBloodRequests.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        TableNotifications.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
    }
}
