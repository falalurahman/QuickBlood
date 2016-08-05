package com.tathva.falalurahman.quickblood;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationsAdapter extends CursorAdapter{

    public NotificationsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View customView = LayoutInflater.from(context).inflate(R.layout.row_notification,parent,false);
        TextView TimeStampTextView = (TextView) customView.findViewById(R.id.TimeStamp);
        TextView NameTextView = (TextView) customView.findViewById(R.id.Name);
        TextView PhoneNumberTextView = (TextView) customView.findViewById(R.id.PhoneNumber);
        TextView EmailTextView = (TextView) customView.findViewById(R.id.Email);
        TextView EmailHeading = (TextView) customView.findViewById(R.id.emailHeading);
        TextView DistrictTextView = (TextView) customView.findViewById(R.id.District);
        TextView BloodGroupTextView = (TextView) customView.findViewById(R.id.BloodGroup);
        TextView VolumeTextView = (TextView) customView.findViewById(R.id.Volume);
        LinearLayout RightLayout = (LinearLayout) customView.findViewById(R.id.RightLayout);
        LinearLayout AddressLayout = (LinearLayout) customView.findViewById(R.id.address_layout);
        TextView AddressTextView = (TextView) customView.findViewById(R.id.Address);
        TextView DonorsTextView = (TextView) customView.findViewById(R.id.Donors);

        int isSeen = cursor.getInt(cursor.getColumnIndex(TableNotifications.COLUMN_ISSEEN));
        long TimeStamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_TIMESTAMP)));
        String Name = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_NAME));
        final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_PHONENUMBER));
        String Email = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_EMAIL));
        String District = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_DISTRICT));
        String BloodGroup = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_BLOODGROUP));
        String Address = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_ADDRESS));
        int Volume = cursor.getInt(cursor.getColumnIndex(TableNotifications.COLUMN_VOLUME));

        if(isSeen == 0){
            NameTextView.setTypeface(NameTextView.getTypeface(), Typeface.BOLD_ITALIC);
            BloodGroupTextView.setTypeface(BloodGroupTextView.getTypeface(), Typeface.BOLD);
        }else {
            NameTextView.setTypeface(NameTextView.getTypeface(), Typeface.NORMAL);
            BloodGroupTextView.setTypeface(BloodGroupTextView.getTypeface(), Typeface.NORMAL);
        }
        NameTextView.setText(Name);
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeStamp);
        TimeStampTextView.setText(dateFormat.format(calendar.getTime()));
        PhoneNumberTextView.setText(PhoneNumber);
        if(Email.equals("")){
            EmailTextView.setText(Email);
            EmailTextView.setVisibility(View.GONE);
            EmailHeading.setVisibility(View.GONE);
        }else {
            EmailHeading.setVisibility(View.VISIBLE);
            EmailTextView.setVisibility(View.VISIBLE);
            EmailTextView.setText(Email);
        }
        DistrictTextView.setText(District);
        if(Address.equals("")){
            AddressTextView.setText(Address);
            AddressLayout.setVisibility(View.GONE);
        }else {
            AddressTextView.setText(Address);
            AddressLayout.setVisibility(View.VISIBLE);
        }
        String BloodGroupString = BloodGroup.replaceAll("\\s.?ve","<small><small><small>$0</small></small></small>");
        BloodGroupTextView.setText(Html.fromHtml(BloodGroupString));
        VolumeTextView.setText(Integer.toString(Volume));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor(TimeStamp);
        GradientDrawable gradientDrawable = (GradientDrawable) RightLayout.getBackground();
        gradientDrawable.setColor(color);

        String Donors = "";
        Cursor cursor1 = context.getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,new String[]{TableBloodDonor.COLUMN_NAME,TableBloodDonor.COLUMN_PHONENUMBER},
                TableBloodDonor.COLUMN_BLOODGROUP + "='" + BloodGroup + "'",null,null);
        boolean oneAdded = false;
        if(cursor1!=null){
            while (cursor1.moveToNext()){
                if(cursor1.getString(cursor1.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER)).equals(PhoneNumber))
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
        DonorsTextView.setText(Donors);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("text/plain");
                intent.setData(Uri.parse("tel:" + PhoneNumber));
                context.startActivity(Intent.createChooser(intent, "Complete Action Using"));
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
            }
        });
        return customView;
    }

    @Override
    public void bindView(View customView,final Context context, Cursor cursor) {

        TextView TimeStampTextView = (TextView) customView.findViewById(R.id.TimeStamp);
        TextView NameTextView = (TextView) customView.findViewById(R.id.Name);
        TextView PhoneNumberTextView = (TextView) customView.findViewById(R.id.PhoneNumber);
        TextView EmailTextView = (TextView) customView.findViewById(R.id.Email);
        TextView EmailHeading = (TextView) customView.findViewById(R.id.emailHeading);
        TextView DistrictTextView = (TextView) customView.findViewById(R.id.District);
        TextView BloodGroupTextView = (TextView) customView.findViewById(R.id.BloodGroup);
        TextView VolumeTextView = (TextView) customView.findViewById(R.id.Volume);
        LinearLayout RightLayout = (LinearLayout) customView.findViewById(R.id.RightLayout);
        LinearLayout AddressLayout = (LinearLayout) customView.findViewById(R.id.address_layout);
        TextView AddressTextView = (TextView) customView.findViewById(R.id.Address);
        TextView DonorsTextView = (TextView) customView.findViewById(R.id.Donors);

        int isSeen = cursor.getInt(cursor.getColumnIndex(TableNotifications.COLUMN_ISSEEN));
        long TimeStamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_TIMESTAMP)));
        String Name = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_NAME));
        final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_PHONENUMBER));
        String Email = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_EMAIL));
        String District = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_DISTRICT));
        String BloodGroup = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_BLOODGROUP));
        String Address = cursor.getString(cursor.getColumnIndex(TableNotifications.COLUMN_ADDRESS));
        int Volume = cursor.getInt(cursor.getColumnIndex(TableNotifications.COLUMN_VOLUME));

        if(isSeen == 0){
            NameTextView.setTypeface(NameTextView.getTypeface(), Typeface.BOLD_ITALIC);
            BloodGroupTextView.setTypeface(BloodGroupTextView.getTypeface(), Typeface.BOLD);
        }else {
            NameTextView.setTypeface(NameTextView.getTypeface(), Typeface.NORMAL);
            BloodGroupTextView.setTypeface(BloodGroupTextView.getTypeface(), Typeface.NORMAL);
        }
        NameTextView.setText(Name);
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeStamp);
        TimeStampTextView.setText(dateFormat.format(calendar.getTime()));
        PhoneNumberTextView.setText(PhoneNumber);
        if(Email.equals("")){
            EmailTextView.setText(Email);
            EmailTextView.setVisibility(View.GONE);
            EmailHeading.setVisibility(View.GONE);
        }else {
            EmailHeading.setVisibility(View.VISIBLE);
            EmailTextView.setVisibility(View.VISIBLE);
            EmailTextView.setText(Email);
        }
        DistrictTextView.setText(District);
        if(Address.equals("")){
            AddressTextView.setText(Address);
            AddressLayout.setVisibility(View.GONE);
        }else {
            AddressTextView.setText(Address);
            AddressLayout.setVisibility(View.VISIBLE);
        }
        String BloodGroupString = BloodGroup.replaceAll("\\s.?ve","<small><small><small>$0</small></small></small>");
        BloodGroupTextView.setText(Html.fromHtml(BloodGroupString));
        VolumeTextView.setText(Integer.toString(Volume));
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor(TimeStamp);
        GradientDrawable gradientDrawable = (GradientDrawable) RightLayout.getBackground();
        gradientDrawable.setColor(color);

        String Donors = "";
        Cursor cursor1 = context.getContentResolver().query(DatabaseContentProvider.BLOODDONORS_URI,new String[]{TableBloodDonor.COLUMN_NAME,TableBloodDonor.COLUMN_PHONENUMBER},
                TableBloodDonor.COLUMN_BLOODGROUP + "='" + BloodGroup + "'",null,null);
        boolean oneAdded = false;
        if(cursor1!=null){
            while (cursor1.moveToNext()){
                if(cursor1.getString(cursor1.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER)).equals(PhoneNumber))
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
        DonorsTextView.setText(Donors);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("text/plain");
                intent.setData(Uri.parse("tel:" + PhoneNumber));
                context.startActivity(Intent.createChooser(intent, "Complete Action Using"));
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
            }
        });
    }
}
