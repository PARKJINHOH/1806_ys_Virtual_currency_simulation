package com.example.jinhoh.coinsimulation;


public class mycoinlist {

    String Coin_title;
    String Coin_price;
    String Coin_change;
    String txtCoincount;

    public mycoinlist(String coin_title, String Coin_price, String coin_change, String txtCoincount) {
        this.Coin_title = coin_title;
        this.Coin_price = Coin_price + "원";
        this.Coin_change = coin_change;
        this.txtCoincount = txtCoincount + " " + coin_title;
    }
}
