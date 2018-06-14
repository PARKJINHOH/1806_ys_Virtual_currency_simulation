package com.example.jinhoh.coinsimulation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class mycoinlistAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<mycoinlist> mData;

    public mycoinlistAdapter(Context mContext, ArrayList<mycoinlist> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.mycoinlist, null);
        }

        TextView txtCoinpriceTitle = (TextView) convertView.findViewById(R.id.txtCoinpriceTitle);
        TextView txtCoinpricePrice = (TextView) convertView.findViewById(R.id.txtCoinpricePrice);
        TextView txtCoinpriceChange = (TextView) convertView.findViewById(R.id.txtCoinpriceChange);
        TextView txtCoincount = (TextView) convertView.findViewById(R.id.txtCoincount);

        txtCoinpriceTitle.setText(mData.get(position).Coin_title);
        txtCoinpricePrice.setText(mData.get(position).Coin_price);
        txtCoinpriceChange.setText(mData.get(position).Coin_change);
        txtCoincount.setText(mData.get(position).txtCoincount);

        return convertView;
    }
}
