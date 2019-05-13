package com.mypaybox;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import common.ApiCall;
import common.Common;
import common.CustomTypefaceSpan;
import common.Utils;
import login.Login;
import model.ProfileDataset;

public class VendorDashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView userNamee,emailIdd,businessName;
    SharedPreferences prfs;
    SharedPreferences.Editor edit;
    ProgressDialog pd;
    EditText userName,mobile,emailId,address,city,pincode;
    Button editImage;
    public final  int permissionReadCamera=1;
    Typeface typeface;
    boolean isImageCaptured=false;
    boolean isUpdateProfilePicRequested=false;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        customActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);
        }
        updateUI(navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        if (Utils.isInternetAvailable(VendorDashBoard.this)) {

            new GetProfile().execute();
        } else {
            Toast.makeText(VendorDashBoard.this,"Internet Unavailable.", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(VendorDashBoard.this)
                .setTitle("Update Profile Pic");

        final FrameLayout frameView = new FrameLayout(VendorDashBoard.this);
        builder.setView(frameView);

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.uploadprofilepic, frameView);
        ImageView image = (ImageView) dialoglayout.findViewById(R.id.pic);
        Button upload = (Button) dialoglayout.findViewById(R.id.upload);
        upload.setTypeface(typeface);
        Uri uri = Uri.fromFile(new File(Common.tempPath));

        Picasso.with(VendorDashBoard.this).load(uri)
                .resize(300, 300).centerCrop().into(image);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageCaptured == true) {
                    isUpdateProfilePicRequested=true;
                    //new ProfileDetails.GetProfile().execute();

                }
            }
        });
        alertDialog.show();
    }
    public void customActionBar(Toolbar toolbar) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        SpannableString s = new SpannableString("1Bazaar");
        s.setSpan(new TypefaceSpan("lato_light.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Update the action bar title with the TypefaceSpan instance
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(s);
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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

    public void updateUI(NavigationView view) {
        Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        prfs = PreferenceManager.getDefaultSharedPreferences(this);
        edit = prfs.edit();
        View header = view.getHeaderView(0);
        userNamee = (TextView) header.findViewById(R.id.textView);
        emailIdd = (TextView) header.findViewById(R.id.textView3);
        String value = prfs.getString("UserName", "");
        if (value.contains("@")) {
            String[] val = value.split("@");
            userNamee.setText(val[0]);
        }
        emailIdd.setText(prfs.getString("UserName", ""));
        View myLayout = findViewById( R.id.dashboard );
        View contentDashboard=myLayout.findViewById( R.id.contentDashboard);
        businessName=(TextView)contentDashboard.findViewById(R.id.businessName);
        userName=(EditText)contentDashboard.findViewById(R.id.userName);
        mobile=(EditText)contentDashboard.findViewById(R.id.mobile);
        emailId=(EditText)contentDashboard.findViewById(R.id.emailId);
        address=(EditText)contentDashboard.findViewById(R.id.address) ;
        city=(EditText)contentDashboard.findViewById(R.id.city) ;
        pincode=(EditText)contentDashboard.findViewById(R.id.pin) ;
        editImage=(Button)contentDashboard.findViewById(R.id.editImage);
        submit=(Button)contentDashboard.findViewById(R.id.submit);
        businessName.setTypeface(typeface);
        userName.setTypeface(typeface);
        mobile.setTypeface(typeface);
        emailId.setTypeface(typeface);
        address.setTypeface(typeface);
        city.setTypeface(typeface);
        pincode.setTypeface(typeface);
        userName.setTypeface(typeface);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectImageDialog(VendorDashBoard.this);
            }
        });
        int permissionCheck = ContextCompat.checkSelfPermission(VendorDashBoard.this,
                Manifest.permission.CAMERA);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(VendorDashBoard.this,
                        new String[]{Manifest.permission.CAMERA},
                        permissionReadCamera);
            }
        }
        enableView();
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

        } else {
            Toast.makeText(this, "Data UnAvailable", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.report) {
            Intent in=new Intent(VendorDashBoard.this,VendorReport.class);
            startActivity(in);
        }
        else if (id == R.id.deliverables) {
            Intent in=new Intent(VendorDashBoard.this,MyDeliverables.class);
            startActivity(in);
        }
       else if (id == R.id.changePassword) {
            Intent in=new Intent(VendorDashBoard.this,ChangePassword.class);
            startActivity(in);
        }
        else if (id == R.id.logout) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
            Intent in = new Intent(VendorDashBoard.this, Login.class);
            startActivity(in);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class GetProfile extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VendorDashBoard.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {


            String response = ApiCall.getData(Common.getUserProfileUrl + "" + Common.userId);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject job = new JSONObject(s);
                    ProfileDataset pds = new ProfileDataset(job);
                    updateProfile(pds);
                }catch (Exception ex)
                {
                    ex.fillInStackTrace();
                }
            }
            if(pd!=null)
            {
                pd.cancel();
            }
        }
            }

    public void enableView()

    {
        businessName.setEnabled(true);
        userName.setEnabled(true);
        mobile.setEnabled(true);
        emailId.setEnabled(true);
        address.setEnabled(true);
        city.setEnabled(true);
        pincode.setEnabled(true);
        submit.setVisibility(View.VISIBLE);
    }
}
