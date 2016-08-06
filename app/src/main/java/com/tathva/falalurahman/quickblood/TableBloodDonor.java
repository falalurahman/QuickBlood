package com.tathva.falalurahman.quickblood;

import android.database.sqlite.SQLiteDatabase;

public class TableBloodDonor {
    public static final String TABLE_NAME = "BloodDonors";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DATEOFBIRTH = "DateOfBirth";
    public static final String COLUMN_WEIGHT = "Weight";
    public static final String COLUMN_PHONENUMBER = "PhoneNumber";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_BLOODGROUP = "BloodGroup";
    public static final String COLUMN_DISTRICT = "District";
    public static final String COLUMN_ISUPLOADED = "isUploaded";
    public static final String COLUMN_ISPUBLIC = "isPublic";
    public static final String COLUMN_DEFAULT = "isDefault";
    public static final String COLUMN_BLOODDONATEDTIME = "BloodDonatedTime";

    public static void onCreate(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE "
                + TABLE_NAME
                + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DATEOFBIRTH + " TEXT NOT NULL, "
                + COLUMN_WEIGHT + " INT NOT NULL, "
                + COLUMN_PHONENUMBER + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_BLOODGROUP + " TEXT NOT NULL, "
                + COLUMN_DISTRICT + " TEXT NOT NULL, "
                + COLUMN_ISUPLOADED + " INT NOT NULL, "
                + COLUMN_ISPUBLIC + " INT NOT NULL, "
                + COLUMN_DEFAULT + " INT NOT NULL, "
                + COLUMN_BLOODDONATEDTIME + " TEXT NOT NULL "
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion , int newVersion){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
}
