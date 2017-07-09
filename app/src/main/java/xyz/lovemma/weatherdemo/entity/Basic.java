package xyz.lovemma.weatherdemo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OO on 2017/5/19.
 */

public class Basic {
    /**
     * city : 成都
     * cnty : 中国
     * id : CN101270101
     * lat : 30.65946198
     * lon : 104.06573486
     * update : {"loc":"2017-05-19 15:52","utc":"2017-05-19 07:52"}
     */
    @SerializedName("city")
    private String city;
    @SerializedName("cnty")
    private String cnty;
    @SerializedName("id")
    private String id;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("update")
    private Update update;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public class Update {
        /**
         * loc : 2017-05-19 15:52
         * utc : 2017-05-19 07:52
         */
        @SerializedName("loc")
        private String loc;
        @SerializedName("utc")
        private String utc;

        public String getLoc() {
            return loc;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getUtc() {
            return utc;
        }

        public void setUtc(String utc) {
            this.utc = utc;
        }
    }
}
