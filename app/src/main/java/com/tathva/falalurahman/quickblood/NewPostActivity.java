package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 200;
    ImageView cameraButton;
    ImageView galleryButton;
    ImageView linkButton;
    ImageView uploadImage;
    ImageView removeImage;
    ImageView ProfilePicture;
    LinearLayout postButton;
    Uri fileUri;
    String filePath;
    TextView linkTextView;
    String linkUpload;
    Bitmap image;
    ProgressDialog progressDialog;
    EditText statusEditText;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPoolSize(5)
                .diskCacheExtraOptions(480,320,null)
                .build();
        ImageLoader.getInstance().init(configuration);

        final SharedPreferences sharedPreferences = getSharedPreferences("QuickBlood",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isAdmin",false)){
            final String username = sharedPreferences.getString("Username","");

            ProfilePicture = (ImageView) findViewById(R.id.profile_pic);
            if(username.equals("Tathva 16")){
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.tathva16, ProfilePicture);
            }else if(username.equals("Blood Donation Forum")){
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.blood_donor,ProfilePicture);
            }
            ProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(username.equals("Tathva 16")){
                        Intent intent = new Intent(NewPostActivity.this,AboutUsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if(username.equals("Blood Donation Forum")){
                        Intent intent = new Intent(NewPostActivity.this,BloodDonationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
        }else {
            Intent intent = new Intent(NewPostActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if(getIntent().getBooleanExtra("Photo",false)){
            if(isDeviceSupportCamera()){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri();
                filePath = fileUri.getPath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }else {
                Toast.makeText(getApplicationContext(),"Sorry! Your Device doesn't support camera",Toast.LENGTH_LONG).show();
            }
        }

        statusEditText = (EditText) findViewById(R.id.status_editText);
        statusEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0){
                    if(uploadImage.getVisibility() == View.GONE && linkTextView.getVisibility() == View.GONE)
                        postButton.setEnabled(false);
                }else if(count == 1 ){
                    postButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        uploadImage = (ImageView) findViewById(R.id.upload_image);
        removeImage = (ImageView) findViewById(R.id.removeImage);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage.setVisibility(View.GONE);
                removeImage.setVisibility(View.GONE);
                if(linkTextView.getVisibility() == View.GONE && statusEditText.getText().toString().length() == 0)
                    postButton.setEnabled(false);
                filePath = null;
            }
        });

        cameraButton = (ImageView) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeviceSupportCamera()){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    filePath = fileUri.getPath();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                    startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry! Your Device doesn't support camera",Toast.LENGTH_LONG).show();
                }
            }
        });

        galleryButton = (ImageView) findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALLERY_IMAGE_REQUEST_CODE);
            }
        });

        linkTextView = (TextView) findViewById(R.id.linkTextView);
        linkTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(NewPostActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog);
                TextView DialogTitle = (TextView) dialog.findViewById(R.id.DialogTitle);
                DialogTitle.setText("Remove Link");
                TextView DialogMessage = (TextView) dialog.findViewById(R.id.DialogMessage);
                DialogMessage.setText("Do you want to remove this link?");
                TextView Confirm = (TextView) dialog.findViewById(R.id.Confirm);
                Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linkTextView.setVisibility(View.GONE);
                        linkTextView.setText("");
                        if(uploadImage.getVisibility() == View.GONE && statusEditText.getText().toString().length() == 0)
                            postButton.setEnabled(false);
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
                return true;
            }
        });

        linkButton = (ImageView) findViewById(R.id.linkButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(NewPostActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_link_dialog);
                final EditText DialogLink = (EditText) dialog.findViewById(R.id.DialogLink);
                TextView Add = (TextView) dialog.findViewById(R.id.Add);
                Add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DialogLink.getText().toString().length()!=0) {
                            String link = "Link: ";
                            link = link.concat(DialogLink.getText().toString());
                            linkTextView.setVisibility(View.VISIBLE);
                            linkTextView.setText(link);
                            linkUpload = DialogLink.getText().toString();
                            postButton.setEnabled(true);
                        }
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
            }
        });

        postButton = (LinearLayout) findViewById(R.id.postButton);
        postButton.setEnabled(false);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(NewPostActivity.this);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_drawable));
                if (uploadImage.getVisibility() == View.GONE)
                    progressDialog.setMessage("Posting Your Status...");
                else
                    progressDialog.setMessage("Posting Your Image...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                        requestQueue.cancelAll("NewPostRequest");
                        progressDialog = null;
                    }
                });
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.new_post_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                progressDialog = null;
                                if (response.equals("Success")) {
                                    Toast.makeText(getApplicationContext(), "Status Posted!!!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(NewPostActivity.this, NewPostService.class);
                                    startService(intent);
                                    Intent intent1 = new Intent(NewPostActivity.this, HomeActivity.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent1);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                progressDialog = null;
                                if (error instanceof NoConnectionError) {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error In Connection!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("username", sharedPreferences.getString("Username","Username"));
                        params.put("timeStamp", Long.toString(System.currentTimeMillis()));
                        String status = statusEditText.getText().toString();
                        status = status.replaceAll("#\\w+","<b>$0</b>");
                        status = status.replaceAll("\n","<br>");
                        params.put("status", status );
                        if (uploadImage.getVisibility() == View.VISIBLE) {
                            params.put("image", getStringImage(image));
                        } else {
                            params.put("image", "");
                        }
                        if (linkTextView.getVisibility() == View.VISIBLE) {
                            params.put("link", linkUpload);
                        } else {
                            params.put("link", "");
                        }
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(25 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                stringRequest.setTag("NewPostRequest");
                requestQueue = Volley.newRequestQueue(NewPostActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.ORIENTATION,MediaStore.Images.Media.DATE_ADDED};
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                columnIndex = cursor.getColumnIndex(filePathColumn[1]);
                int rotation = cursor.getInt(columnIndex);
                cursor.close();
                uploadImage.setVisibility(View.VISIBLE);
                BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                imageOptions.inSampleSize = 8;
                image = BitmapFactory.decodeFile(filePath,imageOptions);
                if(rotation!=0){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotation);
                    image = Bitmap.createBitmap(image,0,0,image.getWidth(),image.getHeight(),matrix,true);
                }
                removeImage.setVisibility(View.VISIBLE);
                postButton.setEnabled(true);
                uploadImage.setImageBitmap(image);
            }else if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
                filePath = fileUri.getPath();
                ExifInterface exifInterface = new ExifInterface(filePath);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                int rotation=0;
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotation=270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotation=180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotation=90;
                        break;
                }
                uploadImage.setVisibility(View.VISIBLE);
                BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                imageOptions.inSampleSize = 8;
                image = BitmapFactory.decodeFile(filePath,imageOptions);
                if(rotation!=0){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotation);
                    image = Bitmap.createBitmap(image,0,0,image.getWidth(),image.getHeight(),matrix,true);
                }
                removeImage.setVisibility(View.VISIBLE);
                postButton.setEnabled(true);
                uploadImage.setImageBitmap(image);
            }
        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"Someting went wrong!! Try Again",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isDeviceSupportCamera(){
        return (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    public Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"QuickBloodFileUpload");
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.i("QBError","Cannot Make Directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
        return mediaFile;
    }

    public String getStringImage(Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }

}
