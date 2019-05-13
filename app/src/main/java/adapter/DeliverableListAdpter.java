package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mypaybox.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import common.AppController;
import common.Utils;
import model.DeliverableDataset;


/**
 * Created by Honey Singh on 5/27/2017.
 */

public class DeliverableListAdpter extends BaseAdapter {
    Activity act;
    ArrayList<DeliverableDataset> deliverableList;
    ArrayList<DeliverableDataset> deliveringList=null;
    AppController controller;
    public DeliverableListAdpter(Activity act, ArrayList<DeliverableDataset> deliverableList) {
        this.act=act;
        this.deliverableList=deliverableList;
        controller=(AppController)act.getApplicationContext();
    }
    public DeliverableListAdpter(Activity act, ArrayList<DeliverableDataset> deliverableList, ArrayList<DeliverableDataset> deliveringList) {
        this.act=act;
        this.deliverableList=deliverableList;
        this.deliveringList=deliveringList;
        controller=(AppController)act.getApplicationContext();
    }

    @Override
    public int getCount() {
        return deliverableList.size();
    }

    @Override
    public Object getItem(int position) {
        return deliverableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflator=(LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {convertView=inflator.inflate(R.layout.productrow,parent,false);
            holder=new ViewHolder();
            holder.add=(ImageButton) convertView.findViewById(R.id.add);
            holder.minus=(ImageButton) convertView.findViewById(R.id.minus);
            holder.productImage=(ImageView) convertView.findViewById(R.id.productImage);
            holder.addCategory=(Button)convertView.findViewById(R.id.addCategory);
            holder.completeLayout=(LinearLayout)convertView.findViewById(R.id.completeLayout);
            holder.productName=(TextView)convertView.findViewById(R.id.productName);
            holder.count=(TextView)convertView.findViewById(R.id.itemCount);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.add.setEnabled(false);
        holder.minus.setEnabled(false);
        holder.productName.setText(deliverableList.get(position).getItemName()+"("+deliverableList.get(position).getItemType()+" "+deliverableList.get(position).getTemCategory()+")");
        Picasso.with(act).load("http://"+deliverableList.get(position).getImagePath()).placeholder(R.drawable.placeholder).into(holder.productImage);
      if(Utils.isItemAlreadyDelivering(deliveringList,deliverableList.get(position)))
      { holder.completeLayout.setBackgroundResource(R.color.white);
          holder.count.setText(String.valueOf(deliverableList.get(position).getQuantity()));
          holder.addCategory.setVisibility(View.GONE);
          holder.add.setEnabled(true);
          holder.minus.setEnabled(true);
      }else{
          holder.count.setText("0");
      }
        holder.addCategory.setTypeface(controller.getTypeface());
        convertView.setTag(holder);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity= Integer.parseInt(holder.count.getText().toString());
                quantity=quantity+1;
                holder.count.setText(String.valueOf(quantity));
                deliverableList.get(position).setQuantity(quantity);
                deliverableList.get(position).setEnabled(true);


            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity= Integer.parseInt(holder.count.getText().toString());
                if(quantity!=0) {
                    quantity = quantity - 1;
                    holder.count.setText(String.valueOf(quantity));
                    deliverableList.get(position).setQuantity(quantity);
                }else{
                    holder.completeLayout.setBackgroundResource(R.color.grey);
                    deliverableList.get(position).setEnabled(false);
                    holder.addCategory.setVisibility(View.VISIBLE);
                    holder.add.setEnabled(false);
                    holder.minus.setEnabled(false);
                }
            }

        });
        holder.addCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.completeLayout.setBackgroundResource(R.color.white);
                holder.addCategory.setVisibility(View.GONE);
                holder.add.setEnabled(true);
                holder.minus.setEnabled(true);
            }
        });

        return convertView;
    }
    public class ViewHolder{
        LinearLayout completeLayout;
        ImageButton add,minus;
        Button addCategory;
        ImageView productImage;
        TextView productName,count;
    }
}
