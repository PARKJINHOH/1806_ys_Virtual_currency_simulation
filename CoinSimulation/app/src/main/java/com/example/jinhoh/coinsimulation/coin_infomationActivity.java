package com.example.jinhoh.coinsimulation;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
public class coin_infomationActivity extends TabActivity implements TabHost.OnTabChangeListener {
    TabHost tabHost;

    //매수
    TextView TVcoinName, TVcoinPrice, TVMyMoney, TVmaybeBuyCoin, TVMyMoneycoin;
    EditText ETBuyCoin;
    Button BTNBuyCoin, BTNBuyResetCoin;
    Double finCoinMoney;
    Double coincashdouble;

    //매도
    TextView TVMyCoin, TVSellCoin, TVMyMoneycash;
    EditText ETSellCoin;
    Button BTNSellCoin, BTNSellResetCoin;
    Double Icoincash; // 보유한 코인 개수
    Double sellcoincashdouble; // 코인 * 가격

    //코인
    Api_Client api;
    HashMap<String, String> rgParams;
    Double Dprice;

    //DB
    DBCoinHelper coinHelper;
    SQLiteDatabase coindb;
    Cursor cr;

    DBCoinpriceHelper coinpriceHelper;
    SQLiteDatabase coinpricedb;
    Cursor crprice;

    static String stocklist;

    public boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_information);

        //매수
        TVcoinName = (TextView) findViewById(R.id.TVcoinName);
        TVcoinPrice = (TextView) findViewById(R.id.TVcoinPrice);
        TVMyMoney = (TextView) findViewById(R.id.TVMyMoney);
        TVmaybeBuyCoin = (TextView) findViewById(R.id.TVmaybeBuyCoin);
        ETBuyCoin = (EditText) findViewById(R.id.ETBuyCoin);
        ETBuyCoin.setText("0");
        BTNBuyCoin = (Button) findViewById(R.id.BTNBuyCoin);
        BTNBuyResetCoin = (Button) findViewById(R.id.BTNBuyResetCoin);
        TVMyMoneycoin = (TextView) findViewById(R.id.TVMyMoneycoin);

        //매도
        TVMyMoneycash = (TextView) findViewById(R.id.TVMyMoneycash);
        TVMyCoin = (TextView) findViewById(R.id.TVMyCoin);
        ETSellCoin = (EditText) findViewById(R.id.ETSellCoin);
        ETSellCoin.setText("0");
        BTNSellCoin = (Button) findViewById(R.id.BTNSellCoin);
        BTNSellResetCoin = (Button) findViewById(R.id.BTNSellResetCoin);
        TVSellCoin = (TextView) findViewById(R.id.TVSellCoin);


        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec tabBuyCoin = tabHost.newTabSpec("BuyCoin").setIndicator("매수");
        tabBuyCoin.setContent(R.id.LinerBuyCoin);
        tabHost.addTab(tabBuyCoin);

        TabHost.TabSpec tabSellCoin = tabHost.newTabSpec("SellCoin").setIndicator("매도");
        tabSellCoin.setContent(R.id.LinerSellCoin);
        tabHost.addTab(tabSellCoin);
        tabHost.setCurrentTab(0);


        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#7392B5"));
        }
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#4E4E9C"));

        //DBprice
        coinpriceHelper = new DBCoinpriceHelper(this, "coinprice", null, 1);
        coinpricedb = coinpriceHelper.getWritableDatabase(); //DB열기
        coinHelper = new DBCoinHelper(this, "coin", null, 1);
        coindb = coinHelper.getWritableDatabase(); //DB열기

        //api start
        NetworkThread thread = new NetworkThread();
        thread.start();
        //api end

        //DB
        Intent out = getIntent();
        stocklist = out.getStringExtra("coinName");
        final String getid = out.getStringExtra("id");
        TVcoinName.setText(stocklist);




        BTNBuyCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double Icoincash = 0.0;
                Double Icoinprice = 0.0;

                if (coincashdouble < finCoinMoney) {
                    Toast.makeText(getApplicationContext(), "자금이 모자랍니다.", Toast.LENGTH_LONG).show();
                } else {
                    String sqlselect = "select coin" + stocklist + " from coin where coin_id=?";
                    String[] argsselect = {getid};
                    cr = coindb.rawQuery(sqlselect, argsselect);

                    while (cr.moveToNext()) {
                        Icoincash = cr.getDouble(0);
                    }
                    Double countCoin = Icoincash + Double.parseDouble(ETBuyCoin.getText().toString());

                    String sql = "update coin set coin" + stocklist + "=? where coin_id=?";
                    Object[] args = {countCoin.toString(), getid};
                    coindb.execSQL(sql, args);

                    //coinprice
                    String sqlselectprice = "select coinprice" + stocklist + " from coinprice where coin_price_id=?";
                    String[] argsselectprice = {getid};
                    crprice = coinpricedb.rawQuery(sqlselectprice, argsselectprice);

                    while (crprice.moveToNext()) {
                        Icoinprice = crprice.getDouble(0);
                    }
                    if (Icoinprice == 0) {
                        Icoinprice = Dprice;
                    } else {
                        Icoinprice = (Icoinprice + Dprice) / 2;
                    }
                    String sqlprice = "update coinprice set coinprice" + stocklist + "=? where coin_price_id=?";
                    Object[] argsprice = {Icoinprice, getid};
                    coinpricedb.execSQL(sqlprice, argsprice);

                    Toast.makeText(getApplicationContext(), stocklist + "을 " + ETBuyCoin.getText().toString() + "개 구매하셨습니다.", Toast.LENGTH_LONG).show();
                    //coinprice end


                    coincashdouble = coincashdouble - finCoinMoney;
                    String sql2 = "update coin set coinCASH=? where coin_id=?";
                    Object[] args2 = {coincashdouble, getid};
                    String compat = "#,###";
                    DecimalFormat df = new DecimalFormat(compat);
                    TVMyMoney.setText(df.format(coincashdouble) + "원");
                    coindb.execSQL(sql2, args2);
                }
                ETBuyCoin.setText("0");

            }
        });

        BTNSellCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Icoincash < Double.parseDouble(ETSellCoin.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "판매할 수량이 보유 수량보다 많습니다.", Toast.LENGTH_LONG).show();
                } else {
                    //코인 개수 계산
                    Icoincash = Icoincash - Double.parseDouble(ETSellCoin.getText().toString());
                    String sql = "update coin set coin" + stocklist + "=? where coin_id=?";
                    Object[] args = {Icoincash, getid};
                    coindb.execSQL(sql, args);

                    coincashdouble = coincashdouble + sellcoincashdouble;
                    // 캐쉬 계산
                    String sql2 = "update coin set coinCASH=? where coin_id=?";
                    Object[] args2 = {coincashdouble, getid};
                    coindb.execSQL(sql2, args2);
                }
                ETSellCoin.setText("0");

            }
        });

        BTNBuyResetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isRunning = false;
                    NetworkThread thread = new NetworkThread();
                    thread.start();
                    coindb.close();
                    coinpricedb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        BTNSellResetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isRunning = false;
                    NetworkThread thread = new NetworkThread();
                    thread.start();
                    coindb.close();
                    coinpricedb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent outIntent = new Intent(getApplicationContext(), coin_main_Activity.class);
        setResult(1, outIntent);
//        네트워크 스레드 닫기
        try {
            isRunning = false;
            NetworkThread thread = new NetworkThread();
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    public void onTabChanged(String tabId) {
        // Tab 색 변경
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#7392B5"));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#4E4E9C"));
    }


    class NetworkThread extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Api_Client api = new Api_Client("42f5442b73a9754e1c5ac3ff96d2fd7a",
                            "e33b74bc5036859d07f90ef5846880ab");
                    HashMap<String, String> rgParams = new HashMap<String, String>();
                    rgParams.put("order_currency", "BTC");
                    rgParams.put("payment_currency", "KRW");


                    final String result = api.callApi("/public/transaction_history/" + stocklist + "?cont_no=0&count=1", rgParams);

                    Log.e("mssg", "결과값 : " + result);

                    // JSONObject 객체에 담는다.
                    JSONObject obj = new JSONObject(result);
                    obj.toString();
                    String status = obj.getString("status");

                    // 'data' 객체는 Object
                    JSONArray data_list = obj.getJSONArray("data");
                    JSONObject data_list_obj = data_list.getJSONObject(0);
                    String price = data_list_obj.getString("price");
                    Dprice = data_list_obj.getDouble("price");

                    String compat = "#,###";
                    DecimalFormat df = new DecimalFormat(compat);
                    TVcoinPrice.setText(df.format(Dprice) + "원");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    showStockList();
                                    buychangemoney();
                                    sellchangemoney();
                                }
                            });
                        }
                    }).start();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void showStockList() {
        try {
            if (ETBuyCoin.getText().toString().isEmpty()) {
                ETBuyCoin.setText("0");
            }
            String compat = "#,###";
            DecimalFormat df = new DecimalFormat(compat);
            Double calTVcoinPrice = Dprice;
            Double calETBuyCoin = Double.parseDouble(ETBuyCoin.getText().toString());
            finCoinMoney = calTVcoinPrice * calETBuyCoin;
            TVmaybeBuyCoin.setText(df.format(finCoinMoney) + "원");
        } catch (Exception e) {
            ETBuyCoin.setText("0");
        }


    }

    public void buychangemoney() {

        Intent out = getIntent();
        final String getid = out.getStringExtra("id");


        String sql = "select * from coin where coin_id=?";
        String[] args = {getid};
        cr = coindb.rawQuery(sql, args);

        while (cr.moveToNext()) {
            Double Icoincash = cr.getDouble(1);
            coincashdouble = Icoincash;

            String compat = "#,###";
            DecimalFormat df = new DecimalFormat(compat);
            TVMyMoney.setText(df.format(Icoincash) + "원");
            TVMyMoneycash.setText(df.format(Icoincash) + "원");
        }
    }

    public void sellchangemoney() {
        try {
            if (ETSellCoin.getText().toString().isEmpty()) {
                ETSellCoin.setText("0");
            }

            Intent out = getIntent();
            stocklist = out.getStringExtra("coinName");
            String getida = out.getStringExtra("id");

            String sql = "select coin" + stocklist + " from coin where coin_id=?";
            String[] args = {getida};
            cr = coindb.rawQuery(sql, args);

            while (cr.moveToNext()) {
                Icoincash = cr.getDouble(0);
                Icoincash = Double.parseDouble(String.format("%.7f", Icoincash));
                TVMyCoin.setText(Icoincash + "개");
                TVMyMoneycoin.setText(Icoincash + "개");
                sellcoincashdouble = Double.parseDouble(ETSellCoin.getText().toString()) * Dprice;
                String compat = "#,###";
                DecimalFormat df = new DecimalFormat(compat);
                TVSellCoin.setText(df.format(sellcoincashdouble) + "원");
            }
        } catch (Exception e) {
            ETSellCoin.setText("0");
        }
    }
}

