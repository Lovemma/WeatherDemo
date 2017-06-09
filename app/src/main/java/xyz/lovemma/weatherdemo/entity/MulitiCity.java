package xyz.lovemma.weatherdemo.entity;

/**
 * Created by OO on 2017/6/8.
 */

public class MulitiCity {
    String city;
    String cond;
    String temp;

    public MulitiCity(String city, String cond, String temp) {
        this.city = city;
        this.cond = cond;
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
