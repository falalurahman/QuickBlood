package com.tathva.falalurahman.quickblood;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Pattern;

public class RegisterDonor extends AppCompatActivity {

    MaterialEditText nameEditText;
    MaterialEditText phoneNumberEditText;
    MaterialEditText emailEditText;
    MaterialBetterSpinner bloodGroupSpinner;
    MaterialBetterSpinner districtSpinner;
    Button signUpButton;
    RadioGroup ContactInfoRadioGroup;

    String name, phoneNumber, email, bloodGroup, district;
    boolean nameError=true, phoneNumberError=true, bloodGroupError=true, districtError=true, emailError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_donor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ContactInfoRadioGroup = (RadioGroup) findViewById(R.id.contactInfoRadioGroup);

        bloodGroupSpinner = (MaterialBetterSpinner) findViewById(R.id.blood_group_spinner);
        final ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.blood_group_array));
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);
        bloodGroupSpinner.setError("Select Blood Group");
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
        final ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.district_array));
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setError("Select District");
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
        phoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    phoneNumber = phoneNumberEditText.getText().toString();
                    if(phoneNumber.length()==0) {
                        phoneNumberError=true;
                    }else if(phoneNumber.length()!=10){
                        phoneNumberError=true;
                        phoneNumberEditText.setError("Invalid Phone Number");
                    }else {
                        phoneNumberError=false;
                    }
                }
            }
        });

        emailEditText = (MaterialEditText) findViewById(R.id.email_edit_text);
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
                name = nameEditText.getText().toString();
                if(name.length()<4||name.matches(".*\\d.*")){
                    nameError=true;
                    nameEditText.setError("Invalid Name");
                }else {
                    nameError=false;
                }
                phoneNumber = phoneNumberEditText.getText().toString();
                if(phoneNumber.length()!=10){
                    phoneNumberError=true;
                    phoneNumberEditText.setError("Invalid Phone Number");
                }else {
                    phoneNumberError=false;
                }
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                email = emailEditText.getText().toString();
                if(email.length() == 0){
                    emailError = false;
                }else if(pattern.matcher(email).matches()){
                    emailError = false;
                }else {
                    emailError = true;
                    emailEditText.setError("Invalid Email Address");
                }
                if(!bloodGroupError){
                    bloodGroup = bloodGroupSpinner.getText().toString();
                }else{
                    bloodGroupError=true;
                    bloodGroupSpinner.setError("Select Blood Group");
                }
                if(!districtError){
                    district=districtSpinner.getText().toString();
                }else {
                    districtError=true;
                    districtSpinner.setError("Select District");
                }
                if(!nameError && !phoneNumberError && !emailError && !bloodGroupError && !districtError){
                    int radioButtonId = ContactInfoRadioGroup.getCheckedRadioButtonId();
                    ContentValues contentValues = new ContentValues();
                    switch (radioButtonId){
                        case R.id.public_radio:
                            contentValues.put("isPublic",1);
                            break;
                        case R.id.private_radio:
                            contentValues.put("isPublic",0);
                            break;
                        default:
                            Log.e("QB","Invalid Choice");
                    }
                    contentValues.put("Name",name);
                    contentValues.put("PhoneNumber",phoneNumber);
                    contentValues.put("Email",email);
                    contentValues.put("BloodGroup",bloodGroup);
                    contentValues.put("District",district);
                    contentValues.put("isUploaded",0);
                    contentValues.put("BloodDonatedTime","0");
                    contentValues.put("isDefault",0);
                    getContentResolver().insert(DatabaseContentProvider.BLOODDONORS_URI,contentValues);
                    Intent intent = new Intent(RegisterDonor.this,UploadBloodDonorService.class);
                    startService(intent);
                    Intent intent1 = new Intent(RegisterDonor.this,DownloadRequestService.class);
                    startService(intent1);
                    Intent intent2 = new Intent(RegisterDonor.this,BloodDonorsActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                }
            }
        });

    }

}
