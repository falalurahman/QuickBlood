package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Pattern;

public class EditBloodDonorsActivity extends AppCompatActivity {

    MaterialEditText nameEditText;
    MaterialEditText phoneNumberEditText;
    MaterialEditText emailEditText;
    MaterialBetterSpinner bloodGroupSpinner;
    MaterialBetterSpinner districtSpinner;
    Button signUpButton;
    RadioGroup ContactInfoRadioGroup;

    String name, phoneNumber, email, bloodGroup, district;
    boolean nameError=false, bloodGroupError=false, districtError=false, emailError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blood_donors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        String[] projection = {TableBloodDonor.COLUMN_ID,TableBloodDonor.COLUMN_NAME,TableBloodDonor.COLUMN_PHONENUMBER,TableBloodDonor.COLUMN_EMAIL,
                TableBloodDonor.COLUMN_DISTRICT,TableBloodDonor.COLUMN_BLOODGROUP,TableBloodDonor.COLUMN_BLOODDONATEDTIME,
                TableBloodDonor.COLUMN_ISPUBLIC,TableBloodDonor.COLUMN_DEFAULT};
        final Cursor cursor = getContentResolver().query(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" + phoneNumber),
                projection,null,null,null);
        cursor.moveToFirst();
        ContactInfoRadioGroup = (RadioGroup) findViewById(R.id.contactInfoRadioGroup);
        if(cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_ISPUBLIC))==1)
            ContactInfoRadioGroup.check(R.id.public_radio);
        else
            ContactInfoRadioGroup.check(R.id.private_radio);

        bloodGroupSpinner = (MaterialBetterSpinner) findViewById(R.id.blood_group_spinner);
        final ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.blood_group_array));
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);
        bloodGroupSpinner.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODGROUP)));
        bloodGroupSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bloodGroupError=false;
            }
        });

        districtSpinner = (MaterialBetterSpinner) findViewById(R.id.district_spinner);
        final ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.district_array));
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DISTRICT)));
        districtSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                districtError=false;
            }
        });

        nameEditText = (MaterialEditText) findViewById(R.id.name_edit_text);
        nameEditText.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_NAME)));
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    name = nameEditText.getText().toString();
                    if(name.length()==0){
                        nameError=true;
                    }else if(name.length()<4||name.matches(".*\\d.*")){
                        nameError=true;
                        nameEditText.setError("Invalid Name");
                    }else {
                        nameError=false;
                    }
                }
            }
        });

        phoneNumberEditText = (MaterialEditText) findViewById(R.id.phone_number_edit_text);
        phoneNumberEditText.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_PHONENUMBER)));

        emailEditText = (MaterialEditText) findViewById(R.id.email_edit_text);
        emailEditText.setText(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_EMAIL)));
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if(!hasFocus){
                    email = emailEditText.getText().toString();
                    if(email.length() == 0){
                        emailError = false;
                    }else if(pattern.matcher(email).matches()){
                        emailError = false;
                    }else {
                        emailError = true;
                        emailEditText.setError("Invalid Email Address");
                    }
                }
            }
        });

        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(EditBloodDonorsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog);
                TextView DialogTitle = (TextView) dialog.findViewById(R.id.DialogTitle);
                DialogTitle.setText("Confirm Changes");
                TextView DialogMessage = (TextView) dialog.findViewById(R.id.DialogMessage);
                DialogMessage.setText("Do you add changes to this Blood Donor's details?");
                TextView Confirm = (TextView) dialog.findViewById(R.id.Confirm);
                Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = nameEditText.getText().toString();
                        if (name.length() < 4 || name.matches(".*\\d.*")) {
                            nameError = true;
                            nameEditText.setError("Invalid Name");
                        } else {
                            nameError = false;
                        }
                        Pattern pattern = Patterns.EMAIL_ADDRESS;
                        email = emailEditText.getText().toString();
                        if (email.length() == 0) {
                            emailError = false;
                        } else if (pattern.matcher(email).matches()) {
                            emailError = false;
                        } else {
                            emailError = true;
                            emailEditText.setError("Invalid Email Address");
                        }
                        if (!bloodGroupError) {
                            bloodGroup = bloodGroupSpinner.getText().toString();
                        } else {
                            bloodGroupError = true;
                            bloodGroupSpinner.setError("Select Blood Group");
                        }
                        if (!districtError) {
                            district = districtSpinner.getText().toString();
                        } else {
                            districtError = true;
                            districtSpinner.setError("Select District");
                        }
                        if (!nameError && !emailError && !bloodGroupError && !districtError) {
                            int radioButtonId = ContactInfoRadioGroup.getCheckedRadioButtonId();
                            ContentValues contentValues = new ContentValues();
                            switch (radioButtonId) {
                                case R.id.public_radio:
                                    contentValues.put("isPublic", 1);
                                    break;
                                case R.id.private_radio:
                                    contentValues.put("isPublic", 0);
                                    break;
                                default:
                                    Log.e("QB", "Invalid Choice");
                            }
                            contentValues.put("Name", name);
                            contentValues.put("Email", email);
                            contentValues.put("BloodGroup", bloodGroup);
                            contentValues.put("District", district);
                            contentValues.put("isUploaded", 0);
                            getContentResolver().update(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" + phoneNumber), contentValues, null, null);
                            Intent intent = new Intent(EditBloodDonorsActivity.this,UploadBloodDonorService.class);
                            startService(intent);
                            Intent intent1 = new Intent(EditBloodDonorsActivity.this,DownloadRequestService.class);
                            startService(intent1);
                            Intent intent2 = new Intent(EditBloodDonorsActivity.this,BloodDonorsActivity.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent2);
                            dialog.dismiss();
                        }
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
    }

}
