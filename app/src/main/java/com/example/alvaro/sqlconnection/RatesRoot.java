package com.example.alvaro.sqlconnection;

/**
 * Created by Alvaro on 04/06/2016.
 */
public class RatesRoot {
    String date;
    String base;
    Rates rates;

    public RatesRoot(String date, String base, Rates rates) {
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

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }
}
