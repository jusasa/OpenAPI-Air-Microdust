package com.inhatc.two.model;

public class DustVO {
    private String stationName;
    private String pm10Value;
    private String pm10Grade;
    private String pm25Value;
    private String pm25Grade;

    public DustVO() {
    }

    public DustVO(String stationName, String pm10Value, String pm10Grade, String pm25Value, String pm25Grade) {
        this.stationName = stationName;
        this.pm10Value = pm10Value;
        this.pm10Grade = pm10Grade;
        this.pm25Value = pm25Value;
        this.pm25Grade = pm25Grade;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(String pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getPm25Grade() {
        return pm25Grade;
    }

    public void setPm25Grade(String pm25Grade) {
        this.pm25Grade = pm25Grade;
    }
}
