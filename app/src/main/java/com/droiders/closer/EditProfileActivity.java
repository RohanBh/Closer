package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.droiders.closer.Users.UserInfo;
import com.droiders.closer.Users.users;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;


public class EditProfileActivity extends AppCompatActivity {
    private Spinner mBloodGroupSpinner;
    String  mId,imageUrl,mGender,mName ,homeContact  , homeEmail , faceBookUrl , homeAddress , workAddress , mBloodGroup,mProfession,mSkillSet;

    private MobileServiceClient mClient;
    private MobileServiceTable<users> mToDoTable;
    DBHandler myDB;
    EditText homeContactEditText,homeEmailEditText,homeAddressEditText,
            workAddressEditText,professionEditText,skillSetEditText;
    TextView genderEditText,fbUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mClient = new MobileServiceClient(
                    "https://droidersapp.azurewebsites.net",
                    this);
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
            createTable();
        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            createAndShowDialog(e, "Error");
        }

        myDB= DBHandler.getInstance(this);

        //***********************************
        homeContactEditText = (EditText) findViewById(R.id.homeContactNumber);
        homeEmailEditText = (EditText) findViewById(R.id.homeEmail);
        homeAddressEditText = (EditText) findViewById(R.id.homeAddress);
        workAddressEditText = (EditText) findViewById(R.id.dateOfBirth);
        professionEditText = (EditText) findViewById(R.id.profession);
        skillSetEditText = (EditText) findViewById(R.id.skillSet);
        genderEditText = (TextView) findViewById(R.id.genderEditText);
        fbUrlEditText = (TextView) findViewById(R.id.fbUrl);
        //***********************************

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        ImageView profileImageView = (ImageView) findViewById(R.id.profilePicture);

        Intent intent = getIntent();
        mId= intent.getStringExtra("id");
        mName = intent.getStringExtra("Name");
        homeEmail = intent.getStringExtra("Email");
        mGender = intent.getStringExtra("Gender");
        faceBookUrl = intent.getStringExtra("FbUrl");
        //imageUrl = intent.getStringExtra("PictureUrl");
        String imageUrlLarge="https://graph.facebook.com/"+mId+"/picture?width=1000";
        Picasso.with(this).load(imageUrlLarge).resize(1000,1000).centerCrop().into(profileImageView);
        collapsingToolbarLayout.setTitle(mName);

        //Update activity via local db when opened from main
        if(mName==null){
            UserInfo me=new UserInfo();
                   me= myDB.getMe(mId);
            homeContactEditText.setText(me.getMobile());
            homeEmailEditText.setText(me.getEmail());
            homeAddressEditText.setText(me.getAddress());
            workAddressEditText.setText(me.getDob());
            professionEditText.setText(me.getProfession());
            skillSetEditText.setText(me.getSkillset());
            genderEditText.setText(me.getGender());
            fbUrlEditText.setText(me.getLink());
        }

        setEditProfile();

        mBloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroup);

        setupSpinner();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                users userItem = new users(mId,mName,mGender,homeAddress,
                        mBloodGroup,homeContact,workAddress,homeEmail,mProfession,mSkillSet,faceBookUrl);
                pushToTable(userItem);
                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                intent.putExtra("id",mId);
                intent.putExtra("name",mName);
                intent.putExtra("email",homeEmail);
                intent.putExtra("gender",mGender);
                intent.putExtra("addr",homeAddress);
                intent.putExtra("bloodgrp",mBloodGroup);
                intent.putExtra("mob",homeContact);
                intent.putExtra("dob",workAddress);
                intent.putExtra("profession",mProfession);
                intent.putExtra("skillset",mSkillSet);
                intent.putExtra("fburl",faceBookUrl);
                startActivity(intent);
                finish();
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

        homeContact = homeContactEditText.getText().toString();
        homeEmail = homeEmailEditText.getText().toString();
        homeAddress = homeAddressEditText.getText().toString();
        workAddress = workAddressEditText.getText().toString();
        mProfession = professionEditText.getText().toString();
        mSkillSet = skillSetEditText.getText().toString();

        UserInfo me= new UserInfo();
          me= myDB.getMe(mId);
        me.setMobile(homeContact);
        me.setEmail(homeEmail);
        me.setAddress(homeAddress);
        me.setDob(workAddress);
        me.setProfession(mProfession);
        me.setSkillset(mSkillSet);
        myDB.editMe(me);

    }

    private void setEditProfile(){

        fbUrlEditText.setText(faceBookUrl);
        homeEmailEditText.setText(homeEmail);
        genderEditText.setText(mGender);

    }
    private void pushToTable(users usersItem){
        createTable();
        insertRow(usersItem);
    }
    private void createTable(){
        // Get the Mobile Service Table instance to use
        mToDoTable = mClient.getTable(users.class);
    }
    public void insertRow(users userItem){
        final users item=userItem;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final users entity = mToDoTable.insert(item).get();
                } catch (final Exception e) {
                    Log.e("EEEEEEEEEEEEEEEEEE",e.getMessage());
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}
