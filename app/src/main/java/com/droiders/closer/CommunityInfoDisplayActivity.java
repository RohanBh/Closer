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
import android.view.View;
import android.widget.TextView;

import com.droiders.closer.Users.communities;
import com.droiders.closer.Users.community;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CommunityInfoDisplayActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable<communities> mToDoTable;
    private MobileServiceTable<community> mCommTable;
    private String communityName;
    private String mId;
private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_info_page);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        communityName = i.getStringExtra("CommunityName");
        mId=i.getStringExtra("id");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitle(communityName);
        fab = (FloatingActionButton) findViewById(R.id.goToNoticesFab);


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
        pullFromTable();
    }
private boolean isMember=false;
    private void pullFromTable() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<community> rows = refreshItemsFromMobileServiceTable2();
                    final List<communities> results = refreshItemsFromMobileServiceTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(community item:rows){
                                isMember=true;
                            }
                            for (communities item : results) {
                                TextView communityDescription = (TextView) findViewById(R.id.communityDescription);
                                communityDescription.setText(item.getDescription());
                            }
                            TextView joined = (TextView) findViewById(R.id.joined);
                            if (isMember) {
                                joined.setText("Already a member.");
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(CommunityInfoDisplayActivity.this,ViewPagerActivity.class);
                                        intent.putExtra("CommunityName",communityName);
                                        intent.putExtra("id",mId);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }else {
                                joined.setText("You are not a member");
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        community item = new community(mId,false,false,"");
                                        pushToTable(item);
                                        Intent intent = new Intent(CommunityInfoDisplayActivity.this,ViewPagerActivity.class);
                                        intent.putExtra("CommunityName",communityName);
                                        intent.putExtra("id",mId);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }//

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
    private List<communities> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mToDoTable.where().field("name").eq(communityName).execute().get();
    }
    private List<community> refreshItemsFromMobileServiceTable2() throws ExecutionException, InterruptedException, MobileServiceException {
        return mCommTable.where().field("userid").eq(mId).execute().get();
    }
    private void createTable(){
        // Get the Mobile Service Table instance to use
        mToDoTable = mClient.getTable(communities.class);
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
