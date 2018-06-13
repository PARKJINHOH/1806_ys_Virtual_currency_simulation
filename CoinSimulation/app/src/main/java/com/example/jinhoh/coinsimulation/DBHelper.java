package com.example.jinhoh.coinsimulation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String memberSQL = "create table member(member_id char(20) unique primary key ,";
//        memberSQL += "password char(20));";
//        db.execSQL(memberSQL);

        String memberSQL = "create table member(member_id char(20) unique primary key,";
        memberSQL += "password char(20));";
        db.execSQL(memberSQL);

//        String coinSQL = "create table coin(coin_num int auto_increment NOT NULL primary key, coinCASH int, coinCOINCASH int, coinBTC int, coinETH int, coinDASH int, coinLTC int" +
//                "coinETC int, coinXRP int, coinBCH int, coinQTUM int, coinEOS int)";
//        db.execSQL(coinSQL);

//        String coinSQL = "create table coin(coin_num int auto_increment NOT NULL primary key, coin_id char(20) ,coinCASH int default 0, coinCOINCASH int default 0,";
//        coinSQL += "coinBTC int default 0, coinETH int default 0, coinDASH int default 0, coinLTC int default 0, coinETC int default 0, coinXRP int default 0, coinBCH int default 0, coinQTUM int default 0, coinEOS int default 0, FOREIGN KEY(coin_id) REFERENCES member(member_id))";
//        db.execSQL(coinSQL)

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists member;");
        onCreate(db);
    }


}
