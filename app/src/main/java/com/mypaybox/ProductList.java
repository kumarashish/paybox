package com.mypaybox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import adapter.GridViewAdapter;
import common.Common;

/**
 * Created by Honey Singh on 4/10/2017.
 */

public class ProductList extends Activity implements View.OnClickListener{
    ImageView imageView,back;
    TextView text,heading;
    LinearLayout layout;
    public static String selectedProduct="";
    public static String selectedService="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicesproduct);
        initializeAll();
    }
    public void initializeAll()
    {   back=(ImageView)findViewById(R.id.back);
        heading=(TextView)findViewById(R.id.heading);
        imageView=(ImageView)findViewById(R.id.bgImage);
        text=(TextView)findViewById(R.id.productName);
        layout=(LinearLayout)findViewById(R.id.itemsList);
        if(Common.serviceImageUrl!=null)
        {

            Picasso.with(this).load("http://"+ Common.serviceImageUrl).fit().into(imageView);
        }else{
            imageView.setImageResource(R.drawable.daymilk);
        }
        heading.setText(selectedService);
        text.setText(selectedProduct);
        back.setOnClickListener(this);
        addViewToLinearLayout();
    }

    public void addViewToLinearLayout() {
        for (int i = 0; i < DashBoard.data.size(); i++) {
            LinearLayout parent = new LinearLayout(ProductList.this);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.productlistrow, parent, false);
            Button viewAll=(Button)v.findViewById(R.id.viewAllButton);
            TextView name = (TextView) v.findViewById(R.id.categoryName);
            GridView grid = (GridView) v.findViewById(R.id.items);
            grid.setAdapter(new GridViewAdapter(ProductList.this, DashBoard.data.get(i).getItems(),DashBoard.data.get(i).getName()));
            name.setText(DashBoard.data.get(i).getName());
            if (DashBoard.data.get(i).getItems().size() > 3) {
                viewAll.setVisibility(View.VISIBLE);
            } else {
                viewAll.setVisibility(View.GONE);
            }
            parent.addView(v);
            layout.addView(parent);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==back.getId())
        {
            onBackPressed();
        }
    }
}
