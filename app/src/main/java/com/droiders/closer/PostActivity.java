package com.droiders.closer;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.droiders.closer.Users.community;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class PostActivity extends AppCompatActivity {
private Spinner mPostSpinner;
    private EditText postEditText;
    private boolean isRequest=false;
    private boolean isShared=false;
    private String postBody="";
    private String communityName;
    private String mId;
    private FloatingActionButton fab;

    private MobileServiceClient mClient;
    private MobileServiceTable<community> mCommTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mPostSpinner=(Spinner) findViewById(R.id.postTypeSpinner);
        postEditText=(EditText) findViewById(R.id.postBody);
        postBody=postEditText.getText().toString();
        setupSpinner();
        communityName = getIntent().getStringExtra("CommunityName");
        fab = (FloatingActionButton) findViewById(R.id.goToNoticesFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!postBody.equals("")){
                community item = new community(mId,isRequest,isShared,postBody);
                pushToTable(item);
            }else{
                    Toast.makeText(PostActivity.this,"Post field cannot be empty",Toast.LENGTH_SHORT);
                }
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

    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter postSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_post_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        postSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mPostSpinner.setAdapter(postSpinnerAdapter);
        postEditText=(EditText) findViewById(R.id.postBody);
        // Set the integer mSelected to the constant values
        mPostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    int pos=mPostSpinner.getSelectedItemPosition();
                    switch(pos){
                        case 1: isRequest=true;break;
                        case 2: isShared=true;break;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //postType = null; // Unknown
            }
        });
    }

    private void createTable(){
        // Get the Mobile Service Table instance to use
        mCommTable = mClient.getTable(communityName,community.class);
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
    private void pushToTable(community usersItem){
        createTable();
        insertRow(usersItem);
    }
    public void insertRow(community userItem){
        final community item=userItem;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final community entity = mCommTable.insert(item).get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }
}
