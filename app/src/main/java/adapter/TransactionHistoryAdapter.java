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

import common.Utils;
import model.TransactionHistoryModel;


/**
 * Created by Ashish.Kumar on 26-12-2017.
 */

public class TransactionHistoryAdapter extends BaseAdapter {
    ArrayList<TransactionHistoryModel> list;
    Activity activity;

    public TransactionHistoryAdapter(ArrayList<TransactionHistoryModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TransactionHistoryModel model = list.get(position);
        ViewHolder holder;
        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.transaction_history_row, parent, false);
            holder = new ViewHolder();
            holder.propertyName = (TextView) convertView.findViewById(R.id.propertyName);
            holder.ownerName = (TextView) convertView.findViewById(R.id.ownerName);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.account = (TextView) convertView.findViewById(R.id.acc_no);
            holder.bank = (TextView) convertView.findViewById(R.id.bankName);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);
        holder.propertyName.setText("Property Name : "+model.getProperty().getPropetyName());
        holder.ownerName.setText("Owner Name : "+model.getProperty().getOwnerName());
        holder.address.setText("Address : "+model.getProperty().getAddress()+", "+model.getProperty().getLocality()+", "+model.getProperty().getCity());
        holder.account.setText("Account Number : "+model.getModel().getAccNumber());
        holder.bank.setText("Bank : "+model.getModel().getBankName());
        holder.amount.setText("Rent Paid : Rs"+model.getTransactionModel().getTransactionAmount());
        holder.date.setText("Date: "+ Utils.getFormatedDate(model.getTransactionModel().getTransactionDate()));
        holder.comment.setText("Cmment : "+model.getTransactionModel().getComment());
        return convertView;
    }

    public class ViewHolder {
        TextView propertyName,ownerName,address,account,bank,amount,date,comment;
    }
}
