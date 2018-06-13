package com.example.jinhoh.coinsimulation;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class setCashActivity extends AppCompatActivity {

    EditText ETsetcash;
    Button BTNsetCoin, BTNrecencelCoin;

    //DB
    DBCoinHelper coinHelper;
    SQLiteDatabase coindb;
    Cursor cr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setcash);


        ETsetcash = (EditText) findViewById(R.id.ETsetcash);
        BTNsetCoin = (Button) findViewById(R.id.BTNsetCoin);
        BTNrecencelCoin = (Button) findViewById(R.id.BTNrecencelCoin);

        coinHelper = new DBCoinHelper(this, "coin", null, 1);

        Intent getintent = getIntent();
        final String getid = getintent.getStringExtra("id");


        BTNsetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cash = ETsetcash.getText().toString();

                if (Integer.parseInt(cash) >= 10000 && Integer.parseInt(cash) <= 50000000) {
                    coindb = coinHelper.getWritableDatabase(); //DB열기
                    String sql = "update coin set coinCASH=?, coinCOINCASH=0.0,coinBTC=0.0, coinETH=0.0, coinDASH=0.0, coinLTC=0.0, coinETC=0.0, coinXRP=0.0, coinBCH=0.0, coinQTUM=0.0, coinEOS=0.0 where coin_id=?";
                    Object[] args = {cash, getid};
                    coindb.execSQL(sql, args);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"설정금액 사이 값을 넣어주세요.",Toast.LENGTH_LONG).show();
                }


            }
        });

        BTNrecencelCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
