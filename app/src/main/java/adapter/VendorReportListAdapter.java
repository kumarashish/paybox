package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mypaybox.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import common.AppController;
import model.VendorReportDataset;


/**
 * Created by Ashish.Kumar on 16-05-2017.
 */

public class VendorReportListAdapter extends BaseAdapter {
    ArrayList<VendorReportDataset> data;
    Activity act;
    AppController controller;
    LayoutInflater inflator;
    public VendorReportListAdapter(Activity act, ArrayList<VendorReportDataset> data)
    {
        this.act=act;
        this.data=data;
        controller=(AppController)act.getApplicationContext();inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override

    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        VendorReportDataset ds=data.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.vendor_report_row, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.prductMeasurement = (TextView) convertView.findViewById(R.id.productDetails);
            holder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            holder.productQuantity = (TextView) convertView.findViewById(R.id.productUnits);
            holder.userName= (TextView) convertView.findViewById(R.id.userName);
            holder.deliveryAddress = (TextView) convertView.findViewById(R.id.deliveryaddress);
            holder.deliveryDuration = (TextView) convertView.findViewById(R.id.deliveryDuration);
            holder.deliveryTimming = (TextView) convertView.findViewById(R.id.deliverytimming);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.productName.setTypeface(controller.getTypeface());
        holder.prductMeasurement.setTypeface(controller.getTypeface());
        holder.productPrice.setTypeface(controller.getTypeface());
        holder.productQuantity.setTypeface(controller.getTypeface());
        holder.userName.setTypeface(controller.getTypeface());
        holder.deliveryAddress.setTypeface(controller.getTypeface());
        holder.deliveryDuration.setTypeface(controller.getTypeface());
        holder.deliveryTimming.setTypeface(controller.getTypeface());
        holder.productName.setText(ds.getProductName());
        holder.prductMeasurement.setText("Measurement : "+ds.getPrductMeasurement());
        holder.productPrice.setText("Price : "+ds.getProductPrice());
        holder.productQuantity.setText("Quantity : "+ds.getProductQuantity());
        holder.userName.setText("User Name : "+ds.getUserName());
        holder.deliveryAddress.setText("Delivery Address : "+ds.getDeliveryAddress());
        holder.deliveryDuration.setText("Duration : "+ds.getDeliveryDuration());
        holder.deliveryTimming.setText("Timming :"+ds.getDeliveryTimming());
        Picasso.with(act).load("http://"+ds.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.image);
        convertView.setTag(holder);
        return convertView;
    }
    public class ViewHolder{
        ImageView image;
        TextView productName,prductMeasurement,productPrice,productQuantity,userName,deliveryAddress,deliveryDuration,deliveryTimming;
    }
}
