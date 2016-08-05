package com.tathva.falalurahman.quickblood;

import android.database.sqlite.SQLiteDatabase;

public class TableQBPosts {
    public static final String TABLE_NAME = "QBPosts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_TIMESTAMP = "TimeStamp";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_LINK = "Link";
    public static final String COLUMN_IMAGE = "Image";

    public static void onCreate(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE "
                + TABLE_NAME
                + "( "
                + COLUMN_ID + " INT PRIMARY KEY NOT NULL, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_TIMESTAMP + " TEXT NOT NULL, "
                + COLUMN_STATUS + " TEXT, "
                + COLUMN_LINK + " TEXT, "
                + COLUMN_IMAGE + " TEXT"
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion , int newVersion){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
}
