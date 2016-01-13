/**
 *
 */
package ru.example.weatherapp.model;

import java.util.Date;

public class Condition {


    protected String text;
        protected int code;

    protected int temp;
    protected String date;

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public int getTemp() {
        return temp;
    }

    public String getDate() {
        return date;
    }

}
