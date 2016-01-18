/**
 *
 */
package ru.example.weatherapp.model;

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

    public void setText(String text) {
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
