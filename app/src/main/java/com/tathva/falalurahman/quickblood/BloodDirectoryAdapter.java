package com.tathva.falalurahman.quickblood;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BloodDirectoryAdapter extends CursorAdapter {

    public BloodDirectoryAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View customView, final Context context, Cursor cursor) {

        LinearLayout HeaderView = (LinearLayout) customView.findViewById(R.id.HeaderView);
        RelativeLayout RowView = (RelativeLayout) customView.findViewById(R.id.RowView);
        ImageView CallButton = (ImageView) customView.findViewById(R.id.CallButton);

        final String phoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_PHONENUMBER));
        if(phoneNumber.equals("1111")){
            HeaderView.setVisibility(View.VISIBLE);
            RowView.setVisibility(View.GONE);
            TextView BloodGroup = (TextView) customView.findViewById(R.id.BloodGroup);
            BloodGroup.setText( cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_NAME)));
        }else {
            HeaderView.setVisibility(View.GONE);
            RowView.setVisibility(View.VISIBLE);
            TextView Name = (TextView) customView.findViewById(R.id.Name);
            TextView PhoneNumber = (TextView) customView.findViewById(R.id.PhoneNumber);

            String name = cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_NAME));

            Name.setText(name);
            PhoneNumber.setText(phoneNumber);
            CallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("text/plain");
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    context.startActivity(Intent.createChooser(intent, "Complete Action Using"));
                }
            });

        }
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View customView = LayoutInflater.from(context).inflate(R.layout.row_blood_directory,parent,false);

        LinearLayout HeaderView = (LinearLayout) customView.findViewById(R.id.HeaderView);
        RelativeLayout RowView = (RelativeLayout) customView.findViewById(R.id.RowView);
        ImageView CallButton = (ImageView) customView.findViewById(R.id.CallButton);

        final String phoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_PHONENUMBER));
        if(phoneNumber.equals("1111")){
            HeaderView.setVisibility(View.VISIBLE);
            RowView.setVisibility(View.GONE);
            TextView BloodGroup = (TextView) customView.findViewById(R.id.BloodGroup);
            BloodGroup.setText( cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_NAME)));
        }else {
            HeaderView.setVisibility(View.GONE);
            RowView.setVisibility(View.VISIBLE);
            TextView Name = (TextView) customView.findViewById(R.id.Name);
            TextView PhoneNumber = (TextView) customView.findViewById(R.id.PhoneNumber);

            String name = cursor.getString(cursor.getColumnIndex(TableBloodDirectory.COLUMN_NAME));

            Name.setText(name);
            PhoneNumber.setText(phoneNumber);

            CallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("text/plain");
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    context.startActivity(Intent.createChooser(intent, "Complete Action Using"));
                }
            });

        }
        return customView;
    }
}
