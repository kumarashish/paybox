package login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mypaybox.NewDashBoard;
import com.mypaybox.R;
import com.mypaybox.Registration;
import com.mypaybox.VendorDashBoard;

import org.json.JSONObject;

import common.ApiCall;
import common.AppController;
import common.Common;
import common.PrefManager;
import common.Utils;


/**
 * Created by Ashish.Kumar on 22-01-2018.
 */

public class Login extends Activity  implements View.OnClickListener{

    ProgressDialog pd;
    boolean isSupplierLoginRequested = false;
    int vendor = 2;
    int customer = 1;
    Boolean isForgetPasswordRequested = false;
    EditText emailId,password;
    Button submit;
    CheckBox remember;
    TextView forgetPassword;
    TextView Signup;
    AppController controller;
    PrefManager prfs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeAll();
        if(Utils.isInternetAvailable(Login.this))
        {Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                ApiCall.loginData(new String[]{"test","test"});
            }
        });
            t.start();
        }
    }

    public void initializeAll() {
        controller=(AppController)getApplicationContext();
        prfs =controller.getPrefsManger();
        emailId = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.passwordedt);
        submit = (Button) findViewById(R.id.login);
        remember = (CheckBox) findViewById(R.id.remember);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        Signup = (TextView) findViewById(R.id.signup);
        submit.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        Signup.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        remember.setChecked(true);
        String remeberId= prfs.getPrefsString("RememberEmailId");
        if ((remeberId != null) && (remeberId.length() > 0)) {
            emailId.setText(remeberId);
            emailId.setSelection(remeberId.length());
        }
        password.setTypeface(controller.getTypeface());
        emailId.setTypeface(controller.getTypeface());
    }

    public boolean isFieldsvalidated() {
        if (emailId.getText().length() > 0) {
            if (Utils.isEmailIdValidated(emailId.getText().toString())) {
                if (password.getText().length() > 0) {
                    if (Utils.isPasswordValid(password.getText().toString())) {
                        return true;
                    } else {
                        Toast.makeText(Login.this, "Password lenghth must be greater than 2 digits.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Please enter valid emailId", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Login.this, "Please enter emailId", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login:
                if (isFieldsvalidated() == true) {
                    String email = emailId.getText().toString();
                    if (Utils.isInternetAvailable(Login.this)) {
                        new LoginApi().execute(new String[]{email, password.getText().toString()});
                    } else {
                        Toast.makeText(Login.this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.signup:
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
                break;
            case R.id.forgetPassword:
                if (emailId.getText().length() > 0) {
                    if (Utils.isEmailIdValidated(emailId.getText().toString())) {
                        if (Utils.isInternetAvailable(Login.this)) {
                            isForgetPasswordRequested = true;
                            new LoginApi().execute(new String[]{emailId.getText().toString()});
                        } else {
                            Toast.makeText(Login.this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Please enter valid email id.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Please enter email id in above field.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public class LoginApi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = "";
            if (isForgetPasswordRequested == true) {
                response = ApiCall.getData(Common.forgetPasswordUrl + "" + params[0]);
            } else {
                response = ApiCall.loginData(params);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isForgetPasswordRequested == true) {
                isForgetPasswordRequested = false;
                forgetPasswordResponse(s);
            } else {
                jsonParsing(s);
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public void forgetPasswordResponse(String s) {
        if (s.contains("Password sent on your E-Mail")) {
            Toast.makeText(Login.this, "New password is sent to " + emailId.getText().toString(), Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(s);
                Toast.makeText(Login.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                ex.fillInStackTrace();
            }
        }
    }

    public void navigateToNextClass() {
        if (Common.loggedInUserType == vendor) {
            Intent in = new Intent(Login.this, VendorDashBoard.class);
            startActivity(in);
            finish();
        } else {
            Intent in = new Intent(Login.this, NewDashBoard.class);
            startActivity(in);
            finish();
        }
    }

    public void jsonParsing(String json) {
        try {
            JSONObject job = new JSONObject(json);
            if (!job.isNull("access_token")) {
                Common.UserName = job.getString("userName");
                Common.acessToken = job.getString("access_token");
                Common.userId = job.getString("id");
                Common.emailId = job.getString("email");
                Common.profilePic = job.getString("image");
                prfs.getEditor().putBoolean("LoggedinStatus", true);
                prfs.getEditor().putString("token", Common.acessToken);
                prfs.getEditor().putString("emailId", Common.emailId);
                prfs.getEditor().putString("UserName", job.getString("userName"));
                prfs.getEditor().putString("UserId", job.getString("id"));
                prfs.getEditor().putString("profilePic", job.getString("image"));
                if(!job.isNull("Mobile"))
                {
                    prfs.getEditor().putString("Mobile", job.getString("MobileNumber"));
                }
                if (remember.isChecked()) {
                    prfs.getEditor().putString("RememberEmailId", Common.emailId);
                }
                    if ((job.getString("IsUser").equalsIgnoreCase("true")) || (job.getString("IsApartmentUser").equalsIgnoreCase("true"))) {
                        prfs.getEditor().putInt("LoggedInUserType", customer);
                        if (job.getString("IsApartmentUser").equalsIgnoreCase("true")) {
                            prfs.getEditor().putBoolean("isAppartementUser", true);
                            prfs.getEditor().putString("locality", job.getString("locality"));
                            prfs.getEditor().putString("city", job.getString("city"));
                            Common.isAppartmentUser = true;
                        } else {
                            prfs.getEditor().putBoolean("isAppartementUser", false);
                            Common.isAppartmentUser = false;
                        }
                        Common.loggedInUserType = customer;
                        prfs.getEditor().commit();
                        Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                        navigateToNextClass();

                } else {

                        Common.loggedInUserType = vendor;
                        prfs.getEditor().putInt("LoggedInUserType", vendor);
                        Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                        navigateToNextClass();

                }


            } else if (!job.isNull("error")) {
                Toast.makeText(Login.this, job.getString("error_description"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
}
