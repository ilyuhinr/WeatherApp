/**
 *
 */
package ru.example.weatherapp.model;

public class Forecast {

    protected String day;

    protected String date;

    protected int low;
    protected int high;
    protected String text;

    protected int code;

    public Forecast() {
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


}
