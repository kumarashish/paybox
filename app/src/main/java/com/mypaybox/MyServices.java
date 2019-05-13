package com.mypaybox;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ashish.kumar on 03-05-2017.
 */

public class MyServices  extends Activity implements View.OnClickListener{
    ImageView back;
    TextView headingView;
    ListView list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlist);
        initializeAll();
    }

    public void initializeAll() {
       Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        list = (ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);
        headingView.setText("My Services");
        headingView.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }
    }
}
