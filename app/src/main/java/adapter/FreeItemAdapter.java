package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.mypaybox.CartDetails;
import com.mypaybox.R;

import java.util.ArrayList;

import common.AppController;
import model.FreeItemsDataset;


/**
 * Created by Honey Singh on 12/8/2017.
 */

public class FreeItemAdapter extends BaseAdapter {
    ArrayList<FreeItemsDataset> freeItemsList;
    Activity act;
    AppController controller;
    public FreeItemAdapter(ArrayList<FreeItemsDataset> freeItemsList, Activity act)
    {
        this.freeItemsList=freeItemsList;
        this.act=act;

    }
    @Override
    public int getCount() {
        return freeItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return freeItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        FreeItemsDataset freeItem=freeItemsList.get(position);
        LayoutInflater inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView = inflator.inflate(R.layout.free_item_row, parent, false);
            holder = new ViewHolder();
            holder.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
            holder.itemName=(TextView)convertView.findViewById(R.id.text);
        }else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.itemName.setText(freeItem.getName());
        if(freeItem.isItemChecked()==true)
        {
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    for (int i = 0; i < freeItemsList.size(); i++) {
                       freeItemsList.get(i).setisItemChecked(false);
                    }

                    freeItemsList.get(position).setisItemChecked(true);
                    CartDetails.freeItem=  freeItemsList.get(position);
                }else{
                    for (int i = 0; i < freeItemsList.size(); i++) {
                        freeItemsList.get(i).setisItemChecked(false);

                    }
                    CartDetails.freeItem=null;
                }
                notifyDataSetChanged();
            }
        });
        convertView.setTag(holder);
        return convertView;

    }
    public class ViewHolder{

        TextView itemName;
        CheckBox checkBox;
    }
}
