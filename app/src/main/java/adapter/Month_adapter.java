package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mypaybox.R;


/**
 * Created by Ashish.Kumar on 21-08-2017.
 */

public class Month_adapter extends BaseAdapter {
    String[] months;
    Activity act;

    public Month_adapter(Activity act, String[] months) {
        this.act = act;
        this.months = months;
    }

    @Override
    public int getCount() {
        return months.length;
    }

    @Override
    public Object getItem(int position) {
        return months[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.listrow, parent, false);
            holder = new ViewHolder();
            holder.month = (TextView) convertView.findViewById(R.id.item);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.month.setText(months[position]);
        convertView.setTag(holder);
        return convertView;
    }

    public class ViewHolder {
        TextView month;
    }
}
