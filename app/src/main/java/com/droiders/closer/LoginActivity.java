package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.droiders.closer.Users.UserInfo;
import com.droiders.closer.Users.users;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    /**
     * Mobile Service Client reference
     */


    //UserInfo object person stores details of logged in user
    UserInfo userInfo=new UserInfo();

    private MobileServiceClient mClient;
    private MobileServiceTable<users> mToDoTable;
    private String mId;
    //Button to login in
    private LoginButton loginButton;
    private DBHandler myDB;

    //Callback for fb
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myDB=DBHandler.getInstance(this);
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

        //Log in
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
                                mId=userInfo.getId();
                                //pulls data and takes to espective activites
                                pullFromTable(userInfo);
                            }
                        });
                Bundle parameters = new Bundle();
                //field values
                parameters.putString("fields", "id,name,cover,link,gender,picture{url},verified,friends,email");
                request.setParameters(parameters);
                request.executeAsync();
                Toast.makeText(LoginActivity.this,"SUCCESS",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"CANCEL",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this,"ERROR",Toast.LENGTH_LONG).show();
                Log.e("ERROR",error.getMessage());
            }
        });
        //============================================================


    }
    private boolean isUser=false;
    private void createTable(){
        // Get the Mobile Service Table instance to use
        mToDoTable = mClient.getTable(users.class);
    }


    private void pullFromTable(UserInfo userInfo) {
        final UserInfo temp = userInfo;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<users> results = refreshItemsFromMobileServiceTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (users item : results) {
                                isUser=true;
                                myDB.addme(new UserInfo(item.getId(),item.getName(),item.getFburl(),item.getSex(),
                                        null,true,null,item.getEmail(),null));
                            }
                            if(isUser){
                                Intent i =new Intent(LoginActivity.this,MainActivity.class);

                                i.putExtra("id",mId);

                                startActivity(i);
                                finish();
                            }else{
                                Intent i=new Intent(LoginActivity.this,EditProfileActivity.class);
                                i.putExtra("id",temp.getId());
                                i.putExtra("Name",temp.getName());
                                i.putExtra("Email",temp.getEmail());
                                i.putExtra("Gender",temp.getGender());
                                i.putExtra("FbUrl",temp.getLink());
                                i.putExtra("UserId",temp.getId());
                                i.putExtra("PictureUrl",temp.getPicture().getData().getUrl());
                                startActivity(i);
                                finish();}
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
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
