package com.example.jinhoh.coinsimulation;

import android.graphics.drawable.Drawable;

public class fragment_Coin {

    Drawable imgCoin;
    String Coin_title;
    String Coin_price;
    String Coin_change;

    public fragment_Coin(Drawable imgCoin, String coin_title, String Coin_price, String coin_change) {
        this.imgCoin = imgCoin;
        this.Coin_title = coin_title;
        this.Coin_price = Coin_price;
        this.Coin_change = coin_change;
    }
}
