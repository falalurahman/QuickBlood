package com.tathva.falalurahman.quickblood;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Hashtable;
import java.util.Map;

public class UploadRequestService extends IntentService {

    Cursor cursor;

    public UploadRequestService() {
        super("UploadRequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        cursor = getContentResolver().query(DatabaseContentProvider.BLOODREQUESTS_URI,null,
                TableBloodRequests.COLUMN_ISUPLOADED + "=0",null,null);
        if(cursor != null) {
            while (cursor.moveToNext()){
                final int id = cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_ID));
                final String TimeStamp = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_TIMESTAMP));
                final String Name = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_NAME));
                final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_PHONENUMBER));
                final String Email = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_EMAIL));
                final String BloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_BLOODGROUP));
                final String District = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_DISTRICT));
                final String Address = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_ADDRESS));
                final String Details = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_OTHERDETAILS));
                final int Volume = cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_VOLUME));
                StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.upload_blood_request_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Success")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("isUploaded", 1);
                                    getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODREQUESTS_URI + "/" + id),
                                            contentValues, null, null);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<>();
                        params.put("TimeStamp",TimeStamp);
                        params.put("Name",Name);
                        params.put("PhoneNumber",PhoneNumber);
                        params.put("Email", Email);
                        params.put("BloodGroup",BloodGroup);
                        params.put("District",District);
                        params.put("Address", Address);
                        params.put("OtherDetails",Details);
                        params.put("Volume",Integer.toString(Volume));
                        return params;
                    }
                };
                SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
            }
            cursor.close();
        }
        cursor = getContentResolver().query(DatabaseContentProvider.BLOODREQUESTS_URI,new String[]{TableBloodRequests.COLUMN_ID,TableBloodRequests.COLUMN_TIMESTAMP},
                TableBloodRequests.COLUMN_ISUPLOADED + "=2",null,null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                final int id = cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_ID));
                final String TimeStamp = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_TIMESTAMP));
                StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.delete_blood_request_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Success")) {
                                    getContentResolver().delete(Uri.parse(DatabaseContentProvider.BLOODREQUESTS_URI + "/" + Integer.toString(id)),
                                            null, null);
                                }else {
                                    Intent startIntent = new Intent(UploadRequestService.this,UploadRequestService.class);
                                    PendingIntent pendingIntent = PendingIntent.getService(UploadRequestService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    long nextUpdateTime = System.currentTimeMillis();
                                    nextUpdateTime += 120000;
                                    alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Intent startIntent = new Intent(UploadRequestService.this,UploadRequestService.class);
                                PendingIntent pendingIntent = PendingIntent.getService(UploadRequestService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                long nextUpdateTime = System.currentTimeMillis();
                                nextUpdateTime += 120000;
                                alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<>();
                        params.put("TimeStamp", TimeStamp);
                        return params;
                    }
                };
                SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
            }
            cursor.close();
        }
    }
}
