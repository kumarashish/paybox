package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mypaybox.R;

import java.util.ArrayList;

import interfaces.Callback;
import interfaces.DeleteCallBack;
import model.AddressDataset;


/**
 * Created by Honey Singh on 4/20/2017.
 */

public class AddressAdapter extends BaseAdapter {
    ArrayList<AddressDataset> list;
    Activity act;
    TextView tv;
    Dialog dialog;
    Callback callback;
    DeleteCallBack deleteCallBack;
    public AddressAdapter(Activity act, ArrayList<AddressDataset> list, Callback callback, DeleteCallBack deleteCallBack) {
        this.act = act;
        this.list = list;
        this.callback = callback;
        this.deleteCallBack=deleteCallBack;
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
        LayoutInflater inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.addressrow, parent, false);
            holder = new ViewHolder();
            holder.address1 = (TextView) convertView.findViewById(R.id.address1);
            holder.address2 = (TextView) convertView.findViewById(R.id.address2);
            holder.addressType = (TextView) convertView.findViewById(R.id.address4);
            holder.city = (TextView) convertView.findViewById(R.id.address5);
            holder.addressView=(LinearLayout)convertView.findViewById(R.id.addressView);
            holder.edit=(ImageView)convertView.findViewById(R.id.edit);
            holder.delete=(ImageView)convertView.findViewById(R.id.delete);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.address1.setText(list.get(position).getAddress1());
        holder.address2.setText(list.get(position).getAddress2());
        holder.addressType.setText(list.get(position).getCity() + "," + list.get(position).getState() + "," + list.get(position).getPincode());
        holder.city.setText(list.get(position).getAddressType());

        convertView.setTag(holder);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCallBack.onEditClick(list.get(position));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCallBack.onDeleteClick(list.get(position).getAddressId());
            }
        });
        holder.addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteCallBack.onAddresssClick(position);

            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView edit,delete;
        TextView address1, address2, address3, addressType, city;
        LinearLayout addressView;
    }
}