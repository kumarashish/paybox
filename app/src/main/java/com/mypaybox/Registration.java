package com.mypaybox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import common.ApiCall;
import common.Common;
import common.Utils;
import login.Login;

/**
 * Created by Honey Singh on 4/5/2017.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener {
    EditText email, password, mobile, confirmpassword,name;
    Button register;
    ProgressDialog pd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initializeAll();
    }

    public void initializeAll() {
        Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        name= (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);
        mobile = (EditText) findViewById(R.id.mobile);
        confirmpassword = (EditText) findViewById(R.id.confirmPassword);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        register.setTypeface(typeface);
        name.setTypeface(typeface);
        email.setTypeface(typeface);
        password.setTypeface(typeface);
        mobile.setTypeface(typeface);
        confirmpassword.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        if (isFieldsValidated()) {
            if (Utils.isInternetAvailable(Registration.this)) {
               new RegistrationApi().execute();
            } else {
                Toast.makeText(Registration.this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public boolean isFieldsValidated() {
        if(name.getText().length()>0)
        {
            if (email.getText().length() > 0) {
                if (Utils.isEmailIdValidated(email.getText().toString())) {
                    if (password.getText().length() > 0) {
                        if (Utils.isPasswordValid(password.getText().toString())) {
                            if (confirmpassword.getText().length() > 0) {
                                if (Utils.isPassword_ConfirmPasswordSame(password.getText().toString(), confirmpassword.getText().toString())) {
                                    if (mobile.getText().length() > 0) {
                                        if (Utils.isMobileNumberValid(mobile.getText().toString())) {
                                            return true;
                                        } else {
                                            Toast.makeText(Registration.this, "Mobile number should be of 10 digits.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Registration.this, "Please enter mobile number.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Registration.this, "Password and confirm password need to be same.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Registration.this, "Please enter confirm password.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Registration.this, "Password length should be greater than 2 digits.", Toast.LENGTH_SHORT).show();
                             }
                    } else {
                        Toast.makeText(Registration.this, "Please enter password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Registration.this, "Please enter valid email Id", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Registration.this, "Please enter email Id", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(Registration.this, "Please enter Your Name", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class RegistrationApi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Registration.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = ApiCall.postData(Common.registartionUrl,getRegistrationJsonData());
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("true")) {
                Toast.makeText(Registration.this, "Registered Sucessfully.", Toast.LENGTH_SHORT).show();
                navigateToNextClass();
            } else {
                Toast.makeText(Registration.this, s, Toast.LENGTH_SHORT).show();
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public void navigateToNextClass() {
        Intent in = new Intent(Registration.this, Login.class);
        startActivity(in);
        finish();
    }

    public JSONObject getRegistrationJsonData() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Name", name.getText().toString());
            jsonObject.put("Email", email.getText().toString());
            jsonObject.put("Password", password.getText().toString());
            jsonObject.put("ConfirmPassword", confirmpassword.getText().toString());
            jsonObject.put("MobileNumber", mobile.getText().toString());
            jsonObject.put("Address","  ");

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }
}
