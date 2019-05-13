package com.mypaybox;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.CityAdapter;
import adapter.ItemsDeliveryAdpter;
import adapter.ItemsServiceAdapter;
import adapter.LocalityAdapter;
import common.ApiCall;
import common.AppController;
import common.Common;
import common.CustomTypefaceSpan;
import common.Utils;
import interfaces.LocalityCallBack;
import login.Login;
import model.CityDataset;
import model.LocalityDataset;
import model.ProductDataset;
import model.ProfileDataset;
import model.ServicesDataset;


public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener ,interfaces.Callback, LocalityCallBack {
TextView city,locality,items;
    RadioButton deliverables,service;
    ProgressDialog pd;
    ArrayList<CityDataset> cityList=null;
   ArrayList<ServicesDataset> servicesList=null;
    ArrayList<LocalityDataset> localityList=null;
    ArrayList<ServicesDataset> deliverablesList=new ArrayList<ServicesDataset>();
    TextView userName,emailId;
    RadioGroup group;
    Dialog dialog;
    int cityId;
    public boolean isCategoryRequested = false;
    public boolean isLocalityRequested = false;
    public boolean isProductListRequested = false;
    public boolean isCitySelected = false;
    public boolean isItemListRequested = false;
    AppController conntroller;
    public static boolean isItemSelected=false;
    Button done,clear;
    public static boolean isLocalitySelected=false;
    public static ArrayList<ProductDataset> data=new ArrayList<>();
    common.CustomImageView profilePic;
    LocalityCallBack localityCallBack;
    int RenttActivityRequestCode=45;
    SharedPreferences prfs;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        initializeAll();
        if (Utils.isInternetAvailable(DashBoard.this)) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String response = ApiCall.getData(Common.getUserProfileUrl + "" + Common.userId);
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        conntroller.setProfile(new ProfileDataset(jsonObject));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                {
                                    Picasso.with(DashBoard.this).load(conntroller.getProfile().getImagePath()).resize(96, 96).centerCrop().placeholder(R.drawable.circular_image).into(profilePic);
                                }
                            }
                        });
                    }catch (Exception ex)
                    {
                        ex.fillInStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    public void customActionBar(Toolbar toolbar) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        SpannableString s = new SpannableString("MyPayBox");
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
public void initializeAll()
{  Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
    conntroller=(AppController)getApplicationContext();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    customActionBar(toolbar);
    prfs= PreferenceManager.getDefaultSharedPreferences(DashBoard.this);
    edit=prfs.edit();
    localityCallBack=this;
    Common.isAppartmentUser =prfs.getBoolean("isAppartementUser",false);
    NavigationView view=(NavigationView)findViewById(R.id.nav_view);
    Menu m = view.getMenu();
    for (int i=0;i<m.size();i++) {
        MenuItem mi = m.getItem(i);
        applyFontToMenuItem(mi);
    }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    View header =view.getHeaderView(0);
    userName=(TextView) header .findViewById(R.id.textView);
    emailId=(TextView)header.findViewById(R.id.textView3);
    profilePic=(common.CustomImageView)header.findViewById(R.id.imageView);
    conntroller.setProfilePicView(profilePic);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    String value=prfs.getString("UserName","");
    if(value.contains("@"))
    {
        String[]val=value.split("@");
        userName.setText(val[0]);
    }

    emailId.setText(prfs.getString("UserName",""));
    View myLayout = findViewById( R.id.dashboard );
    View contentDashboard=myLayout.findViewById( R.id.contentDashboard);
    done=(Button) contentDashboard.findViewById(R.id.done);
    clear=(Button) contentDashboard.findViewById(R.id.clear);
    done.setTypeface(typeface);
    clear.setTypeface(typeface);
    city=(TextView) contentDashboard.findViewById(R.id.city);
    locality=(TextView) contentDashboard.findViewById(R.id.locality);
    items=(TextView) contentDashboard.findViewById(R.id.selectItem);
    group=(RadioGroup)contentDashboard.findViewById(R.id.group) ;
    deliverables=(RadioButton) contentDashboard.findViewById(R.id.deliverables);
    service=(RadioButton) contentDashboard.findViewById(R.id.services);
    group.setVisibility(View.GONE);
    deliverables.setTypeface(typeface);
    service.setTypeface(typeface);
    group.check(deliverables.getId());
    city.setOnClickListener(this);
    locality.setOnClickListener(this);
    items.setOnClickListener(this);
    done.setOnClickListener(this);
    clear.setOnClickListener(this);
    ProductList.selectedService="Deliverables";
    if(Utils.isInternetAvailable(DashBoard.this))
    {isCategoryRequested=true;
        new GetData().execute();
    }
group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId){
        if (checkedId == deliverables.getId()) {
            items.setText("Select Items");
            ProductList.selectedService="Deliverables";
        } else if (checkedId == service.getId()) {
            items.setText("Select Items");
            ProductList.selectedService="Service";
        }
    }
});
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




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
switch (id) {
    case R.id.logout:
        // Handle the camera action
        String rememberId= prfs.getString("RememberEmailId","");
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        edit.putString("RememberEmailId",rememberId);
        edit.commit();
        Intent in = new Intent(DashBoard.this, Login.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
        break;
    case R.id.profile:
        // Handle the camera action

        in = new Intent(DashBoard.this, ProfileDetails.class);
        startActivity(in);

        break;
    case R.id.services:
        Toast.makeText(this, "Under development", Toast.LENGTH_SHORT).show();
        break;
    case R.id.orders:
        in = new Intent(DashBoard.this, MyOrder.class);
        startActivity(in);
        break;
    case R.id.changePassword:
         in=new Intent(this,ChangePassword.class);
        startActivity(in);
        break;
    case R.id.offers:
        in=new Intent(this,Offer.class);
        startActivity(in);
        break;
    case R.id.history:
        in=new Intent(this,History.class);
        startActivity(in);
        break;

}
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
switch (v.getId())
{
    case R.id.city:
        if(cityList!=null) {
            showAlertDialog("Select City", 0);
        }else{
        Intent in = new Intent(DashBoard.this, Login.class);
        startActivity(in);
        finish();
        Toast.makeText(DashBoard.this,"Session Expired ,Please login.", Toast.LENGTH_SHORT).show();
    }
        break;
    case R.id.locality:
        if(localityList!=null) {
            showAlertDialog("Select Locality", 1);
        }else{
            Toast.makeText(DashBoard.this,"Please select city first.", Toast.LENGTH_SHORT).show();
        }
        break;
    case R.id.selectItem:
        showAlertDialog("Select Items",2);
        break;
    case R.id.done:
      if((isCitySelected==true)&&(isLocalitySelected=true)&&(isItemSelected==true))
      {
          if(items.getText().toString().equalsIgnoreCase("Appartment"))
          {   Common.city=city.getText().toString();
              Common.locality=locality.getText().toString();
              Intent in=new Intent(DashBoard.this,AppartmentDetails.class);
              startActivity(in);
          }else if(items.getText().toString().equalsIgnoreCase("Car Wash")||(items.getText().toString().equalsIgnoreCase("Laundry")))
          {
              CarWash_Laundry.Title=items.getText().toString();
              Intent in=new Intent(DashBoard.this,CarWash_Laundry.class);
              startActivity(in);
          }else if(items.getText().toString().equalsIgnoreCase("RENT"))
          {
              Common.city=city.getText().toString();
              Common.locality=locality.getText().toString();
              Intent in=new Intent(DashBoard.this,Rent.class);
              startActivityForResult(in,45);
          }
          else {
              ProductList.selectedProduct = items.getText().toString();
              isProductListRequested = true;
              new GetData().execute();
          }
      }else{
          if(isCitySelected==false)
          { Toast.makeText(DashBoard.this,"Please select city.", Toast.LENGTH_SHORT).show();}
          else if(isLocalitySelected==false)
          { Toast.makeText(DashBoard.this,"Please select locality.", Toast.LENGTH_SHORT).show();}
          else {
              Toast.makeText(DashBoard.this,"Please select items.", Toast.LENGTH_SHORT).show();
          }
      }
        break;
    case R.id.clear:
        clearAll();
        break;
}
    }
public void clearAll()
{
    city.setText("Select City");
    locality.setText("Select Locality");
    items.setText("Select Items");
    isCitySelected=false;
    isLocalitySelected=false;
    isItemSelected=false;

}
    @Override
    public void onClick(int pos) {
        isLocalitySelected=false;
        isCitySelected=true;
        isItemSelected=false;
        isLocalityRequested=true;
        cityId=pos;
        locality.setText("Select Locality");
        items.setText("Select Items");
new GetData().execute();
    }

    @Override
    public void onLocalityClicked(int pos) {
        isItemListRequested=true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetData().execute();
            }
        });

    }

    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DashBoard.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
            if(isCategoryRequested==true) {
               response = ApiCall.getData(Common.getAllCategoriesUrl);
            }
            else if (isLocalityRequested==true) {
                response = ApiCall.getData(Common.getLocalityUrl+""+cityId);
            }
            else if (isItemListRequested==true) {
                response = ApiCall.getData(Common.getServiceUrl+""+ Common.localityId);
            }
            else if(isProductListRequested==true)
            {
                response = ApiCall.getData(Common.getProductUrl(Common.serviceId, Common.localityId));
            }
            Log.d("Api respone:",response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isCategoryRequested==true) {
                isCategoryRequested=false;
                JSONParsing(s);
            }else if(isLocalityRequested==true){
                localityJSONParsing(s);

                isLocalityRequested=false;
            }
            else if(isItemListRequested==true){
                serviceListJSonParsing(s);
                isItemListRequested=false;
            }else if(isProductListRequested==true)
            {
                isProductListRequested=false;
                productsJsonParsing(s);
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }
    public void serviceListJSonParsing(String s)
    {try {
        deliverablesList.clear();
        JSONArray jsonArray=new JSONArray(s);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            ServicesDataset ds = new ServicesDataset(object);
            deliverablesList.add(ds);
        }
    }catch (Exception ex)
    {
        ex.fillInStackTrace();
    }
    }
public void productsJsonParsing(String s)
{data.clear();
    try {

JSONArray jsonArray=new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++)
            {
                ProductDataset vds=new  ProductDataset(jsonArray.getJSONObject(i));
                data.add(vds);
            }
            if(jsonArray.length()>0)
            {
                Intent in=new Intent(DashBoard.this,ProductList.class);
                startActivity(in);
            }else{
                Toast.makeText(DashBoard.this,"Data for selected type is Unavailable.", Toast.LENGTH_SHORT).show();
            }

    }catch (Exception ex)
    {
        ex.fillInStackTrace();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.isNull("Message")) {

                Toast.makeText(DashBoard.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception exx)
        {
            exx.fillInStackTrace();
        }
    }
}
    public void localityJSONParsing(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            {
                localityList = new ArrayList<LocalityDataset>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject job = jsonArray.getJSONObject(i);
                    LocalityDataset ds = new LocalityDataset(job);
                    localityList.add(ds);
                }
                showAlertDialog("Select Locality", 1);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.isNull("Message")) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                    Toast.makeText(DashBoard.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(DashBoard.this, Login.class);
                    startActivity(in);
                    finish();
                }
            } catch (Exception exx) {
                exx.fillInStackTrace();
            }
        }
    }

    public void JSONParsing(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.isNull("Message")) {
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                Toast.makeText(DashBoard.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(DashBoard.this, Login.class);
                startActivity(in);
                finish();

            } else {
                if (!jsonObject.isNull("cities")) {
                    cityList = new ArrayList<CityDataset>();
                    JSONArray jsonArray = jsonObject.getJSONArray("cities");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CityDataset ds = new CityDataset(object);
                        cityList.add(ds);
                    }
                }
//                if (!jsonObject.isNull("services")) {
//
//                    servicesList = new ArrayList<ServicesDataset>();
//                    JSONArray jsonArray = jsonObject.getJSONArray("services");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        ServicesDataset ds = new ServicesDataset(object);
//                        servicesList.add(ds);
//                    }
//                }
//                if (!jsonObject.isNull("deliverableList")) {
//                    deliverablesList = new ArrayList<ServicesDataset>();
//                    JSONArray jsonArray = jsonObject.getJSONArray("deliverableList");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        ServicesDataset ds = new ServicesDataset(object);
//                        deliverablesList.add(ds);
//                    }
//                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public void showAlertDialog(String heading, final int val) {
        final Dialog dialog = new Dialog(DashBoard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);
        // set the custom dialog components - text, image and button
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText(heading);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        boolean isDialogTobeShown = false;
        switch (val) {
            case 0:
                if (cityList.size() > 0) {
                    isDialogTobeShown = true;
                    listView.setAdapter(new CityAdapter(DashBoard.this, cityList, city, dialog, this));
                }else{
                    Toast.makeText(DashBoard.this, "Unable to fetch city,please check your internet.", Toast.LENGTH_SHORT).show();
                }

                break;
            case 1:
                if (localityList.size() > 0) {
                    isDialogTobeShown = true;
                    listView.setAdapter(new LocalityAdapter(DashBoard.this, localityList, locality, dialog, localityCallBack));
                } else {
                    Toast.makeText(DashBoard.this, "Vendor not available for selected city.", Toast.LENGTH_SHORT).show();
                }

                break;
            case 2:
                if (group.getCheckedRadioButtonId() == deliverables.getId()) {
                    if (isAppartmentAdded() == false) {
                        if (Common.isAppartmentUser == true) {
                            ServicesDataset ds = new ServicesDataset("APPARTMENT");
                            deliverablesList.add(ds);
                        }
                    }
                    if (deliverablesList.size() > 0) {
                        isDialogTobeShown = true;
                        listView.setAdapter(new ItemsDeliveryAdpter(DashBoard.this, deliverablesList, items, dialog));
                    } else {
                        Toast.makeText(DashBoard.this, "Please select city and locality first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    listView.setAdapter(new ItemsServiceAdapter(DashBoard.this, servicesList, items, dialog));
                }
                break;
        }
        if (isDialogTobeShown == true) {
            dialog.show();
        }
    }

public  boolean isAppartmentAdded()
    {
        if(deliverablesList.size()>0) {
            for (int i = 0; i < deliverablesList.size(); i++) {
                if (deliverablesList.get(i).getServiceName().equalsIgnoreCase("APPARTMENT")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==RenttActivityRequestCode)&& (resultCode == RESULT_OK ))
        {
            clearAll();
        }
    }
}
