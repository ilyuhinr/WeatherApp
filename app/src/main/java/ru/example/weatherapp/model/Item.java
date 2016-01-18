/**
 *
 */
package ru.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {
    protected String title;
    protected String link;

    protected String description;

    // protected String guid;

    protected String pubDate;

    protected float geoLat;

    protected float geoLong;

    protected Condition condition;
    @SerializedName("forecast")
    protected List<Forecast> forecasts;

    public Item() {
    }

    public long id;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

   /* public String getGuid() {
        return guid;
    }*/

    public String getPubDate() {
        return pubDate;
    }

    public float getGeoLat() {
        return geoLat;
    }

    public float getGeoLong() {
        return geoLong;
    }

    public Condition getCondition() {
        return condition;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setGeoLat(float geoLat) {
        this.geoLat = geoLat;
    }

    public void setGeoLong(float geoLong) {
        this.geoLong = geoLong;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
