package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.droiders.closer.Users.UserInfo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    /*//UserInfo object person stores details of logged in user
    UserInfo userInfo;

    //Button to login in
    private LoginButton loginButton;

    //Callback for fb
    CallbackManager callbackManager;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Usual Stuff
        //*******************************************************************
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
        //*******************************************************************

        /*//Log in
        //============================================================
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        //Read permissions-fb profile
        loginButton.setReadPermissions("public_profile","user_friends","email");
        //Callback method
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //Request to get profile info
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Gson gson = new GsonBuilder().create();
                                userInfo= gson.fromJson(object.toString(),UserInfo.class);
                                createAndShowDialog(userInfo.getEmail(),"Title");
                            }
                        });
                Bundle parameters = new Bundle();
                //field values
                parameters.putString("fields", "id,name,cover,link,gender,picture{url},verified,friends,email");
                request.setParameters(parameters);
                request.executeAsync();
                Toast.makeText(MainActivity.this,"SUCCESS",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,"CANCEL",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_LONG).show();
            }
        });
        //============================================================*/

        if(isLoggedIn()){
            //************************************************************
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

                //Authenticate User and Sign in via fb
                authenticate();
            } catch (MalformedURLException e) {
                createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
            } catch (Exception e){
                createAndShowDialog(e, "Error");
            }
            //************************************************************
        }
        else {
            Intent i=new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }



    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/

    private void createTable(){
        // Get the Mobile Service Table instance to use

     //   mToDoTable = mClient.getTable(ToDoItem.class);

        // Offline Sync
        //mToDoTable = mClient.getSyncTable("ToDoItem", ToDoItem.class);

     //   mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);

        // Create an adapter to bind the items with the view
     //   mAdapter = new ToDoItemAdapter(this, R.layout.row_list_to_do);
     //   ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
     //   listViewToDo.setAdapter(mAdapter);

        // Load the items from the Mobile Service
    //    refreshItemsFromTable();
    }


    private void authenticate(){
        if(AccessToken.getCurrentAccessToken()!=null)
            mClient.login(MobileServiceAuthenticationProvider.Facebook, AccessToken.getCurrentAccessToken().getToken());
    }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

}
