package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.mypaybox.R;
import com.mypaybox.VendorDetail;

import java.util.ArrayList;

import model.VendorDataset;

public class VendorIistAdapter extends BaseAdapter {
    ArrayList<VendorDataset> listData = new ArrayList<VendorDataset>();
    Activity act;
    LayoutInflater inflator;


    public VendorIistAdapter(Activity act, ArrayList<VendorDataset> listData) {
        this.act = act;
        this.listData = listData;
        inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.sellerrow, parent, false);
            holder.vendorName = (TextView) convertView.findViewById(R.id.vendorName);
            holder.review = (TextView) convertView.findViewById(R.id.review);
            holder.rating = (RatingBar) convertView.findViewById(R.id.ratingBar);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.vendorName.setText(listData.get(position).getName());
        holder.review.setText(listData.get(position).getFeedBack().size()+ "Reviews");
        holder.review.setPaintFlags( holder.review.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.rating.setRating(Float.parseFloat(listData.get(position).getRating()));
        convertView.setTag(holder);
        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(listData.get(position).getFeedBack());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VendorDetail.selectedVendor=listData.get(position);
                Intent in=new Intent(act,VendorDetail.class);
                act.startActivity(in);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView vendorName, review;
        RatingBar rating;
    }
    public void showDialog(ArrayList<String> reviewsList)
    {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reviewspopup);
        dialog.setCancelable(true);
        // Set dialog title
        // set values for custom dialog components - text, image and button
        ListView list = (ListView) dialog.findViewById(R.id.reviewList);
        list.setAdapter(new ReviewsListAdapter(act,reviewsList));
        dialog.show();
    }
}