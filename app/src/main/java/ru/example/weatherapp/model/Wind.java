/**
 *
 */
package ru.example.weatherapp.model;

public class Wind {

    protected Integer chill;

    protected Integer direction;

    protected Float speed;


    public Integer getChill() {
        return chill;
    }

    public Integer getDirection() {
        return direction;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setChill(Integer chill) {
        this.chill = chill;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }
}
