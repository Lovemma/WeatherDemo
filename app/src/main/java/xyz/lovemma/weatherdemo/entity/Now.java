package xyz.lovemma.weatherdemo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OO on 2017/5/19.
 */

public class Now {
    /**
     * cond : {"code":"100","txt":"晴"}
     * fl : 32
     * hum : 44
     * pcpn : 0
     * pres : 1007
     * tmp : 29
     * vis : 6
     * wind : {"deg":"209","dir":"东风","sc":"3-4","spd":"12"}
     */
    @SerializedName("cond")
    private Cond cond;
    @SerializedName("fl")
    private String fl;
    @SerializedName("hum")
    private String hum;
    @SerializedName("pcpn")
    private String pcpn;
    @SerializedName("pres")
    private String pres;
    @SerializedName("tmp")
    private int tmp;
    @SerializedName("vis")
    private String vis;
    @SerializedName("wind")
    private Wind wind;

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public int getTmp() {
        return tmp;
    }

    public void setTmp(int tmp) {
        this.tmp = tmp;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public class Cond {
        /**
         * code : 100
         * txt : 晴
         */
        @SerializedName("code")
        private String code;
        @SerializedName("txt")
        private String txt;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }

    public class Wind {
        /**
         * deg : 209
         * dir : 东风
         * sc : 3-4
         * spd : 12
         */
        @SerializedName("deg")
        private String deg;
        @SerializedName("dir")
        private String dir;
        @SerializedName("sc")
        private String sc;
        @SerializedName("spd")
        private String spd;

        public String getDeg() {
            return deg;
        }

        public void setDeg(String deg) {
            this.deg = deg;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getSc() {
            return sc;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

        public String getSpd() {
            return spd;
        }

        public void setSpd(String spd) {
            this.spd = spd;
        }
    }
}