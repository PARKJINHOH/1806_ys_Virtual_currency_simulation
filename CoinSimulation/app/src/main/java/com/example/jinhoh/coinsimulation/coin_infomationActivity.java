package com.example.jinhoh.coinsimulation;

import android.app.TabActivity;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class coin_infomationActivity extends TabActivity implements TabHost.OnTabChangeListener {
    TabHost tabHost;

    //매수
    TextView TVcoinName, TVcoinPrice, TVMyMoney;
    EditText ETBuyCoin;
    Button BTNBuyCoin, BTNBuyResetCoin;

    //매도
    TextView TVMyCoin;
    EditText ETSellCoin;
    Button BTNSellCoin, BTNSellResetCoin;

    //코인
    Api_Client api;
    HashMap<String, String> rgParams;


    static String stocklist;
    String stock_price;

    public boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_information);

        //매수
        TVcoinName = (TextView) findViewById(R.id.TVcoinName);
        TVcoinPrice = (TextView) findViewById(R.id.TVcoinPrice);
        TVMyMoney = (TextView) findViewById(R.id.TVMyMoney);
        ETBuyCoin = (EditText)findViewById(R.id.ETBuyCoin);
        BTNBuyCoin = (Button)findViewById(R.id.BTNBuyCoin);
        BTNBuyResetCoin = (Button)findViewById(R.id.BTNBuyResetCoin);

        //매도
        TVMyCoin = (TextView) findViewById(R.id.TVMyCoin);
        ETSellCoin = (EditText)findViewById(R.id.ETSellCoin);
        BTNSellCoin = (Button)findViewById(R.id.BTNSellCoin);
        BTNSellResetCoin = (Button)findViewById(R.id.BTNSellResetCoin);



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


        Intent out = getIntent();
        stocklist = out.getStringExtra("coinName");
        TVcoinName.setText(stocklist);

        //api start
        NetworkThread thread = new NetworkThread();
        thread.start();
        //api end

        BTNBuyResetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ETBuyCoin.setText("");
            }
        });
        BTNSellResetCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ETSellCoin.setText("");
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

                    TVcoinPrice.setText(price);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    showStockList();
                                }
                            });
                        }
                    }).start();
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void showStockList() {

    }


}

