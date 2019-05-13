package com.mypaybox;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import common.ApiCall;
import common.Common;
import common.Utils;

/**
 * Created by Honey Singh on 5/21/2017.
 */

public class ChangePassword  extends AppCompatActivity implements View.OnClickListener {
    public static String heading = "";
    ImageView back;
    TextView headingView;
    ProgressDialog pd;
    EditText oldPassword,newPassword,confirmPassword;
    Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        initializeAll();
    }

    public void initializeAll() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        oldPassword=(EditText)findViewById(R.id.oldpassword);
        newPassword=(EditText)findViewById(R.id.newPassword);
        confirmPassword=(EditText)findViewById(R.id.confirmPassword);
        submit=(Button)findViewById(R.id.submit);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        headingView.setTypeface(typeface);
        oldPassword.setTypeface(typeface);
        newPassword.setTypeface(typeface);
        confirmPassword.setTypeface(typeface);
//        if(Utils.isInternetAvailable(SellerList.this))
//        {
//            new SellerList.GetData().execute();
//        }else{
//            Toast.makeText(SellerList.this,"Internet Unavailable",Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == back.getId()) {
            onBackPressed();
        }
        else if(v.getId()==submit.getId())
        {
            if (validateFields()) {
            new WebAPI().execute();
            }
        }
    }

    public boolean validateFields() {
        boolean status = false;
        if (oldPassword.getText().length() > 0) {
            if (newPassword.length() > 0) {
                if (Utils.isPasswordValid(newPassword.getText().toString())) {
                    if (confirmPassword.getText().length() > 0) {
                        if ((newPassword.getText().toString()).equalsIgnoreCase(confirmPassword.getText().toString())) {
                            status = true;
                        } else {
                            Toast.makeText(ChangePassword.this, "Neew Password and Confirm password need to be same.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(ChangePassword.this, "Please confirm password.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(ChangePassword.this, "Password length should be greater than 2 digits.", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(ChangePassword.this, "Please enter new  password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePassword.this, "Please enter old password", Toast.LENGTH_SHORT).show();
        }
        return status;
    }
public JSONObject getChangePasswordJson()
{
    JSONObject job=new JSONObject();
    try{
        job.put("UserEmail", Common.UserName);
        job.put("OldPassword", oldPassword.getText().toString());
        job.put("NewPassword", newPassword.getText().toString());
        job.put("ConfirmPassword", newPassword.getText().toString());
    }catch (Exception ex)
    {
        ex.fillInStackTrace();
    }
    return job;
}


    public class WebAPI extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ChangePassword.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";

                response = ApiCall.postData(Common.changePassword,getChangePasswordJson());


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("true"))
            {
                Toast.makeText(ChangePassword.this,"Password updated sucessfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                    Toast.makeText(ChangePassword.this,s, Toast.LENGTH_SHORT).show();
            }

        if(pd!=null)
        {
            pd.cancel();
        }
        }
    }
}

