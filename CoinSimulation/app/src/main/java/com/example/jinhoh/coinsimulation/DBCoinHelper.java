package com.example.jinhoh.coinsimulation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCoinHelper extends SQLiteOpenHelper {
    public DBCoinHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String coinSQL = "create table coin(coin_id char(20) primary key, coinCASH double default 0, coinCOINCASH double default 0,";
        coinSQL += "coinBTC double default 0, coinETH double default 0, coinDASH double default 0, coinLTC double default 0, coinETC double default 0, coinXRP double default 0, coinBCH double default 0, coinQTUM double default 0, coinEOS double default 0, FOREIGN KEY(coin_id) REFERENCES member(member_id))";
        db.execSQL(coinSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists coin;");
        onCreate(db);
    }
}
