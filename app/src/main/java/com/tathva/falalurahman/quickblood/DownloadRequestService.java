package com.tathva.falalurahman.quickblood;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

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

import me.leolin.shortcutbadger.ShortcutBadger;

public class DownloadRequestService extends IntentService {

    Cursor cursor;
    String selection = "";
    String DefaultBloodGroup = "";
    public static final String BROADCAST_ACTION = "com.tathva.falalurahman.quickblood.notificationcount";

    public DownloadRequestService() {
        super("DownloadRequestService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        cursor = getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,new String[]{"DISTINCT " + TableBloodDonor.COLUMN_BLOODGROUP},
                null,null,TableBloodDonor.COLUMN_BLOODGROUP + " ASC");
        selection = "(";
        if(cursor != null){
            while (cursor.moveToNext()){
                if(cursor.getPosition() != 0){
                    selection = selection.concat(" OR ");
                }
                if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("A +ve")){
                    selection = selection.concat(" (BloodGroup = 'A +ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("A -ve")){
                    selection = selection.concat(" (BloodGroup = 'A -ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("B +ve")){
                    selection = selection.concat(" (BloodGroup = 'B +ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("B -ve")){
                    selection = selection.concat(" (BloodGroup = 'B -ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("O +ve")){
                    selection = selection.concat(" (BloodGroup = 'O +ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("O -ve")){
                    selection = selection.concat(" (BloodGroup = 'O -ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("AB +ve")){
                    selection = selection.concat(" (BloodGroup = 'AB +ve') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)).equals("AB -ve")){
                    selection = selection.concat(" (BloodGroup = 'AB -ve') ");
                }
            }
            cursor.close();
        }
        cursor = getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,new String[]{"DISTINCT " + TableBloodDonor.COLUMN_DISTRICT},
                null,null,null);
        selection = selection.concat(") AND (");
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                if(cursor.getPosition() != 0){
                    selection = selection.concat(" OR ");
                }
                if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Kasargod")){
                    selection = selection.concat(" (District = 'Kasargod') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Kannur")) {
                    selection = selection.concat(" (District = 'Kannur') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Wayanad")) {
                    selection = selection.concat(" (District = 'Wayanad') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Kozhikode")) {
                    selection = selection.concat(" (District = 'Kozhikode') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Malappuram")) {
                    selection = selection.concat(" (District = 'Malappuram') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Palakkad")) {
                    selection = selection.concat(" (District = 'Palakkad') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Thrissur")) {
                    selection = selection.concat(" (District = 'Thrissur') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Ernakulam")) {
                    selection = selection.concat(" (District = 'Ernakulam') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Idukki")) {
                    selection = selection.concat(" (District = 'Idukki') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Kottayam")) {
                    selection = selection.concat(" (District = 'Kottayam') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Alappuzha")) {
                    selection = selection.concat(" (District = 'Alappuzha') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Pathanamthitta")) {
                    selection = selection.concat(" (District = 'Pathanamthitta') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Kollam")) {
                    selection = selection.concat(" (District = 'Kollam') ");
                }else if(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)).equals("Thiruvananthapuram")) {
                    selection = selection.concat(" (District = 'Thiruvananthapuram') ");
                }
            }
            selection = selection.concat(")");
            cursor.close();
        }else {
            selection = "";
        }
        cursor = getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,new String[]{TableBloodDonor.COLUMN_BLOODGROUP},
                TableBloodDonor.COLUMN_DEFAULT + "=1",null,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            DefaultBloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP));
            cursor.close();
        }else {
            DefaultBloodGroup = "";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,getString(R.string.get_requests_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if(i == 0){
                                    editor.putLong("NotificationsStartId",jsonObject.getLong("RequestId"));
                                    editor.apply();
                                }
                                String Name = jsonObject.getString("Name");
                                String PhoneNumber = jsonObject.getString("PhoneNumber");
                                String BloodGroup = jsonObject.getString("BloodGroup");
                                String Donors = "";
                                Cursor cursor1 = getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,new String[]{TableBloodDonor.COLUMN_NAME,TableBloodDonor.COLUMN_PHONENUMBER, TableBloodDonor.COLUMN_BLOODDONATEDTIME},
                                        TableBloodDonor.COLUMN_BLOODGROUP + "='" + BloodGroup + "'",null,null);
                                boolean oneAdded = false;
                                if(cursor1!=null && cursor1.getCount()>0){
                                    while (cursor1.moveToNext()){
                                        if(cursor1.getString(cursor1.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER)).equals(PhoneNumber))
                                            continue;
                                        long BloodDonatedTime = Long.parseLong(cursor1.getString(cursor1.getColumnIndex(TableBloodDonor.COLUMN_BLOODDONATEDTIME)));
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(BloodDonatedTime);
                                        Calendar nowTime = Calendar.getInstance();
                                        nowTime.add(Calendar.MONTH,-3);
                                        if(calendar.compareTo(nowTime) >= 0)
                                            continue;
                                        if(cursor1.getPosition() != 0 && oneAdded){
                                            Donors = Donors.concat(", ");
                                        }
                                        Donors = Donors.concat(cursor1.getString(cursor1.getColumnIndex(TableBloodDonor.COLUMN_NAME)));
                                        oneAdded = true;
                                    }
                                    cursor1.close();
                                }else {
                                    Donors = "No Donors";
                                }
                                if(!Donors.equals("") && !Donors.equals("No Donors")){
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(TableNotifications.COLUMN_ID, jsonObject.getInt("RequestId"));
                                    contentValues.put(TableNotifications.COLUMN_TIMESTAMP, jsonObject.getString("TimeStamp"));
                                    contentValues.put(TableNotifications.COLUMN_NAME, jsonObject.getString("Name"));
                                    contentValues.put(TableNotifications.COLUMN_PHONENUMBER, jsonObject.getString("PhoneNumber"));
                                    contentValues.put(TableNotifications.COLUMN_EMAIL, jsonObject.getString("Email"));
                                    contentValues.put(TableNotifications.COLUMN_BLOODGROUP, jsonObject.getString("BloodGroup"));
                                    contentValues.put(TableNotifications.COLUMN_DISTRICT, jsonObject.getString("District"));
                                    contentValues.put(TableNotifications.COLUMN_ADDRESS, jsonObject.getString("Address"));
                                    contentValues.put(TableNotifications.COLUMN_VOLUME, jsonObject.getInt("Volume"));
                                    contentValues.put(TableNotifications.COLUMN_ISSEEN, 0);
                                    Uri uri = DatabaseContentProvider.NOTIFICATIONS_URI;
                                    getContentResolver().insert(uri, contentValues);

                                    if (DefaultBloodGroup.equals("")){
                                        Intent notificationIntent = new Intent(DownloadRequestService.this,NotificationsActivity.class);
                                        PendingIntent notificationPendingIntent = PendingIntent.getActivity(DownloadRequestService.this,0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                                        NotificationCompat.Builder notification = new NotificationCompat.Builder(DownloadRequestService.this);
                                        notification.setAutoCancel(true);
                                        notification.setOngoing(false);
                                        notification.setWhen(System.currentTimeMillis());
                                        notification.setContentTitle("Quick Blood");
                                        notification.setSmallIcon(R.mipmap.ic_launcher);
                                        notification.setContentIntent(notificationPendingIntent);
                                        notification.setSound(Uri.parse("android.resource://"
                                                + getPackageName() + "/" + R.raw.ringtone));
                                        notification.setDefaults(Notification.DEFAULT_VIBRATE);
                                        notification.setLights(0xffffff00,1000,500);
                                        notification.setTicker(Name + " needs your Donor's Blood");
                                        notification.setStyle(new NotificationCompat.BigTextStyle().bigText(Name + " requests the blood of "
                                                + Donors));
                                        notification.setContentText(Name + " requests the blood of "
                                                + Donors);
                                        int id = (int) (Math.random() * 10000);
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(id, notification.build());
                                    }
                                    else if(jsonObject.getString("BloodGroup").equals(DefaultBloodGroup)){
                                        Intent notificationIntent = new Intent(DownloadRequestService.this,NotificationsActivity.class);
                                        PendingIntent notificationPendingIntent = PendingIntent.getActivity(DownloadRequestService.this,0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                                        NotificationCompat.Builder notification = new NotificationCompat.Builder(DownloadRequestService.this);
                                        notification.setAutoCancel(true);
                                        notification.setOngoing(false);
                                        notification.setWhen(System.currentTimeMillis());
                                        notification.setContentTitle("Quick Blood");
                                        notification.setSmallIcon(R.mipmap.ic_launcher);
                                        notification.setContentIntent(notificationPendingIntent);
                                        notification.setSound(Uri.parse("android.resource://"
                                                + getPackageName() + "/" + R.raw.ringtone));
                                        notification.setDefaults(Notification.DEFAULT_VIBRATE);
                                        notification.setLights(0xffffff00,1000,500);
                                        notification.setTicker(Name + " needs your Blood");
                                        notification.setStyle(new NotificationCompat.BigTextStyle().bigText(Name + " requests the blood of "
                                                + Donors));
                                        notification.setContentText(Name + " requests the blood of "
                                                + Donors);
                                        int id = (int) (Math.random() * 10000);
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(id, notification.build());
                                    }
                                }
                            }
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }

                        Cursor cursor = getContentResolver().query(DatabaseContentProvider.NOTIFICATIONS_URI,new String[]{TableNotifications.COLUMN_ID},TableNotifications.COLUMN_ISSEEN + "=0",null,null);
                        if(cursor != null) {
                            int NotificationCount = cursor.getCount();
                            cursor.close();
                            SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("NotificationCount",NotificationCount);
                            editor.apply();
                            Intent broadcastIntent = new Intent(BROADCAST_ACTION);
                            broadcastIntent.putExtra("NotificationCount",NotificationCount);
                            sendBroadcast(intent);
                            ShortcutBadger.applyCount(DownloadRequestService.this,NotificationCount);
                        }

                        Intent startIntent = new Intent(DownloadRequestService.this,DownloadRequestService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(DownloadRequestService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        long nextUpdateTime = System.currentTimeMillis();
                        nextUpdateTime += 60000;
                        alarmManager.set(AlarmManager.RTC,nextUpdateTime,pendingIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent startIntent = new Intent(DownloadRequestService.this,DownloadRequestService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(DownloadRequestService.this,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
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
                params.put("startId", Long.toString( sharedPreferences.getLong("NotificationsStartId",0)));
                params.put("selection",selection);
                return params;
            }
        };
        SingletonClass.getInstance(this).addToRequestQueue(stringRequest);
    }

}
