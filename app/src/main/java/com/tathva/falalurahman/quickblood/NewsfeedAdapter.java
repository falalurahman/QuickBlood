package com.tathva.falalurahman.quickblood;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class NewsfeedAdapter extends CursorAdapter{

    public static ArrayList<Bitmap> images;
    Context context;

    public interface FacebookShareListener{
        void onShare(String Status,String Link, Uri ImageURL, boolean isRequest);
    }

    public FacebookShareListener facebookShareListener;

    public NewsfeedAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.context = context;
        images = new ArrayList<>();
    }

    @Override
    public void bindView(View view, final Context context,final Cursor cursor) {
        TextView Username = (TextView) view.findViewById(R.id.username);
        TextView TimeStamp = (TextView) view.findViewById(R.id.timeStamp);
        TextView Status = (TextView) view.findViewById(R.id.status);
        TextView Link = (TextView) view.findViewById(R.id.link);
        final ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageView profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        final ImageView profilePic1 = (ImageView) view.findViewById(R.id.profile_pic1);
        final ProgressBar ImageProgress = (ProgressBar) view.findViewById(R.id.ImageProgress);
        final LinearLayout ShareButton = (LinearLayout) view.findViewById(R.id.shareButton);
        TextView TimestampTextView = (TextView) view.findViewById(R.id.timestamp_textview);
        TextView NameTextView = (TextView) view.findViewById(R.id.name_textview);
        TextView DistrictTextView = (TextView) view.findViewById(R.id.district_textview);
        TextView AddressTextView = (TextView) view.findViewById(R.id.address_textview);
        TextView PhoneNumberTextView = (TextView) view.findViewById(R.id.phonenumber_textview);
        TextView EmailTextView = (TextView) view.findViewById(R.id.email_textview);
        TextView BloodGroupTextView = (TextView) view.findViewById(R.id.bloodgroup_textview);
        TextView QuantityTextView = (TextView) view.findViewById(R.id.quantity_textview);
        final ImageView QuickBloodPic = (ImageView) view.findViewById(R.id.quickbloodPic);
        LinearLayout EmailLayout = (LinearLayout) view.findViewById(R.id.email_layout);
        LinearLayout AddressLayout = (LinearLayout) view.findViewById(R.id.address_layout);
        final RelativeLayout NotificationLayout = (RelativeLayout) view.findViewById(R.id.notificationLayout);
        RelativeLayout ImageLayout = (RelativeLayout) view.findViewById(R.id.imageLayout);

        final String username = cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_USERNAME));
        Username.setText(username);
            if (username.equals("Tathva 16")) {
                ImageLoader.getInstance().displayImage("assets://tathva16.jpg", profilePic);
                profilePic.setTag(true);
            } else if (username.equals("Blood Donors Kerala")) {
                ImageLoader.getInstance().displayImage("assets://blood_donor.jpg", profilePic);
                profilePic.setTag(true);
            } else if (username.equals("QuickBlood Blood Request")) {
            ImageLoader.getInstance().displayImage("assets://logo.png", profilePic);
            profilePic.setTag(true);
        }
        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("Tathva 16")){
                    Intent intent = new Intent(context,AboutUsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else if(username.equals("Blood Donors Kerala")){
                    Intent intent = new Intent(context,BloodDonationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("Tathva 16")){
                    Intent intent = new Intent(context,AboutUsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else if(username.equals("Blood Donors Kerala")){
                    Intent intent = new Intent(context,BloodDonationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        });
        String time = cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_TIMESTAMP));
        TimeStamp.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(time), System.currentTimeMillis() , DateUtils.SECOND_IN_MILLIS));
        final int position = cursor.getPosition();
        try {
            images.set(position,null);
        }catch (IndexOutOfBoundsException exception) {
            images.add(position, null);
        }
        ShareButton.setTag(position);
        if(!username.equals("QuickBlood Blood Request")) {
            String status = cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_STATUS));
            ImageLayout.setVisibility(View.VISIBLE);
            NotificationLayout.setVisibility(View.GONE);
            if (!status.equals("")) {
                Status.setVisibility(View.VISIBLE);
                Status.setText(Html.fromHtml(status));
            } else {
                Status.setVisibility(View.GONE);
            }
            String link = cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_LINK));
            if (!link.equals("")) {
                Link.setVisibility(View.VISIBLE);
                Link.setText("Link: " + link);
            } else {
                Link.setVisibility(View.GONE);
                Link.setText(link);
            }
            String imageAddress = cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_IMAGE));
            if (!imageAddress.equals("")) {
                image.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().loadImage(imageAddress, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            ImageProgress.setVisibility(View.VISIBLE);
                            ImageProgress.setEnabled(true);
                            images.set(position, null);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            ImageProgress.setVisibility(View.GONE);
                            ImageProgress.setEnabled(false);
                            images.set(position, null);
                            ImageLoader.getInstance().displayImage("assets://load_fail.png", image);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ImageProgress.setVisibility(View.GONE);
                            ImageProgress.setEnabled(false);
                            image.setImageBitmap(loadedImage);
                            images.set(position, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            ImageProgress.setVisibility(View.GONE);
                            ImageProgress.setEnabled(false);
                        }
                    });
            } else {
                image.setVisibility(View.GONE);
                ImageProgress.setVisibility(View.GONE);
            }
        }else {
            Status.setVisibility(View.GONE);
            Link.setVisibility(View.GONE);
            ImageLayout.setVisibility(View.GONE);
            NotificationLayout.setVisibility(View.VISIBLE);
            ShareButton.setVisibility(View.VISIBLE);
            try {
                    ImageLoader.getInstance().displayImage("assets://logo_back.png",QuickBloodPic, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            profilePic1.setImageBitmap(loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                String status = cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_STATUS));
                JSONObject jsonObject = new JSONObject(status);
                NameTextView.setText(jsonObject.getString("Name"));
                PhoneNumberTextView.setText(jsonObject.getString("PhoneNumber"));
                DistrictTextView.setText(jsonObject.getString("District"));
                AddressTextView.setText(jsonObject.getString("Address"));
                BloodGroupTextView.setText(jsonObject.getString("BloodGroup"));
                EmailTextView.setText(jsonObject.getString("Email"));
                QuantityTextView.setText(Integer.toString(jsonObject.getInt("Volume")));
                Long RequestTime = Long.parseLong(jsonObject.getString("TimeStamp"));
                DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(RequestTime);
                TimestampTextView.setText(dateFormat.format(calendar.getTime()));
                if(jsonObject.getString("Email").equals("")){
                    EmailLayout.setVisibility(View.GONE);
                }else {
                    EmailLayout.setVisibility(View.VISIBLE);
                }
                if(jsonObject.getString("Address").equals("")){
                    AddressLayout.setVisibility(View.GONE);
                }else {
                    AddressLayout.setVisibility(View.VISIBLE);
                }
                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
                int color = colorGenerator.getColor(time);
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{Color.WHITE,color});
                gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                gradientDrawable.setGradientCenter(0.5f,0.5f);
                gradientDrawable.setGradientRadius(700);
                NotificationLayout.setBackground(gradientDrawable);
            }catch (JSONException exception){
                exception.printStackTrace();
            }
        }
        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<images.size();i++) {
                    if (images.get(i) == null)
                        Log.i("QB1", Integer.toString(i) + " null");
                    else
                        Log.i("QB1", Integer.toString(i) + " present");
                }
                facebookShareListener = (FacebookShareListener) context;
                if(!username.equals("QuickBlood Blood Request")) {
                    cursor.moveToPosition(position);
                    if(images.get(position) != null) {
                        facebookShareListener.onShare(cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_STATUS)),
                                cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_LINK)),
                                Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), images.get(position), "QuickBlood", null)), false);
                    }else {
                        facebookShareListener.onShare(cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_STATUS)),
                                cursor.getString(cursor.getColumnIndex(TableQBPosts.COLUMN_LINK)),
                                null, false);
                    }
                }else {
                    facebookShareListener.onShare("","",Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(),  viewToBitmap(NotificationLayout), "QuickBlood Blood Request", null)),true);
                }
            }
        });
        if(position == cursor.getCount()-1){
            Intent intent = new Intent(context,OldPostService.class);
            context.startService(intent);
        }
    }


    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_new_newsfeed,parent,false);
    }

    public Bitmap viewToBitmap (View view){
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache().copy(Bitmap.Config.ARGB_8888,false);
        view.destroyDrawingCache();
        return bitmap;
    }
}
