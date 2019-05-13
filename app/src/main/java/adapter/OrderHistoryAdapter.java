package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mypaybox.R;
import com.mypaybox.Review_Rating;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.AppController;
import common.Utils;
import interfaces.CancelOrderCallBack;
import model.VendorItemsDataset;


/**
 * Created by Honey Singh on 5/6/2017.
 */

public class OrderHistoryAdapter extends BaseAdapter {
    ArrayList<VendorItemsDataset> list;
    Activity act;
    AppController controller;
    CancelOrderCallBack callBack;
    public OrderHistoryAdapter(ArrayList<VendorItemsDataset> list, Activity act)
    {controller=(AppController) act.getApplicationContext();
        callBack=(CancelOrderCallBack)act;
        this.list=list;
        this.act=act;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
     final  VendorItemsDataset ds=list.get(position);
       ViewHolder holder;
        LayoutInflater inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.orderrow, parent, false);
            holder = new ViewHolder();
            holder.duration= (TextView) convertView.findViewById(R.id.duration);
            holder.productImage = (ImageView) convertView.findViewById(R.id.icon);
            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            holder.productName = (TextView) convertView.findViewById(R.id.name);
            holder.duration= (TextView) convertView.findViewById(R.id.duration);
            holder.supplier= (TextView) convertView.findViewById(R.id.supplier);
            holder.measurement=(TextView) convertView.findViewById(R.id.measurement);
            holder.price=(TextView) convertView.findViewById(R.id.price);
            holder.rate=(Button)convertView.findViewById(R.id.rate);
            holder.cancelOrder=(Button)convertView.findViewById(R.id.cancel_Order);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rate.setTypeface(controller.getTypeface());
        holder.cancelOrder.setTypeface(controller.getTypeface());
        holder.productName.setText(ds.getName()+"("+ds.getItemType()+")");
        holder.quantity.setText("Units :"+ds.getNumberOfUnit());
        holder.duration.setText("Duration : "+getFormatedDate(ds.getStartDate())+" to "+getFormatedDate(ds.getEndDate()));
        holder.supplier.setText("Supplied by:"+ds.getSuppliedBy());
        holder.measurement.setText("Type:"+ds.getMeasurement());
        long price= Math.round(ds.getPrice()* Double.parseDouble(ds.getNumberOfUnit()));
        holder.price.setText("Rs "+price);
        Picasso.with(act).load("http://"+ds.getImagePath()).placeholder(R.drawable.placeholder).fit().into(holder.productImage);
        convertView.setTag(holder);
        if(ds.getSupplierId().length()>0)
        {
            holder.rate.setVisibility(View.VISIBLE);
        }
        if((ds.isOrderCancelled()==true)||(((Utils.comparedDate(ds.getStartDate(), Utils.getCurrentDate()))&&(Utils.comparedDate(ds.getEndDate(), Utils.getCurrentDate())))))
        {
            holder.cancelOrder.setVisibility(View.GONE);
            if(ds.isOrderCancelled()==true) {
                holder.productName.setText("Order Cancelled  " + ds.getName() + "(" + ds.getItemType() + ")");
                holder.productName.setTextColor(act.getResources().getColor(R.color.red));
                holder.price.setText(ds.getMessage());
                holder.price.setTextColor(act.getResources().getColor(R.color.red));
            }else{
                holder.productName.setText("Order Completed  " + ds.getName() + "(" + ds.getItemType() + ")");
                holder.productName.setTextColor(act.getResources().getColor(R.color.green_color));
                holder.price.setTextColor(act.getResources().getColor(R.color.black));
            }

        }else{

            holder.cancelOrder.setVisibility(View.VISIBLE);
            holder.productName.setTextColor(act.getResources().getColor(R.color.blue));
        }
        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review_Rating.vendorId=ds.getSupplierId();
                Intent in=new Intent(act,Review_Rating.class);
                act.startActivity(in);
            }
        });
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCancelClicked(position);
            }
        });
        return convertView;
    }
    public class ViewHolder {
        Button rate,cancelOrder;
        ImageView productImage;
        TextView productName,quantity,duration,supplier,measurement,price;

    }

    public String getFormatedDate(String val)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date newDate = format.parse(val);

            format = new SimpleDateFormat("dd-MMM");
            String date = format.format(newDate);
            return date;
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return "";
    }
}
