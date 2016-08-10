package com.tathva.falalurahman.quickblood;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
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

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Pattern;

public class BloodDetails extends AppCompatActivity {

    MaterialEditText nameEditText;
    MaterialEditText phoneNumberEditText;
    MaterialEditText emailEditText;
    MaterialBetterSpinner bloodGroupSpinner;
    MaterialBetterSpinner districtSpinner;
    MaterialEditText quantityEditText;
    MaterialEditText addressEditText;
    MaterialEditText detailsEditText;
    Button signUpButton;

    String name, phoneNumber, email, bloodGroup, district, quantity, address, details;
    boolean nameError=true, phoneNumberError=true, bloodGroupError=true, districtError=true, emailError = false, quantityError=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String nameReceived = getIntent().getStringExtra("Name");
        String phoneNumberReceived = getIntent().getStringExtra("PhoneNumber");
        String emailReceived = getIntent().getStringExtra("Email");

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
        if(!nameReceived.equals("")){
            nameEditText.setText(nameReceived);
            nameError = false;
            nameEditText.setEnabled(false);
            nameEditText.setFocusable(false);
            nameEditText.setFocusableInTouchMode(false);
        }
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
        if(!phoneNumberReceived.equals("")){
            phoneNumberEditText.setText(phoneNumberReceived);
            phoneNumberError = false;
            phoneNumberEditText.setEnabled(false);
            phoneNumberEditText.setFocusable(false);
            phoneNumberEditText.setFocusableInTouchMode(false);
        }
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
        if(!nameReceived.equals("")){
            emailEditText.setText(emailReceived);
            emailError = false;
            emailEditText.setEnabled(false);
            emailEditText.setFocusable(false);
            emailEditText.setFocusableInTouchMode(false);
        }
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

        quantityEditText = (MaterialEditText) findViewById(R.id.quantity_edit_text);
        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    quantity = quantityEditText.getText().toString();
                    if(quantity.length()==0) {
                        quantityError=true;
                    }else {
                        quantityError=false;
                    }
                }
            }
        });

        addressEditText = (MaterialEditText) findViewById(R.id.address_edit_text);
        detailsEditText = (MaterialEditText) findViewById(R.id.details_edit_text);

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
                quantity = quantityEditText.getText().toString();
                if(quantity.length()==0){
                    quantityError=true;
                    quantityEditText.setError("Invalid Quantity");
                }else {
                    quantityError=false;
                }
                address = addressEditText.getText().toString();
                details = detailsEditText.getText().toString();
                if(!nameError && !phoneNumberError && !emailError && !bloodGroupError && !districtError && !quantityError){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("TimeStamp",Long.toString(System.currentTimeMillis()));
                    contentValues.put("Name",name);
                    contentValues.put("PhoneNumber",phoneNumber);
                    contentValues.put("Email",email);
                    contentValues.put("BloodGroup",bloodGroup);
                    contentValues.put("District",district);
                    contentValues.put("Volume",Integer.parseInt(quantity));
                    contentValues.put("Address",address);
                    contentValues.put("OtherDetails",details);
                    contentValues.put("isUploaded",0);
                    getContentResolver().insert(DatabaseContentProvider.BLOODREQUESTS_URI,contentValues);
                    Intent intent = new Intent(BloodDetails.this,UploadRequestService.class);
                    startService(intent);
                    Intent intent1 = new Intent(BloodDetails.this,BloodRequestsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.putExtra("newRequest",true);
                    startActivity(intent1);
                }
            }
        });
    }

}
