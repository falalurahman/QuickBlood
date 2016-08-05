package com.tathva.falalurahman.quickblood;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class DownloadDirectoryService extends IntentService {

    long StartDownload, LastDownloadTime;

    public DownloadDirectoryService() {
        super("DownloadDirectoryService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        StartDownload = sharedPreferences.getLong("BloodDirectoryLastUpdated",0);
        LastDownloadTime = sharedPreferences.getLong("BloodDirectoryUpdateTime",0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(LastDownloadTime);
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DAY_OF_YEAR,-1);
        if(calendar.compareTo(nowTime) < 0)
            DownloadData();
    }

    public void DownloadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.get_donors_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            long LastDownload = sharedPreferences.getLong("BloodDirectoryLastUpdated",8);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(TableBloodDirectory.COLUMN_ID, jsonObject.getLong("DonorId"));
                                contentValues.put(TableBloodDirectory.COLUMN_NAME, jsonObject.getString("Name"));
                                String PhoneNumber = jsonObject.getString("PhoneNumber");
                                contentValues.put(TableBloodDirectory.COLUMN_PHONENUMBER, PhoneNumber );
                                contentValues.put(TableBloodDirectory.COLUMN_EMAIL, jsonObject.getString("Email"));
                                contentValues.put(TableBloodDirectory.COLUMN_BLOODGROUP, jsonObject.getString("BloodGroup"));
                                contentValues.put(TableBloodDirectory.COLUMN_DISTRICT, jsonObject.getString("District"));
                                int isPublic = jsonObject.getInt("isPublic");
                                long BloodDonatedTime = Long.parseLong(jsonObject.getString("BloodDonatedTime"));
                                Uri uri = DatabaseContentProvider.BLOODDIRECTORY_URI;
                                if(i == 0){
                                    getContentResolver().delete(uri,
                                            TableBloodDirectory.COLUMN_ID + ">" + LastDownload + " AND " +
                                            TableBloodDirectory.COLUMN_ID + "<=" + jsonObject.getLong("DonorId"),null);
                                    LastDownload = jsonObject.getLong("DonorId");
                                }
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(BloodDonatedTime);
                                Calendar nowTime = Calendar.getInstance();
                                nowTime.add(Calendar.MONTH,-3);
                                if(isPublic == 1 && (calendar.compareTo(nowTime) < 0) && !PhoneNumber.equals(sharedPreferences.getString("DefaultPhoneNumber","")))
                                    getContentResolver().insert(uri, contentValues);
                            }
                            if(jsonArray.length()!=10){
                                LastDownload = 8;
                            }
                            if(LastDownload == StartDownload || ( LastDownload > StartDownload && LastDownload < ( StartDownload + 10 ) )){
                                editor.putLong("BloodDirectoryLastUpdated",LastDownload);
                                editor.putLong("BloodDirectoryUpdateTime",System.currentTimeMillis());
                                editor.apply();
                            }else {
                                editor.putLong("BloodDirectoryLastUpdated",LastDownload);
                                editor.apply();
                                DownloadData();
                            }
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        Intent startIntent = new Intent(DownloadDirectoryService.this,DownloadDirectoryService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(DownloadDirectoryService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        long nextUpdateTime = System.currentTimeMillis();
                        nextUpdateTime += (24*60*60*1000);
                        alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent startIntent = new Intent(DownloadDirectoryService.this,DownloadDirectoryService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(DownloadDirectoryService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        long nextUpdateTime = System.currentTimeMillis();
                        nextUpdateTime += 60000;
                        alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                params.put("startId", Long.toString( sharedPreferences.getLong("BloodDirectoryLastUpdated",8)));
                return params;
            }
        };
        SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
    }


}
