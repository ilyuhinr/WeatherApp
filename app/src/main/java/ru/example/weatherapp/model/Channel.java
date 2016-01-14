/**
 *
 */
package ru.example.weatherapp.model;

public class Channel {

    public String title;

    public String link;

    public String language;

    public String description;

    public String lastBuildDate;

    public long ttl;
    public Location location;
    public Units units;
    public Wind wind;
    public Astronomy astronomy;
    public Image image;
    public Item item;
    public Atmosphere atmosphere;

    public Channel() {
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public long getTtl() {
        return ttl;
    }

    public Location getLocation() {
        return location;
    }

    public Units getUnits() {
        return units;
    }

    public Wind getWind() {
        return wind;
    }


    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public Image getImage() {
        return image;
    }

    public Item getItem() {
        return item;
    }


}
