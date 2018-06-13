package com.example.jinhoh.coinsimulation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class fragment_CoinListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<fragment_Coin> mData;

    public fragment_CoinListAdapter(Context c, ArrayList<fragment_Coin> a) {
        mContext = c;
        mData = a;
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
            convertView = View.inflate(mContext, R.layout.fragment_coinlist, null);
        }

        ImageView imgCoin = (ImageView) convertView.findViewById(R.id.imgCoin);
        TextView Coin_title = (TextView) convertView.findViewById(R.id.txtCoinTitle);
        TextView Coin_price = (TextView) convertView.findViewById(R.id.txtCoinPrice);
        TextView Coin_change = (TextView) convertView.findViewById(R.id.txtCoinChange);

        imgCoin.setImageDrawable(mData.get(position).imgCoin);
        Coin_title.setText(mData.get(position).Coin_title);
        Coin_price.setText(mData.get(position).Coin_price);
        Coin_change.setText(mData.get(position).Coin_change);

        return convertView;
    }
}
