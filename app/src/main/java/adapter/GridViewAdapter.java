package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mypaybox.R;
import com.mypaybox.SellerList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import common.AppController;
import model.VendorItemsDataset;


/**
 * Created by Honey Singh on 4/10/2017.
 */

public class GridViewAdapter  extends BaseAdapter {
    private Activity mContext;
    ArrayList<VendorItemsDataset> list;
    String product;
    AppController controller;
    // Constructor
    public GridViewAdapter(Activity c, ArrayList<VendorItemsDataset> list, String product) {
        mContext = c;
        this.list = list;
        this.product=product;
        controller=(AppController)c.getApplicationContext();
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.recyclerrow, parent, false);
            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.productSupplier = (TextView) convertView.findViewById(R.id.suppliedBy);
            holder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            holder.productImage = (ImageView) convertView.findViewById(R.id.productImage);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.productName.setText(list.get(position).getName());
        holder.productSupplier.setText(list.get(position).getSuppliedBy());
        holder.productPrice.setText("");
        holder.productName.setTypeface(controller.getBoldTypeface());
        holder.productSupplier.setTypeface(controller.getBoldTypeface());
        try {
            if(list.get(position).getImagePath()!=null) {
                Picasso.with(mContext).load("http://"+list.get(position).getImagePath()).placeholder(R.drawable.placeholder).into(holder.productImage);
            }else{
                holder.productImage.setImageResource(R.drawable.placeholder);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        convertView.setTag(holder);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerList.heading=product;
                SellerList.brandId=list.get(position).getId();
                Intent in = new Intent(mContext, SellerList.class);
                mContext.startActivity(in);
            }
        });
        return convertView;
    }
public  class ViewHolder{
    TextView productName,productSupplier,productPrice;
    ImageView productImage;

}
}
