package com.example.alvaro.sqlconnection;

/**
 * Created by Alvaro on 04/06/2016.
 */
public class RatesRootDollar {
    String date;
    String base;
    RatesDollar rates;

    public RatesRootDollar(String date, String base, RatesDollar rates) {
        this.date = date;
        this.base = base;
        this.rates = rates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public RatesDollar getRates() {
        return rates;
    }

    public void setRates(RatesDollar rates) {
        this.rates = rates;
    }
}
