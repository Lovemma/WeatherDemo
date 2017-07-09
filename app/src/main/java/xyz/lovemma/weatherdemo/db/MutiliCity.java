package xyz.lovemma.weatherdemo.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by OO on 2017/6/30.
 */

public class MutiliCity extends DataSupport {
    private int id;
    @Column(unique = true)
    private String city;
    @Column(defaultValue = "0")
    private long time;
    private String json;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
