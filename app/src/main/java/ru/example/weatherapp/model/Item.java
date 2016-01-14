/**
 *
 */
package ru.example.weatherapp.model;

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
    protected List<Forecast> forecasts;

    public Item() {
    }

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


}
