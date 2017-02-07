package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.droiders.closer.Users.users;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class ProfileActivity extends AppCompatActivity{
private String mId;
    private MobileServiceClient mClient;
    private MobileServiceTable<users> mToDoTable;
    private TextView contactTextView,gender,emailTextView,facebookAddressTextView,addressTextView,dateOfBirthTextView,professionTextView,skillTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


         contactTextView = (TextView) findViewById(R.id.contactTextView);
         gender = (TextView) findViewById(R.id.gender);
         emailTextView = (TextView) findViewById(R.id.emailTextView);
         facebookAddressTextView = (TextView) findViewById(R.id.facebookAddressTextView);
         addressTextView = (TextView) findViewById(R.id.addressTextView);
         dateOfBirthTextView = (TextView) findViewById(R.id.dateOfBirthTextView);
         professionTextView = (TextView) findViewById(R.id.professionTextView);
         skillTextView = (TextView) findViewById(R.id.skillTextView);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button displayCommunityInfoButton = (Button) findViewById(R.id.displayCommunity);
        displayCommunityInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,CommunityInfoDisplayActivity.class);
                startActivity(intent);
            }
        });
        setupProfile();

        Button createCommunityButton = (Button) findViewById(R.id.createCommunity);
        createCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,CreateCommunityActivity.class);
                startActivity(intent);
            }
        });
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
        mId=getIntent().getStringExtra("id");
        pullFromTable();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*Uri uri = Uri.parse("content://com.android.contacts/data/" + number);

                String smsText = "Hii there!!";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, smsText);
                sendIntent.setType("text/plain");

                startActivity(sendIntent);
                Intent.createChooser(sendIntent,"abcd");*/





                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    String top="";
    private void pullFromTable() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<users> results = refreshItemsFromMobileServiceTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (users item : results) {
                                //top+=item.getName()+"**";
                                contactTextView.setText(item.getMob());
                                //private TextView contactTextView,gender,emailTextView,facebookAddressTextView,addressTextView,dateOfBirthTextView,professionTextView,skillTextView;
                                gender.setText(item.getSex());
                                emailTextView.setText(item.getEmail());
                                facebookAddressTextView.setText(item.getFburl());
                                addressTextView.setText(item.getAddr());
                                dateOfBirthTextView.setText(item.getDob());
                                professionTextView.setText(item.getProfession());
                                skillTextView.setText(item.getSkillSet());

                            }
                        }
                    });
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }
    private List<users> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mToDoTable.where().field("id").eq(mId).execute().get();
    }
    private void createTable(){
        // Get the Mobile Service Table instance to use
        mToDoTable = mClient.getTable(users.class);
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

    //Create and Show dialog box
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
    private void setupProfile(){
        contactTextView = (TextView) findViewById(R.id.contactTextView);
        gender = (TextView) findViewById(R.id.gender);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        facebookAddressTextView = (TextView) findViewById(R.id.facebookAddressTextView);
         addressTextView = (TextView) findViewById(R.id.addressTextView);
         dateOfBirthTextView = (TextView) findViewById(R.id.dateOfBirthTextView);
          professionTextView = (TextView) findViewById(R.id.professionTextView);
          skillTextView = (TextView) findViewById(R.id.skillTextView);


    }

}
