package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mypaybox.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import interfaces.Callback;
import model.VendorItemsDataset;


/**
 * Created by ashish.kumar on 11-04-2017.
 */

public class VendorDetailsListAdapter extends BaseAdapter {
    ArrayList<VendorItemsDataset> listData = new ArrayList<VendorItemsDataset>();
    Activity act;
    LayoutInflater inflator;
    Callback callBack;

    public VendorDetailsListAdapter(Activity act, ArrayList<VendorItemsDataset> listData, Callback callback) {
        this.act = act;
        this.listData = listData;
        inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callBack=callback;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      ViewHolder holder = null;
final VendorItemsDataset data=listData.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.vendordetailsrow, parent, false);
            holder.productImage=(ImageView) convertView.findViewById(R.id.image);
            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.productQuantity = (TextView) convertView.findViewById(R.id.productDetails);
            holder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.productName.setText(data.getName()+"("+data.getItemType()+")");
        holder.productQuantity.setText(data.getMeasurement());
        holder.productPrice.setText("Rs."+data.getPrice());
        if (data.getImagePath() != null) {
            Picasso.with(act).load("http://"+data.getImagePath()).placeholder(R.drawable.placeholder).into(holder.productImage);
        }else{
            holder.productImage.setImageResource(R.drawable.daymilk);
        }
        convertView.setTag(holder);
        if(data.isItemChecked()==true)
        {
            holder.checkbox.setChecked(true);
        }else{
            holder.checkbox.setChecked(false);
        }
       holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked)
               {
                   for (int i = 0; i < listData.size(); i++) {
                       listData.get(i).setItemChecked(false);
                   }
                   listData.get(position).setItemChecked(true);

               }else{
                   for (int i = 0; i < listData.size(); i++) {
                       listData.get(i).setItemChecked(false);
                   }

               }
               callBack.onClick(position);
           }
       });
        return convertView;
    }

    public class ViewHolder {
        TextView productName, productQuantity,productPrice;
        CheckBox checkbox;
        ImageView productImage;
    }
}
