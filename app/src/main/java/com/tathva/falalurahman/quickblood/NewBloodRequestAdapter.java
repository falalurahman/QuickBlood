package com.tathva.falalurahman.quickblood;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class NewBloodRequestAdapter extends CursorAdapter {

    public static int position = 1;

    public NewBloodRequestAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View customView;

        if(position % 2 == 1)
            customView = LayoutInflater.from(context).inflate(R.layout.left_round_new_request_row,parent,false);
        else
            customView = LayoutInflater.from(context).inflate(R.layout.right_round_new_request,parent,false);
        position++;
        TextView Name = (TextView) customView.findViewById(R.id.Name);
        TextView PhoneNumber = (TextView) customView.findViewById(R.id.PhoneNumber);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageView);

        final String name = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME));
        final String phoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER));
        final String email = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_EMAIL));
        String bloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP));

        Name.setText(name);
        PhoneNumber.setText(phoneNumber);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor(phoneNumber);
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(21)
                .endConfig()
                .buildRound(bloodGroup,color);
        imageView.setImageDrawable(textDrawable);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BloodDetails.class);
                intent.putExtra("Name",name);
                intent.putExtra("PhoneNumber",phoneNumber);
                intent.putExtra("Email",email);
                context.startActivity(intent);
            }
        });

        return customView;
    }

    @Override
    public void bindView(View customView,final Context context, Cursor cursor) {
        TextView Name = (TextView) customView.findViewById(R.id.Name);
        TextView PhoneNumber = (TextView) customView.findViewById(R.id.PhoneNumber);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageView);

        final String name = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME));
        final String phoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER));
        final String email = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_EMAIL));
        String bloodGroup = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP));

        Name.setText(name);
        PhoneNumber.setText(phoneNumber);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor(phoneNumber);
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(21)
                .endConfig()
                .buildRound(bloodGroup,color);
        imageView.setImageDrawable(textDrawable);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BloodDetails.class);
                intent.putExtra("Name",name);
                intent.putExtra("PhoneNumber",phoneNumber);
                intent.putExtra("Email",email);
                context.startActivity(intent);
            }
        });
    }
}
