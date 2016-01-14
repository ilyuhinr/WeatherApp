package ru.example.weatherapp.utils;

import android.net.Uri;

import ru.example.weatherapp.database.WeatherProvider;

public class Constants {

    public static String URL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Россия%2C%20Брянск%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public static String DATABASE_NAME = "weather";
    public static int DATABASE_VERSION = 1;
    public static String ASTRONOMY_TABLE_NAME = "astronomy";
    public static String ATMOSPHERE_TABLE_NAME = "atmosphere";
    public static String CHANEL_TABLE_NAME = "chanel";
    public static String CONDITION_TABLE_NAME = "condition";
    public static String FORECAST_TABLE_NAME = "forecast";
    public static String IMAGE_TABLE_NAME = "image";
    public static String ITEM_TABLE_NAME = "item";
    public static String LOCATION_TABLE_NAME = "location";
    public static String UNITS_TABLE_NAME = "units";
    public static String WIND_TABLE_NAME = "wind";
    public static String CITY_TABLE_NAME = "city";
    public static final Uri CITY_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.CITY_TABLE_NAME);
    public static final Uri ASTRONOMY_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.ASTRONOMY_TABLE_NAME);

    public static String SQL_LITE_CREATE_TABLE_ASTRONOMY =
            "CREATE TABLE IF NOT EXISTS astronomy (" +
                    "  id integer primary key autoincrement,\n" +
                    "  sunrise varchar NOT NULL,\n" +
                    "  sunset varchar NOT NULL\n" +
                    "  \n" +
                    ");";
    public static String SQL_LITE_CREATE_TABLE_ATMOSPHERE =
            "CREATE TABLE IF NOT EXISTS atmosphere (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  humidity integer NOT NULL,\n" +
                    "  visibility float NOT NULL,\n" +
                    "  pressure float NOT NULL,\n" +
                    "  rising integer NOT NULL\n" +
                    "  \n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_CHANNEL =
            "CREATE TABLE IF NOT EXISTS chanel (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  title varchar NOT NULL,\n" +
                    "  link varchar NOT NULL,\n" +
                    "  language varchar NOT NULL,\n" +
                    "  description text NOT NULL,\n" +
                    "  lastBuildDate date NOT NULL,\n" +
                    "  ttl float NOT NULL,\n" +
                    "  location_id integer DEFAULT NULL,\n" +
                    "  units_id integer DEFAULT NULL,\n" +
                    "  wind_id integer DEFAULT NULL,\n" +
                    "  astronomy_id integer DEFAULT NULL,\n" +
                    "  image_id integer DEFAULT NULL,\n" +
                    "  item_id integer DEFAULT NULL,\n" +
                    "  atmosphere_id integer DEFAULT NULL,\n" +
                    "  FOREIGN KEY (astronomy_id) REFERENCES astronomy (id),\n" +
                    "  FOREIGN KEY (atmosphere_id) REFERENCES atmosphere (id),\n" +
                    "  FOREIGN KEY (image_id) REFERENCES image (id),\n" +
                    "  FOREIGN KEY (item_id) REFERENCES item (id),\n" +
                    "  FOREIGN KEY (location_id) REFERENCES location (id),\n" +
                    "  FOREIGN KEY (units_id) REFERENCES units (id),\n" +
                    "  FOREIGN KEY (wind_id) REFERENCES wind (id)\n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_CONDITION =
            "CREATE TABLE IF NOT EXISTS condition (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  text text NOT NULL,\n" +
                    "  code integer NOT NULL,\n" +
                    "  temp integer NOT NULL,\n" +
                    "  date date NOT NULL\n" +
                    /*"  item_id integer NOT NULL,\n" +
                    "  FOREIGN KEY (item_id) REFERENCES item (id)\n" +*/
                    ")";
    public static String SQL_LITE_CREATE_TABLE_FORECAST =
            "CREATE TABLE IF NOT EXISTS forecast (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  day varchar NOT NULL,\n" +
                    "  date date NOT NULL,\n" +
                    "  low integer NOT NULL,\n" +
                    "  high integer NOT NULL,\n" +
                    "  text text NOT NULL,\n" +
                    "  code integer NOT NULL,\n" +
                    "  item_id integer NOT NULL,\n" +
                    "  FOREIGN KEY (item_id) REFERENCES item (id)\n" +
                    "  \n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_IMAGE =
            "CREATE TABLE IF NOT EXISTS image (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  title text NOT NULL,\n" +
                    "  link varchar(100) NOT NULL,\n" +
                    "  url varchar(100) NOT NULL,\n" +
                    "  width integer NOT NULL,\n" +
                    "  height integer NOT NULL\n" +
                    "  \n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_ITEM =
            "CREATE TABLE IF NOT EXISTS item (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  title text NOT NULL,\n" +
                    "  link varchar NOT NULL,\n" +
                    "  description text NOT NULL,\n" +
                    "  pubDate date NOT NULL,\n" +
                    "  geoLat float NOT NULL,\n" +
                    "  geoLong float NOT NULL,\n" +
                    "  condition_id integer NOT NULL,\n" +
                    " FOREIGN KEY (condition_id) REFERENCES condition (id)\n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_LOCATION =
            "CREATE TABLE IF NOT EXISTS location (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  city varchar NOT NULL,\n" +
                    "  region varchar NOT NULL,\n" +
                    "  country varchar NOT NULL\n" +
                    "  \n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_UNITS =
            "CREATE TABLE IF NOT EXISTS units (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  temperature varchar NOT NULL,\n" +
                    "  distance varchar NOT NULL,\n" +
                    "  pressure varchar NOT NULL,\n" +
                    "  speed varchar NOT NULL\n" +
                    "  \n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_WIND =
            "CREATE TABLE IF NOT EXISTS wind (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  chill integer NOT NULL,\n" +
                    "  direction integer NOT NULL,\n" +
                    "  speed float NOT NULL\n" +
                    ")";
    public static String SQL_LITE_CREATE_TABLE_CITY =
            "CREATE TABLE IF NOT EXISTS city (" +
                    "  id integer primary key autoincrement,\n" +
                    "  cityName varchar NOT NULL,\n" +
                    "  country varchar NOT NULL," +
                    "  geoLat float,\n" +
                    "  geoLong float\n" +
                    "  \n" +
                    ")";

    public enum ColumnAstronomy {
        ID("id"), SUNRISE("sunrise"), SUNSET("sunset");

        private String stringValue;

        private ColumnAstronomy(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }


}
