/**
 *
 */
package ru.example.weatherapp.model;

public class Forecast {
    protected int id;
    protected String day;

    protected String date;

    protected int low;
    protected int high;
    protected String text;

    protected int code;

    public Forecast() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
