package ru.example.weatherapp.utils;

import android.net.Uri;

import ru.example.weatherapp.database.WeatherProvider;

public class Constants {

    public static String DATABASE_NAME = "weather";
    public static int DATABASE_VERSION = 1;
    public static String ASTRONOMY_TABLE_NAME = "astronomy";
    public static String ATMOSPHERE_TABLE_NAME = "atmosphere";
    public static String CHANEL_TABLE_NAME = "channel";
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
    public static final Uri ATMOSPHERE_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.ATMOSPHERE_TABLE_NAME);
    public static final Uri CONDITION_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.CONDITION_TABLE_NAME);
    public static final Uri ITEM_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.ITEM_TABLE_NAME);
    public static final Uri FORECAST_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.FORECAST_TABLE_NAME);
    public static final Uri LOCATION_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.LOCATION_TABLE_NAME);
    public static final Uri UNITS_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.UNITS_TABLE_NAME);
    public static final Uri WIND_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.WIND_TABLE_NAME);
    public static final Uri CHANNEL_CONTENT_URI = Uri.parse("content://"
            + WeatherProvider.AUTHORITY + "/" + Constants.CHANEL_TABLE_NAME);

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
            "CREATE TABLE IF NOT EXISTS channel (\n" +
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

    public enum ColumnAtmosphere {
        ID("id"), HUMIDITY("humidity"), VISIBILITY("visibility"), PRESSURE("pressure"), RISING("rising");

        private String stringValue;

        private ColumnAtmosphere(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnConditions {
        ID("id"), TEXT("text"), CODE("code"), TEMP("temp"), DATE("date");

        private String stringValue;

        private ColumnConditions(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnItem {
        ID("id"), LINK("link"), TITLE("title"), DESCR("description"), PUBDATE("pubDate"), LAT("geoLat"), LONG("geoLong"), CONDITION_ID("condition_id");

        private String stringValue;

        private ColumnItem(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnForecast {
        ID("id"), DAY("day"), DATE("date"), LOW("low"), HIGH("high"), TEXT("text"), CODE("code");

        private String stringValue;

        private ColumnForecast(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnLocation {
        ID("id"), CITY("city"), REGIION("region"), CONTRY("contry");

        private String stringValue;

        private ColumnLocation(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnUnits {
        ID("id"), TEMP("temperature"), DISTANCE("distance"), PRESSURE("pressure"), SPEED("speed");

        private String stringValue;

        private ColumnUnits(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnWind {
        ID("id"), CHILL("chill"), DIRECTION("direction"), SPEED("speed");

        private String stringValue;

        private ColumnWind(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public enum ColumnChannel {
        ID("id"), TITLE("title"), LINK("link"), LANGUAGE("language"), DESCRIPTION("description"), LAST_BUILD_DATE("lastBuildDate"), TTL("ttl"), LOCATION_ID("location_id"), UNITS_ID("units_id"), WIND_ID("wind_id"), ASTRONOMY_ID("astronomy_id"), IMAGE_ID("image_id"), ITEM_ID("item_id"), ATMOSPHERE_ID("atmosphere_id");

        private String stringValue;

        private ColumnChannel(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

}
