package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyBloodRequestAdapter extends CursorAdapter{

    public MyBloodRequestAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View customView = LayoutInflater.from(context).inflate(R.layout.row_my_blood_request,parent,false);
        /*TextView NameTextView = (TextView) customView.findViewById(R.id.name_textview);
        TextView PhoneNumberTextView = (TextView) customView.findViewById(R.id.phonenumber_textview);
        TextView EmailTextView = (TextView) customView.findViewById(R.id.email_textview);
        TextView EmailHeading = (TextView) customView.findViewById(R.id.emailHeading);
        TextView DistrictTextView = (TextView) customView.findViewById(R.id.district_textview);
        TextView AddressTextView = (TextView) customView.findViewById(R.id.address_textview);
        LinearLayout AddressLayout = (LinearLayout) customView.findViewById(R.id.address_layout);
        TextView BloodGroupTextView = (TextView) customView.findViewById(R.id.bloodgroup_textview);
        TextView TimeStampTextView = (TextView) customView.findViewById(R.id.timestamp_textview);
        TextView QuantityTextView = (TextView) customView.findViewById(R.id.quantity_textview);
        LinearLayout BloodLayout = (LinearLayout) customView.findViewById(R.id.blood_layout);

        NameTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_NAME)));
        final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_PHONENUMBER));
        PhoneNumberTextView.setText(PhoneNumber);
        String email = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_EMAIL));
        if(email.equals("")){
            EmailTextView.setText(email);
            EmailTextView.setVisibility(View.GONE);
            EmailHeading.setVisibility(View.GONE);
        }else {
            EmailHeading.setVisibility(View.VISIBLE);
            EmailTextView.setVisibility(View.VISIBLE);
            EmailTextView.setText(email);
        }
        DistrictTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_DISTRICT)));
        String address = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_ADDRESS));
        AddressTextView.setText(address);
        if(address.equals("")){
            AddressLayout.setVisibility(View.GONE);
        }else {
            AddressLayout.setVisibility(View.VISIBLE);
        }
        String bloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_BLOODGROUP));
        bloodGroup = bloodGroup.replaceAll("\\s.?ve","<small><small><small>$0</small></small></small>");
        BloodGroupTextView.setText(Html.fromHtml(bloodGroup));
        Long TimeStamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_TIMESTAMP)));
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeStamp);
        TimeStampTextView.setText(dateFormat.format(calendar.getTime()));
        QuantityTextView.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_VOLUME))));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor((TimeStamp+5));
        GradientDrawable gradientDrawable = (GradientDrawable) BloodLayout.getBackground();
        gradientDrawable.setColor(color);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation rotateAnimation =
                        new RotateAnimation(-1.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setRepeatCount(5);
                rotateAnimation.setRepeatMode(RotateAnimation.REVERSE);
                rotateAnimation.setDuration(100);
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                view.startAnimation(rotateAnimation);
                vibrator.vibrate(100);
                Toast.makeText(context,"Slide Left To Delete Request When Done",Toast.LENGTH_LONG).show();
            }
        });*/
        return customView;
    }

    @Override
    public void bindView(View customView,final Context context, Cursor cursor) {

        TextView NameTextView = (TextView) customView.findViewById(R.id.name_textview);
        TextView PhoneNumberTextView = (TextView) customView.findViewById(R.id.phonenumber_textview);
        TextView EmailTextView = (TextView) customView.findViewById(R.id.email_textview);
        TextView EmailHeading = (TextView) customView.findViewById(R.id.emailHeading);
        TextView DistrictTextView = (TextView) customView.findViewById(R.id.district_textview);
        TextView AddressTextView = (TextView) customView.findViewById(R.id.address_textview);
        LinearLayout AddressLayout = (LinearLayout) customView.findViewById(R.id.address_layout);
        TextView BloodGroupTextView = (TextView) customView.findViewById(R.id.bloodgroup_textview);
        TextView TimeStampTextView = (TextView) customView.findViewById(R.id.timestamp_textview);
        TextView QuantityTextView = (TextView) customView.findViewById(R.id.quantity_textview);
        LinearLayout BloodLayout = (LinearLayout) customView.findViewById(R.id.blood_layout);

        NameTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_NAME)));
        final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_PHONENUMBER));
        PhoneNumberTextView.setText(PhoneNumber);
        String email = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_EMAIL));
        if(email.equals("")){
            EmailTextView.setText(email);
            EmailTextView.setVisibility(View.GONE);
            EmailHeading.setVisibility(View.GONE);
        }else {
            EmailHeading.setVisibility(View.VISIBLE);
            EmailTextView.setVisibility(View.VISIBLE);
            EmailTextView.setText(email);
        }
        DistrictTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_DISTRICT)));
        String address = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_ADDRESS));
        AddressTextView.setText(address);
        if(address.equals("")){
            AddressLayout.setVisibility(View.GONE);
        }else {
            AddressLayout.setVisibility(View.VISIBLE);
        }
        String bloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_BLOODGROUP));
        bloodGroup = bloodGroup.replaceAll("\\s.?ve","<small><small><small>$0</small></small></small>");
        BloodGroupTextView.setText(Html.fromHtml(bloodGroup));
        Long TimeStamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableBloodRequests.COLUMN_TIMESTAMP)));
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeStamp);
        TimeStampTextView.setText(dateFormat.format(calendar.getTime()));
        QuantityTextView.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(TableBloodRequests.COLUMN_VOLUME))));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor((TimeStamp + 5));
        GradientDrawable gradientDrawable = (GradientDrawable) BloodLayout.getBackground();
        gradientDrawable.setColor(color);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation rotateAnimation =
                        new RotateAnimation(-1.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setRepeatCount(5);
                rotateAnimation.setRepeatMode(RotateAnimation.REVERSE);
                rotateAnimation.setDuration(100);
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                view.startAnimation(rotateAnimation);
                vibrator.vibrate(100);
                Toast.makeText(context,"Slide Left To Delete Request When Done",Toast.LENGTH_LONG).show();
            }
        });
    }
}
