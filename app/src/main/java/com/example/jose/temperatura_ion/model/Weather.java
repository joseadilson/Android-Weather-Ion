package com.example.jose.temperatura_ion.model;

/**
 * Created by Jose on 04/01/2017.
 */

public class Weather {
    private String date;
    private String day;
    private String high;
    private String low;
    private String text;

    public Weather(String date,String day, String high, String low, String text) {
        this.date = date;
        this.day  = day;
        this.high = high;
        this.low  = low;
        this.text = text;
    }

    public Weather(){ }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
