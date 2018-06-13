package com.example.jinhoh.coinsimulation;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class coin_main_Activity extends TabActivity implements TabHost.OnTabChangeListener {
    TextView txtMyAllMoney, txtMyVirMoney;
    TabHost tabHost;

    //코인
    Api_Client api;
    HashMap<String, String> rgParams;

    //coinlist
    ListView listView;
    fragment_CoinListAdapter adapter;
    ArrayList<fragment_Coin> coinlist;
    fragment_Coin coin;

    //DB
    DBHelper myHelper;
    DBCoinHelper coinHelper;
    SQLiteDatabase mydb, coindb;
    Cursor cr;


    String[] stocklist = {"BTC", "ETH", "DASH", "LTC", "ETC", "XRP", "BCH", "QTUM", "EOS"};
    String[] stock_price = new String[stocklist.length];
    int[] stock_change_price = new int[stocklist.length];
    double[] cpYesterPercent = new double[stocklist.length];

    public boolean isRunning = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_main);


        listView = (ListView) findViewById(R.id.Coin_listView);

        //api start
        NetworkThread thread = new NetworkThread();
        thread.start();
        //api end

        txtMyAllMoney = (TextView) findViewById(R.id.txtMyAllMoney);
        txtMyVirMoney = (TextView) findViewById(R.id.txtMyVirMoney);
        coinHelper = new DBCoinHelper(this, "coin", null, 1);
        try {
            Intent out = getIntent();
            String getid = out.getStringExtra("id");
            coindb = coinHelper.getWritableDatabase(); //DB열기
            String sql = "select * from coin where coin_id=?";
            String[] args = {getid};
            cr = coindb.rawQuery(sql, args);

            while (cr.moveToNext()) {
                Integer Icoincash = cr.getInt(1);
                String coincash = Icoincash.toString();
                txtMyAllMoney.setText(coincash + "원");

                Integer IcoinCOINCASH = cr.getInt(2);
                String coinCOINCASH = IcoinCOINCASH.toString();
                txtMyVirMoney.setText(coinCOINCASH + "원");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);


        TabHost.TabSpec tabMainCoin = tabHost.newTabSpec("CoinMain").setIndicator("거래소");
        tabMainCoin.setContent(R.id.Linear_CoinExchange);
        tabHost.addTab(tabMainCoin);

        TabHost.TabSpec tabMyCoin = tabHost.newTabSpec("MyCoin").setIndicator("투자내역");
        tabMyCoin.setContent(R.id.Linear_MyCoin);
        tabHost.addTab(tabMyCoin);
        tabHost.setCurrentTab(0);


        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#7392B5"));
        }
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#4E4E9C"));


        //클릭시 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //코인 누른 이름을 가지고 온다
                String coinname = stocklist[position];

                Intent intent = new Intent(getApplicationContext(), coin_infomationActivity.class);
                intent.putExtra("coinName", coinname);
                try {
                    isRunning = false;
                    NetworkThread thread = new NetworkThread();
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivityForResult(intent, 0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            isRunning = true;
            NetworkThread thread = new NetworkThread();
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTabChanged(String tabId) {
        try {
            if (tabId.equals("CoinMain")) {
                isRunning = true;
                NetworkThread thread = new NetworkThread();
                thread.start();
            } else {
                isRunning = false;
                NetworkThread thread = new NetworkThread();
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Tab 색 변경
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#7392B5"));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#4E4E9C"));
    }

    @Override
    public void onBackPressed() {
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

                    for (int i = 0; i < stocklist.length; i++) {
                        final String result = api.callApi("/public/transaction_history/" + stocklist[i] + "?cont_no=0&count=1", rgParams);

                        Log.e("mssg", "결과값 : " + result);

                        // JSONObject 객체에 담는다.
                        JSONObject obj = new JSONObject(result);
                        obj.toString();
                        String status = obj.getString("status");

                        // 'data' 객체는 Object
                        JSONArray data_list = obj.getJSONArray("data");
                        JSONObject data_list_obj = data_list.getJSONObject(0);
                        String price = data_list_obj.getString("price");

                        stock_price[i] = price;

                        String ticker_result = api.callApi("/public/ticker/" + stocklist[i], rgParams);
                        JSONObject obj1 = new JSONObject(ticker_result);
                        JSONObject data_list1 = obj1.getJSONObject("data");
                        int opening_price = Integer.parseInt(data_list1.getString("opening_price"));
                        int closing_price = Integer.parseInt(data_list1.getString("closing_price"));
                        stock_change_price[i] = closing_price - opening_price;
                        cpYesterPercent[i] = ((double) stock_change_price[i] / opening_price) * 100.0;
                        cpYesterPercent[i] = Double.parseDouble(String.format("%.2f", cpYesterPercent[i]));
                    }


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
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < stocklist.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", stocklist[i]);
            map.put("price", stock_price[i]);
            list.add(map);
        }
        final Integer[] CoinImg = {R.drawable.bitcoinimg, R.drawable.ethimg, R.drawable.dashcoin, R.drawable.litecoin, R.drawable.ethereum_classic, R.drawable.ripple
                , R.drawable.bitcoin_cash, R.drawable.qtum, R.drawable.eos};
        String[] key_array = {"name", "price"};
        int[] id_array = {android.R.id.text1, android.R.id.text2};
        String[] price_Percent = new String[CoinImg.length];

        coinlist = new ArrayList<fragment_Coin>();


        for (int i = 0; i < CoinImg.length; i++) {
            if (cpYesterPercent[i] > 0) {
                price_Percent[i] = "+" + Double.toString(cpYesterPercent[i]) + "%";
            } else {
                price_Percent[i] = Double.toString(cpYesterPercent[i]) + "%";
            }
            coin = new fragment_Coin(ContextCompat.getDrawable(this, CoinImg[i]), stocklist[i], stock_price[i], price_Percent[i]);
            coinlist.add(coin);
        }

        adapter = new fragment_CoinListAdapter(getApplicationContext(), coinlist);
        listView.setAdapter(adapter);
    }


}

