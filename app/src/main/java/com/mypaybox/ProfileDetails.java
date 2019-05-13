package com.mypaybox;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import common.ApiCall;
import common.AppController;
import common.Common;
import common.MultipartRequest;
import common.Utils;
import model.ProfileDataset;

/**
 * Created by ashish.kumar on 03-05-2017.
 */

public class
ProfileDetails extends Activity implements View.OnClickListener{
    TextView headingView;
    ListView list;
    ImageView back;
    ProgressDialog pd;
    TextView businessName;
    EditText userName,mobile,emailId,address,city,pincode;
    common.CustomImageView profilePic;
    Button submit;
    Button editProfile;
    Boolean isEditEnabled=false;
    final public static int SELECT_FILE = 02;
    Button editImage;
    public final  int permissionReadCamera=1;
    Typeface typeface;
    boolean isImageCaptured=false;
    boolean isUpdateProfileRequested=false;
    boolean isUpdateProfilePicRequested=false;
    boolean isGetProfileRequetsed=false;
    AlertDialog alertDialog=null;
    AppController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        initializeAll();
        int permissionCheck = ContextCompat.checkSelfPermission(ProfileDetails.this,
                Manifest.permission.CAMERA);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileDetails.this,
                        new String[]{Manifest.permission.CAMERA},
                        permissionReadCamera);
            }
        }
    }
    public void initializeAll() {
       Typeface typeface= Typeface.createFromAsset(getAssets(), "font.ttf");
        controller=(AppController)getApplicationContext();
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        profilePic=(common.CustomImageView)findViewById(R.id.profileImage);
        list = (ListView) findViewById(R.id.listView);
        businessName=(TextView)findViewById(R.id.businessName);
        userName=(EditText)findViewById(R.id.userName);
        mobile=(EditText)findViewById(R.id.mobile);
        emailId=(EditText)findViewById(R.id.emailId);
        address=(EditText)findViewById(R.id.address) ;
        city=(EditText)findViewById(R.id.city) ;
        pincode=(EditText)findViewById(R.id.pin) ;
        submit=(Button)findViewById(R.id.submit);
        editImage=(Button)findViewById(R.id.editImage);
        editProfile=(Button) findViewById(R.id.editProfile);
        back.setOnClickListener(this);
        if (Utils.isInternetAvailable(ProfileDetails.this)) {
            isGetProfileRequetsed=true;
            new GetProfile().execute();
        } else {
            Toast.makeText(ProfileDetails.this,"Internet Unavailable.", Toast.LENGTH_SHORT).show();
        }
        businessName.setTypeface(typeface);
        userName.setTypeface(typeface);
        mobile.setTypeface(typeface);
        emailId.setTypeface(typeface);
        address.setTypeface(typeface);
        submit.setTypeface(typeface);
        city.setTypeface(typeface);
        pincode.setTypeface(typeface);
        editProfile.setOnClickListener(this);
        editImage.setOnClickListener(this);
        submit.setOnClickListener(this);
    }
    public void getProfile()
    {
        isGetProfileRequetsed=true;
        new GetProfile().execute();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }else if(v.getId()==R.id.editProfile)
        {
            businessName.setEnabled(true);
            userName.setEnabled(true);
            mobile.setEnabled(true);
            emailId.setEnabled(true);
            address.setEnabled(true);
            city.setEnabled(true);
            pincode.setEnabled(true);
            submit.setVisibility(View.VISIBLE);
            userName.requestFocus();
            userName.setCursorVisible(true);
            userName.setSelection( userName.getText().length());
            mobile.setSelection( mobile.getText().length());
            emailId.setSelection( emailId.getText().length());
            address.setSelection( address.getText().length());
            city.setSelection( city.getText().length());
            pincode.setSelection(pincode.getText().length());
            isEditEnabled=true;
        }else if(v.getId()==R.id.editImage)
        {
            Utils.selectImageDialog(ProfileDetails.this);
        }else if(v.getId()==R.id.submit)
        {
           if (isValidated())
           {
               isUpdateProfileRequested=true;
               new GetProfile().execute();
           }
        }
    }

    public boolean isValidated() {
        boolean status = false;
        if (userName.getText().length() > 0) {
            if (mobile.getText().length() == 10) {
                if (emailId.getText().length() > 0) {
                    if (Utils.isEmailIdValidated(emailId.getText().toString())) {
                        if (address.getText().toString().length() > 0) {
                            if(city.getText().length()>0)
                            {
                                if(pincode.getText().length()>0)
                                {
                                    status = true;
                                }else
                                    {
                                        Toast.makeText(this, "Please enter pincode", Toast.LENGTH_SHORT).show();
                                    }
                            }else{
                                Toast.makeText(this, "Please enter city", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please enter valid emailId", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter emailId", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
        }
        return status;
    }
    public class GetProfile extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileDetails.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
            if(isGetProfileRequetsed==true) {
               response = ApiCall.getData(Common.getUserProfileUrl + "" + Common.userId);
            }
            else if(isUpdateProfilePicRequested==true){
                MultipartRequest request=new MultipartRequest(Common.tempPath);
               response= request.execute(Common.getUpdateImageUrl(Common.emailId,"profile"));
            }
            else {
                response = ApiCall.postData(Common.updateUserProfileUrl ,getUpdateProfileJson());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    if (isUpdateProfileRequested == true) {
                        isUpdateProfileRequested = false;
                        if(s.equalsIgnoreCase("true"))
                        {    controller.setProfile( new ProfileDataset(getUpdateProfileJson()));
                            Toast.makeText(ProfileDetails.this,"Profile Updated Sucessfully.", Toast.LENGTH_SHORT).show();
                            disableView();
                        }else{
                            Toast.makeText(ProfileDetails.this,s, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(isUpdateProfilePicRequested==true){
                          isUpdateProfilePicRequested=false;
                        Uri uri = Uri.fromFile(new File(Common.tempPath));
                        Picasso.with(ProfileDetails.this).load(uri)
                                .resize(96, 96).centerCrop().into(profilePic);
                        Toast.makeText(ProfileDetails.this,"ProfilePic Updated Sucessfully.", Toast.LENGTH_SHORT).show();
                        if(alertDialog!=null)
                        {
                            alertDialog.cancel();
                        }
                        if (pd != null) {
                            pd.cancel();
                        }
                        getProfile();

                    }else  {
                        isGetProfileRequetsed=false;
                        JSONObject job = new JSONObject(s);
                        ProfileDataset pds = new ProfileDataset(job);
                        controller.setProfile( pds);
                        updateProfile(pds);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
}else{
                isGetProfileRequetsed=false;
                isUpdateProfilePicRequested=false;
                isUpdateProfileRequested = false;
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }
public void disableView()

{
    businessName.setEnabled(false);
    userName.setEnabled(false);
    mobile.setEnabled(false);
    emailId.setEnabled(false);
    address.setEnabled(false);
    city.setEnabled(false);
    pincode.setEnabled(false);
    submit.setVisibility(View.GONE);
}
    public void updateProfile(ProfileDataset pds) {
        if (pds != null) {
            if ((pds.getBusinessName().length() == 0)) {
                businessName.setText(pds.getEmailId());
            } else {
                businessName.setText(pds.getBusinessName());
            }
            if (pds.getName().length() == 0) {
                String value = pds.getEmailId();
                if (value.contains("@")) {
                    String[] val = value.split("@");
                    userName.setText(val[0]);
                }
            } else {
                userName.setText(pds.getName());
            }
            mobile.setText(pds.getMobileNumber());
            emailId.setText(pds.getEmailId());
            address.setText(pds.getAddress() );
            city.setText( pds.getCity());
            pincode.setText(pds.getZipCode());
            Picasso.with(ProfileDetails.this).load(pds.getImagePath()).placeholder(R.drawable.circular_image).into(profilePic);
            Picasso.with(ProfileDetails.this).load(pds.getImagePath()).placeholder(R.drawable.circular_image).into(controller.getProfilePicView());

        } else {
            Toast.makeText(ProfileDetails.this, "Data UnAvailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionReadCamera: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Common.tempPath = Common.imageUri.getPath();
                isImageCaptured=true;
                showDialog();
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == ProfileDetails.SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
               Common.tempPath= c.getString(columnIndex);
                c.close();
                isImageCaptured=true;
                showDialog();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
               // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));

                } else {
                    Toast.makeText(this, " This Image cannot be stored .please try with some other Image. ", Toast.LENGTH_SHORT).show();
                }

            }
        }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDetails.this)
                .setTitle("Update Profile Pic");

        final FrameLayout frameView = new FrameLayout(ProfileDetails.this);
        builder.setView(frameView);

         alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.uploadprofilepic, frameView);
        ImageView image = (ImageView) dialoglayout.findViewById(R.id.pic);
        Button upload = (Button) dialoglayout.findViewById(R.id.upload);
        upload.setTypeface(typeface);
        Uri uri = Uri.fromFile(new File(Common.tempPath));

        Picasso.with(ProfileDetails.this).load(uri)
                .resize(300, 300).centerCrop().into(image);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageCaptured == true) {
                   isUpdateProfilePicRequested=true;
                    new GetProfile().execute();

                }
            }
        });
        alertDialog.show();
    }

    public JSONObject getUpdateProfileJson() {
        JSONObject job = new JSONObject();
        try {
            job.put("Name",userName.getText().toString());
            job.put("Email", emailId.getText().toString());
            job.put("MobileNumber", mobile.getText().toString());
            job.put("Address", address.getText().toString());
            job.put("City", city.getText().toString());
            job.put("ZipCode", pincode.getText().toString());
            job.put("Id", Common.userId);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return job;
    }
}

