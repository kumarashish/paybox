package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mypaybox.R;

import java.util.ArrayList;

/**
 * Created by ashish.kumar on 11-04-2017.
 */

public class ReviewsListAdapter extends BaseAdapter {
    ArrayList<String> listData = new ArrayList<String>();
    Activity act;
    LayoutInflater inflator;


    public ReviewsListAdapter(Activity act, ArrayList<String> listData) {
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
            convertView = inflator.inflate(R.layout.listrow, parent, false);
            holder.review = (TextView) convertView.findViewById(R.id.item);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.review.setText(listData.get(position).toString());

        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder {
        TextView review;

    }
}
