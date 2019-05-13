package com.mypaybox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instamojo.android.models.Order;

import common.AppController;
import common.PrefManager;
import login.Login;

/**
 * Created by Ashish.Kumar on 24-01-2018.
 */

public class NewDashBoard extends Activity implements View.OnClickListener {
    ImageView maintenance,payRent,dailyneeds,dropdown;
    TextView maintenanceTv1,maintenanceTv2,payrentTv1,payrentTv2,dailyneeds1,getDailyneeds2;
    boolean isDropDownClicked=false;
    LinearLayout slideMenuOptions;
    TextView profile,order,offer,history,password,logout;
    PrefManager prfs;
    AppController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        initializeAll();
    }

    public void initializeAll() {
        controller=(AppController)getApplicationContext();
        prfs= controller.getPrefsManger();
        slideMenuOptions=(LinearLayout) findViewById(R.id.slideMenuOptions);
        dropdown=(ImageView)findViewById(R.id.dropdown);
        maintenance = (ImageView) findViewById(R.id.maintenance_img);
        payRent = (ImageView) findViewById(R.id.payrent_img);
        dailyneeds = (ImageView) findViewById(R.id.dailyneed_Img);
        maintenanceTv1 = (TextView) findViewById(R.id.maintenance_tv1);
        maintenanceTv2 = (TextView) findViewById(R.id.maintenance_tv1);
        payrentTv1 = (TextView) findViewById(R.id.payrent_tv1);
        payrentTv2 = (TextView) findViewById(R.id.payrent_tv2);
        profile= (TextView) findViewById(R.id.profile);
        order= (TextView) findViewById(R.id.order);
        offer= (TextView) findViewById(R.id.offer);
        history= (TextView) findViewById(R.id.history);
        password= (TextView) findViewById(R.id.changePassword);
        logout= (TextView) findViewById(R.id.logout);
        dailyneeds1 = (TextView) findViewById(R.id.dailyneed_tv1);
        getDailyneeds2 = (TextView) findViewById(R.id.dailyneed_tv2);
        maintenance.setOnClickListener(this);
        payRent.setOnClickListener(this);
        dailyneeds.setOnClickListener(this);
        maintenanceTv1.setOnClickListener(this);
        maintenanceTv2.setOnClickListener(this);
        payrentTv1.setOnClickListener(this);
        payrentTv2.setOnClickListener(this);
        dailyneeds1.setOnClickListener(this);
        getDailyneeds2.setOnClickListener(this);
        dropdown.setOnClickListener(this);
        profile.setOnClickListener(this);
        order.setOnClickListener(this);
        offer.setOnClickListener(this);
        history.setOnClickListener(this);
        password.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.maintenance_img:
            case R.id.maintenance_tv1:
            case R.id.maintenance_tv2:
                Intent in = new Intent(this, AppartmentDetails.class);
                startActivity(in);
                break;
            case R.id.payrent_img:
            case R.id.payrent_tv1:
            case R.id.payrent_tv2:
                in = new Intent(this, Rent.class);
                startActivity(in);
                break;
            case R.id.dropdown:
                if(isDropDownClicked==false)
                { dropdown.setImageResource(R.drawable.dropup);
                    slideMenuOptions.setVisibility(View.VISIBLE);
                    isDropDownClicked=true;
                }else{
                    slideMenuOptions.setVisibility(View.GONE);
                    isDropDownClicked=false;
                    dropdown.setImageResource(R.drawable.dropdown);
                }
                break;
            case R.id.profile:
                in = new Intent(this, ProfileDetails.class);
                startActivity(in);
                break;
            case R.id.order:
                in = new Intent(this,MyOrder.class);
                startActivity(in);
                break;
            case R.id.offer:
                in = new Intent(this, Offer.class);
                startActivity(in);
                break;
            case R.id.history:
                in = new Intent(this, History.class);
                startActivity(in);
                break;
            case R.id.changePassword:
                in = new Intent(this,ChangePassword.class);
                startActivity(in);
                break;
            case R.id.logout:
                String rememberId= prfs.getPrefsString("RememberEmailId");
                boolean isFirstTime=prfs.isFirstTimeLaunch();
                prfs.handleLogout();
                prfs.getEditor().putString("RememberEmailId",rememberId);
                prfs.setFirstTimeLaunch(isFirstTime);
                prfs.getEditor().commit();
                in = new Intent(this, Login.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
            default:
                in = new Intent(this, DashBoard.class);
                startActivity(in);
                break;
        }

    }
}
