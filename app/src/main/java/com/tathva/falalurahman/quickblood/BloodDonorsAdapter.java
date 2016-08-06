package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;

public class BloodDonorsAdapter extends CursorAdapter{

    Intent intent;

    public BloodDonorsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(final View customView, final Context context, final Cursor cursor) {
        TextView NameTextView = (TextView) customView.findViewById(R.id.name_textview);
        TextView DOBTextView = (TextView) customView.findViewById(R.id.dob_textview);
        TextView WeightTextView = (TextView) customView.findViewById(R.id.weight_textview);
        TextView PhoneNumberTextView = (TextView) customView.findViewById(R.id.phonenumber_textview);
        TextView EmailTextView = (TextView) customView.findViewById(R.id.email_textview);
        TextView EmailHeading = (TextView) customView.findViewById(R.id.emailHeading);
        TextView DistrictTextView = (TextView) customView.findViewById(R.id.district_textview);
        TextView BloodGroupTextView = (TextView) customView.findViewById(R.id.bloodgroup_textview);
        final TextView BloodDonatedTextView = (TextView) customView.findViewById(R.id.blooddonated_textview);
        TextView PublicTextView = (TextView) customView.findViewById(R.id.public_textview);
        ImageView PublicImageView = (ImageView) customView.findViewById(R.id.public_imageview);
        LinearLayout DefaultContact = (LinearLayout) customView.findViewById(R.id.defaultContact);
        LinearLayout DonatedButton = (LinearLayout) customView.findViewById(R.id.donatedButton);
        final ImageView PopUp = (ImageView) customView.findViewById(R.id.PopUp);

        NameTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME)));
        WeightTextView.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_WEIGHT))) + " Kgs");
        DateFormat dateFormat1 = new SimpleDateFormat("EEE, dd MMM yyyy");
        Long DateOfBirth = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DATEOFBIRTH)));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(DateOfBirth);
        DOBTextView.setText(dateFormat1.format(calendar1.getTime()));
        final String PhoneNumber = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER));
        PhoneNumberTextView.setText(PhoneNumber);
        final String email = cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_EMAIL));
        if(email.equals("")){
            EmailTextView.setText(email);
            EmailTextView.setVisibility(View.GONE);
            EmailHeading.setVisibility(View.GONE);
        }else {
            EmailHeading.setVisibility(View.VISIBLE);
            EmailTextView.setVisibility(View.VISIBLE);
            EmailTextView.setText(email);
        }
        DistrictTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)));
        BloodGroupTextView.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)));
        Long BloodDonated = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODDONATEDTIME)));
        if(BloodDonated == 0){
            BloodDonatedTextView.setText("Never");
        }else {
            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(BloodDonated);
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MONTH,-3);
            BloodDonatedTextView.setText(dateFormat.format(calendar.getTime()));
            if(calendar.compareTo(nowTime) < 0) {
                DonatedButton.setVisibility(View.VISIBLE);
            }else {
                DonatedButton.setVisibility(View.GONE);
            }
        }
        final int isPublic = cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ISPUBLIC));
        if(isPublic == 1){
            PublicTextView.setText("Public");
            PublicImageView.setImageResource(R.drawable.ic_public_black_36dp);
        }else {
            PublicTextView.setText("Private");
            PublicImageView.setImageResource(R.drawable.ic_person_black_36dp);
        }
        final int isDefault = cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_DEFAULT));
        if(isDefault == 1){
            DefaultContact.setVisibility(View.VISIBLE);
        }else {
            DefaultContact.setVisibility(View.GONE);
        }

        DonatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long currentTime = System.currentTimeMillis();
                ContentValues contentValues = new ContentValues();
                contentValues.put("BloodDonatedTime",Long.toString(currentTime));
                contentValues.put("isUploaded",0);
                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                        PhoneNumber),contentValues,null,null);
                Intent intent = new Intent(context,UploadBloodDonorService.class);
                context.startService(intent);
            }
        });
        PopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.inflate(R.menu.popup_menu);
                if(isPublic == 1){
                    popupMenu.getMenu().findItem(R.id.Public).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.Private).setVisible(true);
                }else {
                    popupMenu.getMenu().findItem(R.id.Public).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.Private).setVisible(false);
                }
                if(isDefault == 1){
                    popupMenu.getMenu().findItem(R.id.Default).setVisible(false);
                }else {
                    popupMenu.getMenu().findItem(R.id.Default).setVisible(true);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        ContentValues contentValues;
                        switch (id){
                            case R.id.Delete:

                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.alert_dialog);
                                TextView DialogTitle = (TextView) dialog.findViewById(R.id.DialogTitle);
                                DialogTitle.setText("Delete Blood Donor");
                                TextView DialogMessage = (TextView) dialog.findViewById(R.id.DialogMessage);
                                DialogMessage.setText("Are you sure you want to delete this Blood Donor?");
                                TextView Confirm = (TextView) dialog.findViewById(R.id.Confirm);
                                Confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final ContentValues contentValues = new ContentValues();
                                        contentValues.put("isUploaded",2);
                                        context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                                PhoneNumber),contentValues,null,null);
                                        contentValues.put(TableBloodDonor.COLUMN_ID,cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ID)));
                                        contentValues.put(TableBloodDonor.COLUMN_NAME,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME)));
                                        contentValues.put(TableBloodDonor.COLUMN_PHONENUMBER,PhoneNumber);
                                        contentValues.put(TableBloodDonor.COLUMN_EMAIL,email);
                                        contentValues.put(TableBloodDonor.COLUMN_DISTRICT,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)));
                                        contentValues.put(TableBloodDonor.COLUMN_BLOODGROUP,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)));
                                        contentValues.put(TableBloodDonor.COLUMN_BLOODDONATEDTIME,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODDONATEDTIME)));
                                        contentValues.put(TableBloodDonor.COLUMN_ISPUBLIC,cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ISPUBLIC)));
                                        contentValues.put(TableBloodDonor.COLUMN_DEFAULT,cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_DEFAULT)));
                                        contentValues.put(TableBloodDonor.COLUMN_ISUPLOADED,0);
                                        Snackbar.make(customView,"Blood Donor Removed",Snackbar.LENGTH_LONG)
                                                .setAction("UNDO", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        context.getContentResolver().insert(DatabaseContentProvider.BLOODDONORS_URI,contentValues);
                                                    }
                                                })
                                                .setActionTextColor(context.getResources().getColor(R.color.colorPrimary))
                                                .show();
                                        dialog.dismiss();
                                        }
                                });
                                TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
                                Cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                                break;
                            case R.id.Edit:
                                intent = new Intent(context,EditBloodDonorsActivity.class);
                                intent.putExtra("PhoneNumber",PhoneNumber);
                                context.startActivity(intent);
                                break;
                            case R.id.Public:
                                contentValues = new ContentValues();
                                contentValues.put("isPublic",1);
                                contentValues.put("isUploaded", 0);
                                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                        PhoneNumber),contentValues,null,null);
                                intent = new Intent(context,UploadBloodDonorService.class);
                                context.startService(intent);
                                break;
                            case R.id.Private:
                                contentValues = new ContentValues();
                                contentValues.put("isPublic",0);
                                contentValues.put("isUploaded", 0);
                                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                        PhoneNumber),contentValues,null,null);
                                intent = new Intent(context,UploadBloodDonorService.class);
                                context.startService(intent);
                                break;
                            case R.id.Default:
                                contentValues = new ContentValues();
                                contentValues.put("isDefault",0);
                                context.getContentResolver().update(DatabaseContentProvider.BLOODDONORS_URI,contentValues,null,null);
                                contentValues.put("isDefault",1);
                                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                        PhoneNumber),contentValues,null,null);
                                SharedPreferences sharedPreferences = context.getSharedPreferences("QuickBlood",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DefaultPhoneNumber",PhoneNumber);
                                editor.apply();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
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
            }
        });
        customView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {PopupMenu popupMenu = new PopupMenu(context,PopUp);
                popupMenu.inflate(R.menu.popup_menu);
                if(isPublic == 1){
                    popupMenu.getMenu().findItem(R.id.Public).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.Private).setVisible(true);
                }else {
                    popupMenu.getMenu().findItem(R.id.Public).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.Private).setVisible(false);
                }
                if(isDefault == 1){
                    popupMenu.getMenu().findItem(R.id.Default).setVisible(false);
                }else {
                    popupMenu.getMenu().findItem(R.id.Default).setVisible(true);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        ContentValues contentValues;
                        switch (id){
                            case R.id.Delete:

                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.alert_dialog);
                                TextView DialogTitle = (TextView) dialog.findViewById(R.id.DialogTitle);
                                DialogTitle.setText("Delete Blood Donor");
                                TextView DialogMessage = (TextView) dialog.findViewById(R.id.DialogMessage);
                                DialogMessage.setText("Are you sure you want to delete this Blood Donor?");
                                TextView Confirm = (TextView) dialog.findViewById(R.id.Confirm);
                                Confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final ContentValues contentValues = new ContentValues();
                                        contentValues.put("isUploaded",2);
                                        context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                                PhoneNumber),contentValues,null,null);
                                        contentValues.put(TableBloodDonor.COLUMN_ID,cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ID)));
                                        contentValues.put(TableBloodDonor.COLUMN_NAME,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME)));
                                        contentValues.put(TableBloodDonor.COLUMN_PHONENUMBER,PhoneNumber);
                                        contentValues.put(TableBloodDonor.COLUMN_EMAIL,email);
                                        contentValues.put(TableBloodDonor.COLUMN_DISTRICT,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)));
                                        contentValues.put(TableBloodDonor.COLUMN_BLOODGROUP,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)));
                                        contentValues.put(TableBloodDonor.COLUMN_BLOODDONATEDTIME,cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODDONATEDTIME)));
                                        contentValues.put(TableBloodDonor.COLUMN_ISPUBLIC,cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ISPUBLIC)));
                                        contentValues.put(TableBloodDonor.COLUMN_DEFAULT,cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_DEFAULT)));
                                        contentValues.put(TableBloodDonor.COLUMN_ISUPLOADED,0);
                                        Snackbar.make(customView,"Blood Donor Removed",Snackbar.LENGTH_LONG)
                                                .setAction("UNDO", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        context.getContentResolver().insert(DatabaseContentProvider.BLOODDONORS_URI,contentValues);
                                                    }
                                                })
                                                .setActionTextColor(context.getResources().getColor(R.color.colorPrimary))
                                                .show();
                                        dialog.dismiss();
                                    }
                                });
                                TextView Cancel = (TextView) dialog.findViewById(R.id.Cancel);
                                Cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                                break;
                            case R.id.Edit:
                                intent = new Intent(context,EditBloodDonorsActivity.class);
                                intent.putExtra("PhoneNumber",PhoneNumber);
                                context.startActivity(intent);
                                break;
                            case R.id.Public:
                                contentValues = new ContentValues();
                                contentValues.put("isPublic",1);
                                contentValues.put("isUploaded", 0);
                                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                        PhoneNumber),contentValues,null,null);
                                intent = new Intent(context,UploadBloodDonorService.class);
                                context.startService(intent);
                                break;
                            case R.id.Private:
                                contentValues = new ContentValues();
                                contentValues.put("isPublic",0);
                                contentValues.put("isUploaded", 0);
                                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                        PhoneNumber),contentValues,null,null);
                                intent = new Intent(context,UploadBloodDonorService.class);
                                context.startService(intent);
                                break;
                            case R.id.Default:
                                contentValues = new ContentValues();
                                contentValues.put("isDefault",0);
                                context.getContentResolver().update(DatabaseContentProvider.BLOODDONORS_URI,contentValues,null,null);
                                contentValues.put("isDefault",1);
                                context.getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" +
                                        PhoneNumber),contentValues,null,null);
                                SharedPreferences sharedPreferences = context.getSharedPreferences("QuickBlood",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DefaultPhoneNumber",PhoneNumber);
                                editor.apply();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                return true;
            }
        });
        ScaleAnimation scaleAnimation =
                new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation =
                new AlphaAnimation(0.0f,1.0f);
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(400);
        customView.startAnimation(animationSet);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_blooddonors,parent,false);
    }
}
