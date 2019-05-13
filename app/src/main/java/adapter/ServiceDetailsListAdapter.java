package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mypaybox.R;

import java.util.ArrayList;

import model.ServiceDataSet;


/**
 * Created by Ashish.Kumar on 21-08-2017.
 */

public class ServiceDetailsListAdapter extends BaseAdapter {
    Activity activity; ArrayList<ServiceDataSet> data;
    int units=1;
   public ServiceDetailsListAdapter(Activity activity, ArrayList<ServiceDataSet> data,int units)
    {
      this.activity=activity;
      this.data=data;
      this.units=units;
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
       ViewHolder holder=null;

        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

                holder=new ViewHolder();
                convertView = inflator.inflate(R.layout.service_details_row, parent, false);
                holder.cost = (TextView) convertView.findViewById(R.id.cost);
                holder.duration = (TextView) convertView.findViewById(R.id.duration);
                holder.Name = (TextView) convertView.findViewById(R.id.name);
                holder.header=(LinearLayout)convertView.findViewById(R.id.header);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.duration.setText("Cost : "+data.get(position).getServiceCost());
        int perFlatCost=Math.round(Float.parseFloat(data.get(position).getServiceCost())/units);
        holder.cost.setText("Contribution : Rs."+perFlatCost);
        holder.Name.setText(data.get(position).getServiceName());
if(position%2==0)
{
    holder.header.setBackgroundColor(activity.getResources().getColor(R.color.white));
}else{
    holder.header.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
}
        convertView.setTag(holder);
        return convertView;
    }
    class ViewHolder{
        LinearLayout header;
        TextView Name,duration,cost;
    }
}
