/**
 *
 */
package ru.example.weatherapp.model;

public class Location {
    protected String city;

    protected String region;

    protected String country;

    public Location() {
    }

    public Location(String city, String region, String country) {
        this.city = city;
        this.region = region;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

}
