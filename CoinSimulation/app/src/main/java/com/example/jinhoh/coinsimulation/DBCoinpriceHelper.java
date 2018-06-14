package com.example.jinhoh.coinsimulation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCoinpriceHelper extends SQLiteOpenHelper {
    public DBCoinpriceHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String coinSQL = "create table coinprice(coin_price_id char(20) primary key, ";
        coinSQL += "coinpriceBTC double default 0, coinpriceETH double default 0, coinpriceDASH double default 0, coinpriceLTC double default 0, coinpriceETC double default 0, coinpriceXRP double default 0, coinpriceBCH double default 0, coinpriceQTUM double default 0, coinpriceEOS double default 0, FOREIGN KEY(coin_price_id) REFERENCES member(member_id))";
        db.execSQL(coinSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists coinprice;");
        onCreate(db);
    }
}
