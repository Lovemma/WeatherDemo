package xyz.lovemma.weatherdemo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OO on 2017/5/19.
 */

public class Aqi {
    /**
     * city : {"aqi":"118","co":"1","no2":"29","o3":"223","pm10":"106","pm25":"73","qlty":"轻度污染","so2":"9"}
     */
    @SerializedName("city")
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public class City {
        /**
         * aqi : 118
         * co : 1
         * no2 : 29
         * o3 : 223
         * pm10 : 106
         * pm25 : 73
         * qlty : 轻度污染
         * so2 : 9
         */
        @SerializedName("aqi")
        private String aqi;
        @SerializedName("co")
        private String co;
        @SerializedName("no2")
        private String no2;
        @SerializedName("o3")
        private String o3;
        @SerializedName("pm10")
        private String pm10;
        @SerializedName("pm25")
        private int pm25;
        @SerializedName("qlty")
        private String qlty;
        @SerializedName("so2")
        private String so2;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getCo() {
            return co;
        }

        public void setCo(String co) {
            this.co = co;
        }

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getO3() {
            return o3;
        }

        public void setO3(String o3) {
            this.o3 = o3;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public int getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public String getQlty() {
            return qlty;
        }

        public void setQlty(String qlty) {
            this.qlty = qlty;
        }

        public String getSo2() {
            return so2;
        }

        public void setSo2(String so2) {
            this.so2 = so2;
        }
    }
}
