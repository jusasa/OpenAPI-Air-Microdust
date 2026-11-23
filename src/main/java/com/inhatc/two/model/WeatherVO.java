package com.inhatc.two.model;

public class WeatherVO {
    private String baseDate;
    private String baseTime;
    private String fcstDate;
    private String fcstTime;
    private String temp;
    private String rainAmount;
    private String sky;
    private String pty;
    private String weatherDesc;
    private String nx;
    private String ny;

    public WeatherVO() {
    }

    public WeatherVO(String fcstDate, String fcstTime, String temp, String rainAmount, String weatherDesc) {
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.temp = temp;
        this.rainAmount = rainAmount;
        this.weatherDesc = weatherDesc;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    // "2100" -> "21시" 형태로 변환 (JSP에서 ${w.fcstHour}로 사용)
    public String getFcstHour() {
        if (fcstTime == null || fcstTime.length() < 2) {
            return fcstTime;
        }
        return fcstTime.substring(0, 2) + "시";
    }

    // 강수형태(PTY) 1:비, 2:비/눈, 3:눈, 4:소나기 -> 비 또는 눈이 오는 상태
    public boolean isRainOrSnow() {
        return "1".equals(pty) || "2".equals(pty) || "3".equals(pty) || "4".equals(pty);
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getRainAmount() {
        return rainAmount;
    }

    public void setRainAmount(String rainAmount) {
        this.rainAmount = rainAmount;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public String getNx() {
        return nx;
    }

    public void setNx(String nx) {
        this.nx = nx;
    }

    public String getNy() {
        return ny;
    }

    public void setNy(String ny) {
        this.ny = ny;
    }
}
