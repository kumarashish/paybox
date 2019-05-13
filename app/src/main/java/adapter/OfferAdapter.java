package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.mypaybox.Offer;
import com.mypaybox.R;

import java.util.ArrayList;

import common.AppController;
import common.Utils;
import model.Offers;


/**
 * Created by Honey Singh on 7/16/2017.
 */

public class OfferAdapter extends BaseAdapter {
    Activity act; ArrayList<Offers> offerList;
   AppController controller;
   public OfferAdapter(Activity act, ArrayList<Offers> offerList)
   {
       this.act=act;
       this.offerList=offerList;
       controller=(AppController)act.getApplicationContext();

   }
    @Override
    public int getCount() {
        return offerList.size();
    }

    @Override
    public Object getItem(int position) {
        return offerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
     ViewHolder holder;
        final Offers o_ds=offerList.get(position);
        LayoutInflater inflator=(LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {convertView=inflator.inflate(R.layout.offer_row,parent,false);
            holder=new ViewHolder();
            holder.offerCode=(TextView)convertView.findViewById(R.id.offerCode);
            holder.offername=(TextView)convertView.findViewById(R.id.offername);
            holder.startDate=(TextView)convertView.findViewById(R.id.startDate);
            holder.endDate=(TextView)convertView.findViewById(R.id.endDate);
            holder.amount=(TextView)convertView.findViewById(R.id.amount);
            holder.uses=(TextView)convertView.findViewById(R.id.uses);
            holder.copy=(Button)convertView.findViewById(R.id.copy);
            holder. minimumOrderAmount=(TextView)convertView.findViewById(R.id.min_amount);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.offerCode.setText("Offer Code :  "+o_ds.getOfferCode());
        holder.offername.setText("Get "+o_ds.getOfferPercentage()+" % Off");
        holder.startDate.setText("Valid from "+ Utils.getFormatedDate(o_ds.getOfferStartDate()));
        holder.endDate.setText("Valid till "+ Utils.getFormatedDate(o_ds.getOfferEndDate()));
        holder.amount.setText("Maximum offer amount Rs "+o_ds.getMaximumOfferAmount());
        holder.uses.setText("Maximum redeem "+o_ds.getNumberOfUses() +" times");
        holder. minimumOrderAmount.setText("Minimum order amount Rs "+o_ds.getMinOrderAmount() );
        holder.copy.setTypeface(controller.getTypeface());
        holder.amount.setTypeface(controller.getTypeface());
        convertView.setTag(holder);
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Offer.amount> Utils.getIntegerFromString(o_ds.getMinOrderAmount())) {
                    controller.setOffer(o_ds);
                    if (controller.getEnterCoupon() != null) {
                        controller.getEnterCoupon().setText(o_ds.getOfferCode());
                        controller.getEnterCoupon().setSelection(o_ds.getOfferCode().length());
                        act.finish();
                    }
                    Toast.makeText(act, "Offer Copied Sucessfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(act, "You are not eligible for this offer ,your order amount should be greater than Rs."+
                            o_ds.getMinOrderAmount(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return convertView;
    }
    public class ViewHolder{
        TextView offername,offerCode,startDate,endDate,amount,uses,minimumOrderAmount;
        Button copy;
    }
}
