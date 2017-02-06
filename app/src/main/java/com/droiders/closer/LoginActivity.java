package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    //UserInfo object person stores details of logged in user
    UserInfo userInfo;

    //Button to login in
    private LoginButton loginButton;

    //Callback for fb
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                                Intent i=new Intent(LoginActivity.this,EditProfileActivity.class);
                                i.putExtra("Name",userInfo.getName());
                                i.putExtra("Email",userInfo.getEmail());
                                i.putExtra("Gender",userInfo.getGender());
                                i.putExtra("FbUrl",userInfo.getLink());
                                i.putExtra("UserId",userInfo.getId());
                                i.putExtra("PictureUrl",userInfo.getPicture().getData().getUrl());

                                startActivity(i);
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
