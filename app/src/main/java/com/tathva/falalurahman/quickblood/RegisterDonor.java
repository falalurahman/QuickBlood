package com.tathva.falalurahman.quickblood;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;
import java.util.regex.Pattern;

public class RegisterDonor extends AppCompatActivity {

    MaterialEditText nameEditText;
    MaterialEditText weightEditText;
    MaterialEditText phoneNumberEditText;
    MaterialEditText emailEditText;
    MaterialEditText dobPicker;
    MaterialEditText lastDonatedPicker;
    MaterialBetterSpinner bloodGroupSpinner;
    MaterialBetterSpinner districtSpinner;
    Button signUpButton;
    RadioGroup ContactInfoRadioGroup;

    String name, phoneNumber, email, bloodGroup, district, weight;
    Calendar dob, lastDonated;
    boolean nameError=true, phoneNumberError=true, bloodGroupError=true, districtError=true, emailError = false, dobError = true, weightError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_donor);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        dobPicker = (MaterialEditText) findViewById(R.id.dob_picker);
        dobPicker.setError("Select Your Date Of Birth");
        dob = Calendar.getInstance();
        dob.setTimeInMillis(0);
        dobPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    dobPicker.setText(Integer.toString(dayOfMonth)+" / "+Integer.toString(monthOfYear+1)+" / "+Integer.toString(year));
                                    dobError = false;
                                    dob = Calendar.getInstance();
                                    dob.set(Calendar.YEAR,year);
                                    dob.set(Calendar.MONTH,monthOfYear);
                                    dob.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                    dob.set(Calendar.HOUR_OF_DAY,0);
                                    dob.set(Calendar.MINUTE,0);
                                    dob.set(Calendar.SECOND,0);
                                    dob.set(Calendar.MILLISECOND,0);
                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.vibrate(false);
                    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            weightEditText.requestFocus();
                            if(dobPicker.getText().equals("")){
                                dobPicker.setError("Select Your Date Of Birth");
                                dobError = true;
                                dob = Calendar.getInstance();
                                dob.setTimeInMillis(0);
                            }
                        }
                    });
                    datePickerDialog.show(getFragmentManager(),"DatePickerDialog");
                }
            }
        });
        dobPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                dobPicker.setText(Integer.toString(dayOfMonth)+" / "+Integer.toString(monthOfYear+1)+" / "+Integer.toString(year));
                                dobError = false;
                                dob = Calendar.getInstance();
                                dob.set(Calendar.YEAR,year);
                                dob.set(Calendar.MONTH,monthOfYear);
                                dob.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                dob.set(Calendar.HOUR_OF_DAY,0);
                                dob.set(Calendar.MINUTE,0);
                                dob.set(Calendar.SECOND,0);
                                dob.set(Calendar.MILLISECOND,0);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        weightEditText.requestFocus();
                        if(dobPicker.getText().equals("")){
                            dobPicker.setError("Select Your Date Of Birth");
                            dobError = true;
                            dob = Calendar.getInstance();
                            dob.setTimeInMillis(0);
                        }
                    }
                });
                datePickerDialog.vibrate(false);
                datePickerDialog.show(getFragmentManager(),"DatePickerDialog");
            }
        });

        lastDonatedPicker = (MaterialEditText) findViewById(R.id.ld_picker);
        lastDonated = Calendar.getInstance();
        lastDonated.setTimeInMillis(0);
        lastDonatedPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    lastDonatedPicker.setText(Integer.toString(dayOfMonth)+" / "+Integer.toString(monthOfYear+1)+" / "+Integer.toString(year));
                                    lastDonated = Calendar.getInstance();
                                    lastDonated.set(Calendar.YEAR,year);
                                    lastDonated.set(Calendar.MONTH,monthOfYear);
                                    lastDonated.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                    lastDonated.set(Calendar.HOUR_OF_DAY,0);
                                    lastDonated.set(Calendar.MINUTE,0);
                                    lastDonated.set(Calendar.SECOND,0);
                                    lastDonated.set(Calendar.MILLISECOND,0);
                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ContactInfoRadioGroup.requestFocus();
                            lastDonatedPicker.setText("");
                            lastDonated = Calendar.getInstance();
                            lastDonated.setTimeInMillis(0);
                        }
                    });
                    datePickerDialog.vibrate(false);
                    datePickerDialog.show(getFragmentManager(),"DatePickerDialog");
                }
            }
        });
        lastDonatedPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                lastDonatedPicker.setText(Integer.toString(dayOfMonth)+" / "+Integer.toString(monthOfYear+1)+" / "+Integer.toString(year));
                                lastDonated = Calendar.getInstance();
                                lastDonated.set(Calendar.YEAR,year);
                                lastDonated.set(Calendar.MONTH,monthOfYear);
                                lastDonated.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                lastDonated.set(Calendar.HOUR_OF_DAY,0);
                                lastDonated.set(Calendar.MINUTE,0);
                                lastDonated.set(Calendar.SECOND,0);
                                lastDonated.set(Calendar.MILLISECOND,0);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ContactInfoRadioGroup.requestFocus();
                        lastDonatedPicker.setText("");
                        lastDonated = Calendar.getInstance();
                        lastDonated.setTimeInMillis(0);
                    }
                });
                datePickerDialog.vibrate(false);
                datePickerDialog.show(getFragmentManager(),"DatePickerDialog");
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

        weightEditText = (MaterialEditText) findViewById(R.id.weight_edit_text);
        weightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    weight = weightEditText.getText().toString();
                    if(weight.length()==0) {
                        weightError=true;
                    }else {
                        weightError=false;
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
                String dobString="0", lastDonatedString;
                if(!dobError){
                    dobString = Long.toString(dob.getTimeInMillis());
                }else {
                    dobError = true;
                    dobPicker.setError("Select Your Date Of Birth");
                }
                if(!weightError){
                    weight = weightEditText.getText().toString();
                }else {
                    weightError = true;
                    weightEditText.setError("Enter Your Weight");
                }
                lastDonatedString = Long.toString(lastDonated.getTimeInMillis());
                if(!nameError && !phoneNumberError && !emailError && !bloodGroupError && !districtError && !weightError && !dobError){
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
                    contentValues.put("DateOfBirth",dobString);
                    contentValues.put("Weight",Integer.parseInt(weight));
                    contentValues.put("PhoneNumber",phoneNumber);
                    contentValues.put("Email",email);
                    contentValues.put("BloodGroup",bloodGroup);
                    contentValues.put("District",district);
                    contentValues.put("isUploaded",0);
                    contentValues.put("BloodDonatedTime",lastDonatedString);
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
