package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.droiders.closer.Users.users;
import com.facebook.AccessToken;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;
    private MobileServiceTable<users> mToDoTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(isLoggedIn()) {

            try {
                // Create the Mobile Service Client instance, using the provided
                // Mobile Service URL and key
                mClient = new MobileServiceClient(
                        "https://droidersapp.azurewebsites.net",
                        this);

                // Extend timeout from default of 10s to 20s
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
                pullFromTable();

                //Authenticate User and Sign in via fb
                //authenticate();
            } catch (MalformedURLException e) {
                createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
            } catch (Exception e) {
                createAndShowDialog(e, "Error");
            }
        }else {
            Intent i=new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    /*public List<users> pullFromTable() throws MobileServiceException, ExecutionException, InterruptedException{
        List<users> result;
        AsyncTask<Void, Void, List<users>> task = new AsyncTask<Void, Void, List<users>>(){
            @Override
            protected List<users> doInBackground(Void... params) {
                List<users> result=null;
                try {
                    result = mToDoTable.execute().get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return result;
            }
            @Override
            protected void onPostExecute(List<users> list) {
                super.onPostExecute(list);
                String nn = "";
                for (users item:list){
                    nn+=item.getName()+"**";
                }
                Toast.makeText(MainActivity.this,nn,Toast.LENGTH_LONG).show();
            }
        };
        result = task.execute().get();
        return result;
    }*/
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
                                top+=item.getName()+"**";
                            }
                            Toast.makeText(MainActivity.this,top,Toast.LENGTH_LONG).show();
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
        return mToDoTable.execute().get();
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
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

/*    private void authenticate(){
        if(AccessToken.getCurrentAccessToken()!=null)
            mClient.login(MobileServiceAuthenticationProvider.Facebook, AccessToken.getCurrentAccessToken().getToken());
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.viewAccount) {
            gotoActivity(ProfileActivity.class);
        } else if (id == R.id.viewCommunities) {
            gotoActivity(CommunityInfoDisplayActivity.class);
        } else if (id == R.id.createCommunity) {
            gotoActivity(CreateCommunityActivity.class);
        } else if (id == R.id.feedback) {

        } else if (id == R.id.findPeople) {

        } else if (id == R.id.share) {

        } else if (id == R.id.signOut ) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    //Returns true if Logged in
    private boolean isLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }

    private void gotoActivity(Class cls){
        Intent i=new Intent(MainActivity.this,cls);
        startActivity(i);
    }

}
