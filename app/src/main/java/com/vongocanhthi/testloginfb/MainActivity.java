package com.vongocanhthi.testloginfb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    Button btnDangXuat, btnChucNang;
    TextView txtName, txtEmail, txtFirstName;

    CallbackManager callbackManager;
    String name, email, firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        
        addInit();
        addEvents();
        setLoginButton();
    }

    private void setLoginButton() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                btnChucNang.setVisibility(View.VISIBLE);
                btnDangXuat.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                txtFirstName.setVisibility(View.VISIBLE);

                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON", response.getJSONObject().toString());
                try {
                    name = object.getString("name");
                    //email = object.getString("email");
                    firstname = object.getString("first_name");

                    //profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                    profilePictureView.setProfileId("2501010623511268");
                    txtName.setText(name);
                    //txtEmail.setText(email);
                    txtFirstName.setText(firstname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void addEvents() {
        btnChucNang.setVisibility(View.INVISIBLE);
        btnDangXuat.setVisibility(View.INVISIBLE);
        txtName.setVisibility(View.INVISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);
        txtFirstName.setVisibility(View.INVISIBLE);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
    }

    private void addInit() {
        profilePictureView = findViewById(R.id.friendProfilePicture);
        loginButton = findViewById(R.id.login_button);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnChucNang = findViewById(R.id.btnChucNang);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtFirstName = findViewById(R.id.txtFirstName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();

    }
}
