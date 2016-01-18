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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }
}
