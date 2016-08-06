package com.tathva.falalurahman.quickblood;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;
import java.util.regex.Pattern;

public class EditBloodDonorsActivity extends AppCompatActivity {

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
    boolean nameError=false, bloodGroupError=false, districtError=false, emailError = false, dobError = false, weightError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blood_donors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        final Cursor cursor = getContentResolver().query(Uri.parse(DatabaseContentProvider.BLOODDONORS_URI + "/" + phoneNumber),
                null,null,null,null);
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

        dobPicker = (MaterialEditText) findViewById(R.id.dob_picker);
        Long DateOfBirth = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_DATEOFBIRTH)));
        dob = Calendar.getInstance();
        dob.setTimeInMillis(DateOfBirth);
        dobPicker.setText(Integer.toString(dob.get(Calendar.DAY_OF_MONTH)) + " / " + Integer.toString(dob.get(Calendar.MONTH) + 1) + " / " + Integer.toString(dob.get(Calendar.YEAR)));
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
                datePickerDialog.vibrate(false);
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        weightEditText.requestFocus();
                    }
                });
                datePickerDialog.show(getFragmentManager(),"DatePickerDialog");
            }
        });

        lastDonatedPicker = (MaterialEditText) findViewById(R.id.ld_picker);
        Long LastDonatedTime = Long.parseLong(cursor.getString(cursor.getColumnIndex(TableBloodDonor.COLUMN_BLOODDONATEDTIME)));
        lastDonated = Calendar.getInstance();
        lastDonated.setTimeInMillis(LastDonatedTime);
        if(lastDonated.getTimeInMillis()!=0) {
            lastDonatedPicker.setText(Integer.toString(lastDonated.get(Calendar.DAY_OF_MONTH)) + " / " + Integer.toString(lastDonated.get(Calendar.MONTH) + 1) + " / " + Integer.toString(lastDonated.get(Calendar.YEAR)));
        }
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
                    }
                });
                datePickerDialog.vibrate(false);
                datePickerDialog.show(getFragmentManager(),"DatePickerDialog");
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

        weightEditText = (MaterialEditText) findViewById(R.id.weight_edit_text);
        weightEditText.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(TableBloodDonor.COLUMN_WEIGHT))));
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
                            contentValues.put("DateOfBirth",dobString);
                            contentValues.put("Weight",Integer.parseInt(weight));
                            contentValues.put("Email", email);
                            contentValues.put("BloodGroup", bloodGroup);
                            contentValues.put("District", district);
                            contentValues.put("BloodDonatedTime",lastDonatedString);
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
