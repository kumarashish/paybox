package com.mypaybox;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import common.AppController;
import common.Common;
import common.PrefManager;
import login.Login;


/**
 * Created by Ashish.Kumar on 22-01-2018.
 */

public class  Splash extends Activity {
    final int permissionReadExternalStorage=1;
    // Splash screen timer

    private static int SPLASH_TIME_OUT = 3000;
    AppController controller;
    PrefManager prfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        controller=(AppController)getApplicationContext();
        prfs =  controller.getPrefsManger();
        int permissionCheck = ContextCompat.checkSelfPermission(Splash.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Splash.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        permissionReadExternalStorage);
            } else {
                runThread();
            }
        } else {
            runThread();
        }
    }


    public void runThread() {
        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (prfs.getBoolean("LoggedinStatus") == true) {
                    Common.acessToken = prfs.getPrefsString("token");
                    Common.UserName = prfs.getPrefsString("UserName");
                    Common.userId = prfs.getPrefsString("UserId");
                    Common.profilePic=prfs.getPrefsString("profilePic");
                    Common.emailId=prfs.getPrefsString("emailId");
                    Common.loggedInUserType=prfs.getInt("LoggedInUserType");
                    String rememberId=prfs.getPrefsString("RememberEmailId");

                    if(Common.loggedInUserType==1) {
                        Intent i = new Intent(Splash.this, NewDashBoard.class);
                        startActivity(i);
                    }else {
//                        Intent i = new Intent(Splash.this, VendorDashBoard.class);
//                        startActivity(i);
                    }
                } else {
                   // Common.acessToken = "";
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionReadExternalStorage: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (prfs.getBoolean("LoggedinStatus") == true) {
//                        Common.acessToken = prfs.getString("token", "");
//                        Common.UserName = prfs.getString("UserName", "");
//                        Common.userId = prfs.getString("UserId", "");
                        Intent i = new Intent(Splash.this, DashBoard.class);
                        startActivity(i);
                    } else {
//                        Common.acessToken = "";
                        Intent i = new Intent(Splash.this, Login.class);
                        startActivity(i);
                    }
                    // close this activity
                    finish();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }}}