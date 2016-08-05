package com.tathva.falalurahman.quickblood;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseContentProvider extends ContentProvider{

    private DatabaseHelper databaseHelper;

    private static final int QBPostList = 10;
    private static final int QBPostSingle = 20;
    private static final int QBPostLimit = 30;
    private static final int BloodDonorsList = 40;
    private static final int BloodDonorsSingle = 50;
    private static final int BloodDonorsLimit = 60;
    private static final int BloodDirectoryList = 70;
    private static final int BloodDirectorySpecial = 80;
    private static final int BloodRequestsList = 90;
    private static final int BloodRequestsSingle = 100;
    private static final int NotificationsList = 110;
    private static final int NotificationsSingle = 120;

    private static final String AUTHORITY = "com.tathva.falalurahman.quickblood.contentprovider";

    private static final String QBPOST_PATH = "QBPosts";
    private static final String BLOODDONORS_PATH = "BloodDonors";
    private static final String BLOODDIRECTORY_PATH = "BloodDirectory";
    private static final String BLOODREQUESTS_PATH = "BloodRequests";
    private static final String NOTIFICATIONS_PATH = "Notifications";

    public static final Uri QBPOST_URI = Uri.parse("content://" + AUTHORITY + "/" + QBPOST_PATH);
    public static final String QBPOST_MULTIPLE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/QBPosts";
    public static final String QBPOST_SINGLE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/QBPost";
    public static final Uri BLOODDONORS_URI = Uri.parse("content://" + AUTHORITY + "/" + BLOODDONORS_PATH);
    public static final String BLOODDONORS_MULTIPLE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/BloodDonors";
    public static final String BLOODDONORS_SINGLE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/BloodDonor";
    public static final Uri BLOODDIRECTORY_URI = Uri.parse("content://" + AUTHORITY + "/" + BLOODDIRECTORY_PATH);
    public static final String BLOODDIRECTORY_MULTIPLE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/BloodDirectory";
    public static final Uri BLOODREQUESTS_URI = Uri.parse("content://" + AUTHORITY + "/" + BLOODREQUESTS_PATH);
    public static final Uri NOTIFICATIONS_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATIONS_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,QBPOST_PATH,QBPostList);
        uriMatcher.addURI(AUTHORITY,QBPOST_PATH + "/#",QBPostSingle);
        uriMatcher.addURI(AUTHORITY,QBPOST_PATH + "/Limit/#",QBPostLimit);
        uriMatcher.addURI(AUTHORITY,BLOODDONORS_PATH,BloodDonorsList);
        uriMatcher.addURI(AUTHORITY,BLOODDONORS_PATH + "/*",BloodDonorsSingle);
        uriMatcher.addURI(AUTHORITY,BLOODDONORS_PATH + "/Limit/#",BloodDonorsLimit);
        uriMatcher.addURI(AUTHORITY,BLOODDIRECTORY_PATH,BloodDirectoryList);
        uriMatcher.addURI(AUTHORITY,BLOODDIRECTORY_PATH + "/*",BloodDirectorySpecial);
        uriMatcher.addURI(AUTHORITY,BLOODREQUESTS_PATH,BloodRequestsList);
        uriMatcher.addURI(AUTHORITY,BLOODREQUESTS_PATH + "/*",BloodRequestsSingle);
        uriMatcher.addURI(AUTHORITY,NOTIFICATIONS_PATH,NotificationsList);
        uriMatcher.addURI(AUTHORITY,NOTIFICATIONS_PATH + "/*",NotificationsSingle);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        int uriType = uriMatcher.match(uri);
        String limit = "";
        switch (uriType){
            case QBPostList:
                sqLiteQueryBuilder.setTables(TableQBPosts.TABLE_NAME);
                break;
            case QBPostSingle:
                sqLiteQueryBuilder.setTables(TableQBPosts.TABLE_NAME);
                sqLiteQueryBuilder.appendWhere(TableQBPosts.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case QBPostLimit:
                sqLiteQueryBuilder.setTables(TableQBPosts.TABLE_NAME);
                limit = uri.getLastPathSegment();
                break;
            case BloodDonorsList:
                sqLiteQueryBuilder.setTables(TableBloodDonor.TABLE_NAME);
                break;
            case BloodDonorsSingle:
                sqLiteQueryBuilder.setTables(TableBloodDonor.TABLE_NAME);
                sqLiteQueryBuilder.appendWhere(TableBloodDonor.COLUMN_PHONENUMBER + "='" + uri.getLastPathSegment() + "'");
                break;
            case BloodDonorsLimit:
                sqLiteQueryBuilder.setTables(TableBloodDonor.TABLE_NAME);
                limit = uri.getLastPathSegment();
                break;
            case BloodDirectoryList:
                sqLiteQueryBuilder.setTables(TableBloodDirectory.TABLE_NAME);
                break;
            case BloodDirectorySpecial:
                sqLiteQueryBuilder.setTables(TableBloodDirectory.TABLE_NAME);
                break;
            case BloodRequestsList:
                sqLiteQueryBuilder.setTables(TableBloodRequests.TABLE_NAME);
                break;
            case BloodRequestsSingle:
                sqLiteQueryBuilder.setTables(TableBloodRequests.TABLE_NAME);
                sqLiteQueryBuilder.appendWhere(TableBloodRequests.COLUMN_ID + "='" + uri.getLastPathSegment() + "'");
                break;
            case NotificationsList:
                sqLiteQueryBuilder.setTables(TableNotifications.TABLE_NAME);
                break;
            case NotificationsSingle:
                sqLiteQueryBuilder.setTables(TableNotifications.TABLE_NAME);
                sqLiteQueryBuilder.appendWhere(TableNotifications.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor;
        if(uriType != BloodDirectorySpecial)
            if( limit.equals(""))
                cursor = sqLiteQueryBuilder.query(database,projection,selection,selectionArgs,null,null,sortOrder);
            else
                cursor = sqLiteQueryBuilder.query(database,projection,selection,selectionArgs,null,null,sortOrder,limit);
        else{
            String bloodNeeded = uri.getLastPathSegment();
            ArrayList<Cursor> cursorArrayList = new ArrayList<>();
            if(bloodNeeded.charAt(0) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='A +veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='A +ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(1) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='A -veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='A -ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(2) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='B +veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='B +ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(3) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='B -veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='B -ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(4) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='O +veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='O +ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(5) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='O -veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='O -ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(6) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='AB +veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " +  TableBloodDirectory.COLUMN_BLOODGROUP + "='AB +ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            if(bloodNeeded.charAt(7) == '1'){
                cursor = sqLiteQueryBuilder.query(database,projection,TableBloodDirectory.COLUMN_BLOODGROUP + "='AB -veH'",null,null,null,null);
                cursorArrayList.add(cursor);
                cursor = sqLiteQueryBuilder.query(database,projection,selection + " AND " + TableBloodDirectory.COLUMN_BLOODGROUP + "='AB -ve'",null,null,null,TableBloodDirectory.COLUMN_NAME + " ASC");
                cursorArrayList.add(cursor);
            }
            Cursor[] cursors = cursorArrayList.toArray(new Cursor[cursorArrayList.size()]);
            for(int i=0; i<cursors.length ; i++){
                cursors[i].setNotificationUri(getContext().getContentResolver(),uri);
            }
            MergeCursor mergeCursor = new MergeCursor(cursors);
            return mergeCursor;
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = uriMatcher.match(uri);
        switch (uriType){
            case QBPostList:
                return QBPOST_MULTIPLE;
            case QBPostSingle:
                return QBPOST_SINGLE;
            case QBPostLimit:
                return QBPOST_MULTIPLE;
            case BloodDonorsList:
                return BLOODDONORS_MULTIPLE;
            case BloodDonorsSingle:
                return BLOODDONORS_SINGLE;
            case BloodDonorsLimit:
                return BLOODDONORS_MULTIPLE;
            case BloodDirectoryList:
                return BLOODDIRECTORY_MULTIPLE;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = null;
        long id = 0;
        switch (uriType){
            case QBPostList:
                id = database.insert(TableQBPosts.TABLE_NAME,null,values);
                break;
            case BloodDonorsList:
                cursor = query(Uri.parse(BLOODDONORS_URI + "/" + values.get("PhoneNumber")),new String[]{TableBloodDonor.COLUMN_PHONENUMBER}
                        ,null,null,null);
                if(cursor == null || cursor.getCount() == 0){
                    id = database.insert(TableBloodDonor.TABLE_NAME,null,values);
                }else {
                    update(Uri.parse(BLOODDONORS_URI + "/" + values.get("PhoneNumber")),values,null,null);
                }
                if(cursor!=null)
                    cursor.close();
                break;
            case BloodDirectoryList:
                id = database.insert(TableBloodDirectory.TABLE_NAME,null,values);
                break;
            case BloodRequestsList:
                if(values.getAsInteger(TableBloodRequests.COLUMN_ID) != null) {
                    cursor = query(Uri.parse(BLOODREQUESTS_URI + "/" + Integer.toString(values.getAsInteger(TableBloodRequests.COLUMN_ID))), new String[]{TableBloodDonor.COLUMN_PHONENUMBER}
                            , null, null, null);
                }
                if(cursor == null || cursor.getCount() == 0){
                    id = database.insert(TableBloodRequests.TABLE_NAME,null,values);
                }else {
                    update(Uri.parse(BLOODREQUESTS_URI + "/" + Integer.toString(values.getAsInteger(TableBloodRequests.COLUMN_ID))),values,null,null);
                }
                if(cursor!=null)
                    cursor.close();
                break;
            case NotificationsList:
                id = database.insert(TableNotifications.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(QBPOST_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType){
            case QBPostList:
                rowsDeleted = database.delete(TableQBPosts.TABLE_NAME,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case QBPostSingle:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection))
                    rowsDeleted = database.delete(TableQBPosts.TABLE_NAME, TableQBPosts.COLUMN_ID + "='" + id  + "'",null);
                else
                    rowsDeleted = database.delete(TableQBPosts.TABLE_NAME, TableQBPosts.COLUMN_ID + "='" + id + "'"
                            + " and " + selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case QBPostLimit:
                id = uri.getLastPathSegment();
                rowsDeleted = database.delete(TableQBPosts.TABLE_NAME, TableQBPosts.COLUMN_ID + " NOT IN " +
                        " ( SELECT " + TableQBPosts.COLUMN_ID + " FROM " + TableQBPosts.TABLE_NAME + " ORDER BY " +
                        TableQBPosts.COLUMN_TIMESTAMP + " DESC LIMIT " + id + " )", null);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodDonorsList:
                rowsDeleted = database.delete(TableBloodDonor.TABLE_NAME,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodDonorsSingle:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection))
                    rowsDeleted = database.delete(TableBloodDonor.TABLE_NAME, TableBloodDonor.COLUMN_PHONENUMBER + "='" + id + "'" ,null);
                else
                    rowsDeleted = database.delete(TableBloodDonor.TABLE_NAME, TableBloodDonor.COLUMN_PHONENUMBER + "='" + id + "'"
                            + " and " + selection,selectionArgs);
                break;
            case BloodDirectoryList:
                rowsDeleted = database.delete(TableBloodDirectory.TABLE_NAME,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodRequestsList:
                rowsDeleted = database.delete(TableBloodRequests.TABLE_NAME,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodRequestsSingle:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection))
                    rowsDeleted = database.delete(TableBloodRequests.TABLE_NAME, TableBloodDonor.COLUMN_ID + "=" + id ,null);
                else
                    rowsDeleted = database.delete(TableBloodRequests.TABLE_NAME, TableBloodDonor.COLUMN_ID + "=" + id
                            + " and " + selection,selectionArgs);
                break;
            case NotificationsSingle:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection))
                    rowsDeleted = database.delete(TableNotifications.TABLE_NAME, TableNotifications.COLUMN_ID + "=" + id ,null);
                else
                    rowsDeleted = database.delete(TableNotifications.TABLE_NAME, TableNotifications.COLUMN_ID + "=" + id
                            + " and " + selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType){
            case BloodDonorsList:
                rowsUpdated = database.update(TableBloodDonor.TABLE_NAME,values,null,null);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodDonorsSingle:
                id = uri.getLastPathSegment();
                rowsUpdated = database.update(TableBloodDonor.TABLE_NAME,values,TableBloodDonor.COLUMN_PHONENUMBER + "='" + id + "'",null);
                if(values.containsKey("isUploaded")) {
                    if (values.getAsInteger("isUploaded") != 1) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                }
                else
                        getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodRequestsList:
                rowsUpdated = database.update(TableBloodRequests.TABLE_NAME,values,null,null);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case BloodRequestsSingle:
                id = uri.getLastPathSegment();
                rowsUpdated = database.update(TableBloodRequests.TABLE_NAME,values,TableBloodDonor.COLUMN_ID + "=" + id,null);
                if(values.containsKey("isUploaded")) {
                    if (values.getAsInteger("isUploaded") != 1) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                }
                else
                    getContext().getContentResolver().notifyChange(uri,null);
                break;
            case NotificationsList:
                rowsUpdated = database.update(TableNotifications.TABLE_NAME,values,null,null);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsUpdated;
    }
}
