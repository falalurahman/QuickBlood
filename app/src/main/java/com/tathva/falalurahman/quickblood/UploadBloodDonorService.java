package com.tathva.falalurahman.quickblood;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

public class UploadBloodDonorService extends IntentService {

    Cursor cursor;

    public UploadBloodDonorService() {
        super("UploadBloodDonorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        cursor = getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,null,
                TableBloodDonor.COLUMN_ISUPLOADED + "=0",null,null);
        if(cursor != null) {
            while (cursor.moveToNext()){
                final String Name = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME));
                final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER));
                final String Email = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_EMAIL));
                final String BloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP));
                final String District = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT));
                final String isPublic = Integer.toString(cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ISPUBLIC)));
                final String BloodDonatedTime = Integer.toString(cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODDONATEDTIME)));
                Log.i("QB",Name);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.upload_blood_donor_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Success")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("isUploaded", 1);
                                    getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" + PhoneNumber),
                                            contentValues, null, null);
                                }else {
                                    Intent startIntent = new Intent(UploadBloodDonorService.this,UploadBloodDonorService.class);
                                    PendingIntent pendingIntent = PendingIntent.getService(UploadBloodDonorService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
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
                                Intent startIntent = new Intent(UploadBloodDonorService.this,UploadBloodDonorService.class);
                                PendingIntent pendingIntent = PendingIntent.getService(UploadBloodDonorService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                long nextUpdateTime = System.currentTimeMillis();
                                nextUpdateTime += 120000;
                                alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<>();
                        params.put("Name",Name);
                        params.put("PhoneNumber",PhoneNumber);
                        params.put("Email", Email);
                        params.put("BloodGroup",BloodGroup);
                        params.put("District",District);
                        params.put("isPublic",isPublic);
                        params.put("BloodDonatedTime",BloodDonatedTime);
                        return params;
                    }
                };
                SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
            }
            cursor.close();
        }
        cursor = getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,null,
                TableBloodDonor.COLUMN_ISUPLOADED + "=2",null,null);
        if(cursor != null) {
            while (cursor.moveToNext()){
                final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER));
                StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.delete_blood_donor_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Success")) {
                                    getContentResolver().delete(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" + PhoneNumber),
                                            null, null);
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
                        params.put("PhoneNumber",PhoneNumber);
                        return params;
                    }
                };
                SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
            }
            cursor.close();
        }
    }
}
