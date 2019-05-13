package com.mypaybox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Honey Singh on 10/2/2017.
 */

public class CarWash_Laundry  extends Activity implements View.OnClickListener{
    ImageView back;
    TextView headingView;
    TextView message;
    public static String Title;
    public static String Message;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundry);
        initializeAll();

    }

    public void initializeAll() {

        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        message = (TextView) findViewById(R.id.message);
        back.setOnClickListener(this);
        headingView.setText(Title);
        message.setText(Message);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();

    }

}

