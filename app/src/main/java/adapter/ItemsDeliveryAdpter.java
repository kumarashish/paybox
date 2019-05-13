package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mypaybox.CarWash_Laundry;
import com.mypaybox.DashBoard;
import com.mypaybox.R;

import java.util.ArrayList;

import common.Common;
import model.ServicesDataset;


/**
 * Created by Honey Singh on 4/7/2017.
 */

public class ItemsDeliveryAdpter  extends BaseAdapter {
    ArrayList<ServicesDataset> list;
    Activity act;
    TextView tv;
    Dialog dialog;
    public ItemsDeliveryAdpter(Activity act, ArrayList<ServicesDataset> list, TextView tv, Dialog dialog) {
        this.act = act;
        this.list = list;
        this.tv=tv;
        this.dialog=dialog;
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        LayoutInflater inflator=(LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {convertView=inflator.inflate(R.layout.listrow,parent,false);
            holder=new ViewHolder();
            holder.text=(TextView)convertView.findViewById(R.id.item);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.text.setText(list.get(position).getServiceName().toUpperCase());
        convertView.setTag(holder);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(list.get(position).getServiceName());
                Common.serviceId=list.get(position).getServiceId();
                Common.serviceImageUrl=list.get(position).getServiceImage();
                CarWash_Laundry.Message=list.get(position).getServiceMessage();
                DashBoard.isItemSelected=true;
                dialog.cancel();
            }
        });
        return convertView;
    }
    public class ViewHolder{
        TextView text;
    }
}
