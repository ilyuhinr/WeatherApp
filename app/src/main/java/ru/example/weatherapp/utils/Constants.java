package ru.example.weatherapp.utils;

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

    public static String SQL_LITE_CREATE_TABLE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS astronomy (" +
                    "  id integer primary key autoincrement,\n" +
                    "  sunrise varchar NOT NULL DEFAULT '0',\n" +
                    "  sunset varchar NOT NULL DEFAULT '0'\n" +
                    "  \n" +
                    "); " +
                    "CREATE TABLE IF NOT EXISTS atmosphere (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  humidity integer NOT NULL DEFAULT '0',\n" +
                    "  visibility float NOT NULL DEFAULT '0',\n" +
                    "  pressure float NOT NULL DEFAULT '0',\n" +
                    "  rising integer NOT NULL DEFAULT '0'\n" +
                    "  \n" +
                    "); " +
                    "CREATE TABLE IF NOT EXISTS chanel (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  title varchar NOT NULL DEFAULT '0',\n" +
                    "  link varchar NOT NULL DEFAULT '0',\n" +
                    "  language varchar NOT NULL DEFAULT '0',\n" +
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
                    "  ,\n" +
                    "  KEY FK_chanel_location (location_id),\n" +
                    "  KEY FK_chanel_units (units_id),\n" +
                    "  KEY FK_chanel_wind (wind_id),\n" +
                    "  KEY FK_chanel_astronomy (astronomy_id),\n" +
                    "  KEY FK_chanel_image (image_id),\n" +
                    "  KEY FK_chanel_item (item_id),\n" +
                    "  KEY FK_chanel_atmosphere (atmosphere_id),\n" +
                    "  CONSTRAINT FK_chanel_astronomy FOREIGN KEY (astronomy_id) REFERENCES astronomy (id),\n" +
                    "  CONSTRAINT FK_chanel_atmosphere FOREIGN KEY (atmosphere_id) REFERENCES atmosphere (id),\n" +
                    "  CONSTRAINT FK_chanel_image FOREIGN KEY (image_id) REFERENCES image (id),\n" +
                    "  CONSTRAINT FK_chanel_item FOREIGN KEY (item_id) REFERENCES item (id),\n" +
                    "  CONSTRAINT FK_chanel_location FOREIGN KEY (location_id) REFERENCES location (id),\n" +
                    "  CONSTRAINT FK_chanel_units FOREIGN KEY (units_id) REFERENCES units (id),\n" +
                    "  CONSTRAINT FK_chanel_wind FOREIGN KEY (wind_id) REFERENCES wind (id)\n" +
                    ") " +
                    "CREATE TABLE IF NOT EXISTS condition (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  text text NOT NULL,\n" +
                    "  code integer NOT NULL,\n" +
                    "  temp integer NOT NULL,\n" +
                    "  date date NOT NULL,\n" +
                    "  item_id integer NOT NULL DEFAULT '0',\n" +
                    "  ,\n" +
                    "  KEY FK_condition_item (item_id),\n" +
                    "  CONSTRAINT FK_condition_item FOREIGN KEY (item_id) REFERENCES item (id)\n" +
                    ") " +

                    "CREATE TABLE IF NOT EXISTS forecast (\n" +
                    "  id integer unsigned NOT NULL AUTO_INCREMENT,\n" +
                    "  day varchar NOT NULL,\n" +
                    "  date date NOT NULL,\n" +
                    "  low integer NOT NULL,\n" +
                    "  high integer NOT NULL,\n" +
                    "  text text NOT NULL,\n" +
                    "  code integer NOT NULL\n" +
                    "  \n" +
                    ") "
                    +
                    "CREATE TABLE IF NOT EXISTS image (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  title text NOT NULL,\n" +
                    "  link varchar(100) NOT NULL,\n" +
                    "  url varchar(100) NOT NULL,\n" +
                    "  width integer NOT NULL,\n" +
                    "  height integer NOT NULL\n" +
                    "  \n" +
                    ") " +
                    "CREATE TABLE IF NOT EXISTS item (\n" +
                    "  id integer NOT NULL AUTO_INCREMENT,\n" +
                    "  title text NOT NULL,\n" +
                    "  link varchar NOT NULL,\n" +
                    "  description text NOT NULL,\n" +
                    "  guid varchar NOT NULL,\n" +
                    "  pubDate date NOT NULL,\n" +
                    "  geoLat float NOT NULL,\n" +
                    "  geoLong float NOT NULL,\n" +
                    "  condition_id integer NOT NULL,\n" +
                    "  ,\n" +
                    "  KEY FK_item_condition (condition_id),\n" +
                    "  CONSTRAINT FK_item_condition FOREIGN KEY (condition_id) REFERENCES condition (id)\n" +
                    ") " +
                    "CREATE TABLE IF NOT EXISTS location (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  city varchar NOT NULL DEFAULT '0',\n" +
                    "  region varchar NOT NULL DEFAULT '0',\n" +
                    "  country varchar NOT NULL DEFAULT '0'\n" +
                    "  \n" +
                    ") " +
                    "CREATE TABLE IF NOT EXISTS units (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  temperature varchar NOT NULL DEFAULT '0',\n" +
                    "  distance varchar NOT NULL DEFAULT '0',\n" +
                    "  pressure varchar NOT NULL DEFAULT '0',\n" +
                    "  speed varchar NOT NULL DEFAULT '0'\n" +
                    "  \n" +
                    ") " +
                    "CREATE TABLE IF NOT EXISTS wind (\n" +
                    "  id integer primary key autoincrement,\n" +
                    "  chill integer NOT NULL DEFAULT '0',\n" +
                    "  direction integer NOT NULL DEFAULT '0',\n" +
                    "  speed float NOT NULL DEFAULT '0'\n" +
                    ") \n" +
                    "CREATE TABLE IF NOT EXISTS city (" +
                    "  id integer primary key autoincrement,\n" +
                    "  cityName integer NOT NULL DEFAULT '0',\n" +
                    "  country varchar NOT NULL" +
                    "  geoLat float NOT NULL,\n" +
                    "  geoLong float NOT NULL,\n" +
                    "  \n" +
                    ")";
}
