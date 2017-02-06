package com.droiders.closer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditProfileActivity extends AppCompatActivity {
    private Spinner mBloodGroupSpinner;
    String homeContact , workContact , homeEmail , workEmail , homeAddress , workAddress , mBloodGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroup);

        setupSpinner();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();

                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);



            }
        });

    }
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mBloodGroupSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("A+")) {
                        mBloodGroup = "A+";
                    } else if (selection.equals("A-")) {
                        mBloodGroup = "A-";
                    }
                    else if (selection.equals("B+")) {
                        mBloodGroup = "B+";
                    }
                    else if (selection.equals("B-")) {
                        mBloodGroup = "B-";
                    }
                    else if (selection.equals("AB+")) {
                        mBloodGroup = "AB+";
                    }
                    else if (selection.equals("AB-")) {
                        mBloodGroup = "AB-";
                    }
                    else if (selection.equals("O+")) {
                        mBloodGroup = "O+";
                    }
                    else if (selection.equals("A-")) {
                        mBloodGroup = "O-";
                    }
                    else {
                        mBloodGroup = null;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBloodGroup = null; // Unknown
            }
        });
    }

    private void updateProfile(){
        EditText homeContactEditText = (EditText) findViewById(R.id.homeContactNumber);
        EditText workContactEditText = (EditText) findViewById(R.id.workContactNumber);
        EditText homeEmailEditText = (EditText) findViewById(R.id.homeEmail);
        EditText workEmailEditText = (EditText) findViewById(R.id.workEmail);
        EditText homeAddressEditText = (EditText) findViewById(R.id.homeAddress);
        EditText workAddressEditText = (EditText) findViewById(R.id.workAddress);



        homeContact = homeContactEditText.getText().toString();
        workContact = workContactEditText.getText().toString();
        homeEmail = homeEmailEditText.getText().toString();
        workEmail = workEmailEditText.getText().toString();
        homeAddress = homeAddressEditText.getText().toString();
        workAddress = workAddressEditText.getText().toString();
    }
}
