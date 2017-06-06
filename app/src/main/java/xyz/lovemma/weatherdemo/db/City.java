package xyz.lovemma.weatherdemo.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OO on 2017/2/5.
 */

public class City implements Parcelable{

    /**
     * id : CN101010100
     * cityEn : beijing
     * cityZh : 北京
     * countryCode : CN
     * countryEn : China
     * countryZh : 中国
     * provinceEn : beijing
     * provinceZh : 北京
     * leaderEn : beijing
     * leaderZh : 北京
     * lat : 39.904989
     * lon : 116.405285
     */

    private String id;
    private String cityEn;
    private String cityZh;
    private String countryCode;
    private String countryEn;
    private String countryZh;
    private String provinceEn;
    private String provinceZh;
    private String leaderEn;
    private String leaderZh;
    private float lat;
    private float lon;

    public City(String id, String cityEn, String cityZh, String countryCode, String countryEn, String countryZh, String provinceEn, String provinceZh, String leaderEn, String leaderZh, float lat, float lon) {
        this.id = id;
        this.cityEn = cityEn;
        this.cityZh = cityZh;
        this.countryCode = countryCode;
        this.countryEn = countryEn;
        this.countryZh = countryZh;
        this.provinceEn = provinceEn;
        this.provinceZh = provinceZh;
        this.leaderEn = leaderEn;
        this.leaderZh = leaderZh;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCityZh() {
        return cityZh;
    }

    public void setCityZh(String cityZh) {
        this.cityZh = cityZh;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getCountryZh() {
        return countryZh;
    }

    public void setCountryZh(String countryZh) {
        this.countryZh = countryZh;
    }

    public String getProvinceEn() {
        return provinceEn;
    }

    public void setProvinceEn(String provinceEn) {
        this.provinceEn = provinceEn;
    }

    public String getProvinceZh() {
        return provinceZh;
    }

    public void setProvinceZh(String provinceZh) {
        this.provinceZh = provinceZh;
    }

    public String getLeaderEn() {
        return leaderEn;
    }

    public void setLeaderEn(String leaderEn) {
        this.leaderEn = leaderEn;
    }

    public String getLeaderZh() {
        return leaderZh;
    }

    public void setLeaderZh(String leaderZh) {
        this.leaderZh = leaderZh;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.cityEn);
        dest.writeString(this.cityZh);
        dest.writeString(this.countryCode);
        dest.writeString(this.countryEn);
        dest.writeString(this.countryZh);
        dest.writeString(this.provinceEn);
        dest.writeString(this.provinceZh);
        dest.writeString(this.leaderEn);
        dest.writeString(this.leaderZh);
        dest.writeFloat(this.lat);
        dest.writeFloat(this.lon);
    }

    protected City(Parcel in) {
        this.id = in.readString();
        this.cityEn = in.readString();
        this.cityZh = in.readString();
        this.countryCode = in.readString();
        this.countryEn = in.readString();
        this.countryZh = in.readString();
        this.provinceEn = in.readString();
        this.provinceZh = in.readString();
        this.leaderEn = in.readString();
        this.leaderZh = in.readString();
        this.lat = in.readFloat();
        this.lon = in.readFloat();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
