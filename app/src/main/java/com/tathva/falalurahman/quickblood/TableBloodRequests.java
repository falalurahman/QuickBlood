package com.tathva.falalurahman.quickblood;

import android.database.sqlite.SQLiteDatabase;

public class TableBloodRequests {

    public static final String TABLE_NAME = "BloodRequests";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "TimeStamp";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PHONENUMBER = "PhoneNumber";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_BLOODGROUP = "BloodGroup";
    public static final String COLUMN_DISTRICT = "District";
    public static final String COLUMN_VOLUME = "Volume";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_ISUPLOADED = "isUploaded";

    public static void onCreate(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE "
                + TABLE_NAME
                + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_TIMESTAMP + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PHONENUMBER + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_BLOODGROUP + " TEXT NOT NULL, "
                + COLUMN_DISTRICT + " TEXT NOT NULL, "
                + COLUMN_VOLUME + " INT NOT NULL, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_ISUPLOADED + " INT NOT NULL "
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion , int newVersion){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
}
