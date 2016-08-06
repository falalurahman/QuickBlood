package com.tathva.falalurahman.quickblood;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class TableBloodDirectory {
    public static final String TABLE_NAME = "BloodDirectory";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PHONENUMBER = "PhoneNumber";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_BLOODGROUP = "BloodGroup";
    public static final String COLUMN_DISTRICT = "District";

    public static void onCreate(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE "
                + TABLE_NAME
                + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PHONENUMBER + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_BLOODGROUP + " TEXT NOT NULL, "
                + COLUMN_DISTRICT + " TEXT NOT NULL "
                + ");";
        sqLiteDatabase.execSQL(query);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"A +ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"A +veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"A -ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"A -veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"B +ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"B +veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"B -ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"B -veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"O +ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"O +veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"O -ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"O -veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"AB +ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"AB +veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
        contentValues.put(TableBloodDirectory.COLUMN_NAME,"AB -ve Blood Group");
        contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER,"1111");
        contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP,"AB -veH");
        contentValues.put(TableBloodDirectory.COLUMN_DISTRICT,"ALL");
        sqLiteDatabase.insert(TableBloodDirectory.TABLE_NAME,null,contentValues);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion , int newVersion){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
}
