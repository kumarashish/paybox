package com.mypaybox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import common.ApiCall;
import common.Common;

/**
 * Created by ashish.kumar on 03-05-2017.
 */

public class Review_Rating extends Activity implements View.OnClickListener{
    TextView headingView;
    ImageView back;
   public static String vendorId;
    RatingBar rating;
    EditText subject,details;
    Button submit;
    ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.rating);
            initializeAll();
        }catch (Exception ex) {
        ex.fillInStackTrace();
        }

    }
    public void initializeAll() {
        Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        subject=(EditText)findViewById(R.id.subject) ;
        details=(EditText)findViewById(R.id.details);
        rating=(RatingBar)findViewById(R.id.ratingBar) ;
        submit=(Button) findViewById(R.id.submit);
        back.setOnClickListener(this);
        headingView.setTypeface(typeface);
        subject.setTypeface(typeface);
        details.setTypeface(typeface);
        submit.setTypeface(typeface);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }else if(v.getId()==R.id.submit)
        {
            if((subject.getText().length()>0)&&(details.getText().length()>0))
            {
                 new RateOrder().execute();
            }else{
                if(subject.getText().length()==0)
                {
                    Toast.makeText(this,"Please enter subject", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Please enter details", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public JSONObject getRatingJSON() {
        JSONObject job = new JSONObject();
        try {

            job.put("userId", Common.userId);
            job.put("vendorId", vendorId);
            job.put("title", subject.getText().toString());
            job.put("description", details.getText().toString());
            job.put("rating", String.valueOf(rating.getRating()));
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

        return job;
    }

    public class RateOrder extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Review_Rating.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = ApiCall.postData(Common.saveRating,getRatingJSON());
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("true"))
            {
                Toast.makeText(Review_Rating.this,"Rated sucessfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (pd != null) {
                pd.cancel();
            }
        }}
}
