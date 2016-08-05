package com.tathva.falalurahman.quickblood;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class NewPostService extends IntentService {

    public NewPostService() {
        super("NewPostService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.get_post_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("QB",response);
                            JSONArray jsonArray = new JSONArray(response);
                            SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(TableQBPosts.COLUMN_ID, jsonObject.getLong("PostId"));
                                contentValues.put(TableQBPosts.COLUMN_USERNAME, jsonObject.getString("Username"));
                                contentValues.put(TableQBPosts.COLUMN_TIMESTAMP, jsonObject.getString("TimeStamp"));
                                contentValues.put(TableQBPosts.COLUMN_STATUS, jsonObject.getString("Status"));
                                contentValues.put(TableQBPosts.COLUMN_LINK, jsonObject.getString("Link"));
                                contentValues.put(TableQBPosts.COLUMN_IMAGE, jsonObject.getString("Image"));
                                Uri uri = DatabaseContentProvider.QBPOST_URI;
                                getContentResolver().insert(uri, contentValues);

                                if(i == 0){
                                    editor.putLong("QBPostsStartId",jsonObject.getLong("PostId"));
                                    editor.apply();
                                }
                                if(sharedPreferences.getLong("QBPostsEndId",0)==0 && i == jsonArray.length()-1){
                                    editor.putLong("QBPostsEndId",jsonObject.getLong("PostId"));
                                    editor.apply();
                                }

                            }
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        Intent startIntent = new Intent(NewPostService.this,NewPostService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(NewPostService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        long nextUpdateTime = System.currentTimeMillis();
                        nextUpdateTime += 120000;
                        alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent startIntent = new Intent(NewPostService.this,NewPostService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(NewPostService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        long nextUpdateTime = System.currentTimeMillis();
                        nextUpdateTime += 120000;
                        alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                params.put("startId", Long.toString( sharedPreferences.getLong("QBPostsStartId",0)));
                return params;
            }
        };
        SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
    }

}
