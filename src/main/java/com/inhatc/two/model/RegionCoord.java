package com.inhatc.two.model;

import java.util.HashMap;
import java.util.Map;
// 기상청 제공 코드
public class RegionCoord {
    private String nx;
    private String ny;

    public RegionCoord(String nx, String ny) {
        this.nx = nx;
        this.ny = ny;
    }

    public String getNx() {
        return nx;
    }

    public String getNy() {
        return ny;
    }

    private static final double RE = 6371.00877;
    private static final double GRID = 5.0;
    private static final double SLAT1 = 30.0;
    private static final double SLAT2 = 60.0;
    private static final double OLON = 126.0;
    private static final double OLAT = 38.0;
    private static final double XO = 210 / GRID;
    private static final double YO = 675 / GRID;

    private static final double PI = Math.asin(1.0) * 2.0;
    private static final double DEGRAD = PI / 180.0;
    private static final double RADDEG = 180.0 / PI;

    private static final double RE_GRID = RE / GRID;
    private static final double SLAT1_RAD = SLAT1 * DEGRAD;
    private static final double SLAT2_RAD = SLAT2 * DEGRAD;
    private static final double OLON_RAD = OLON * DEGRAD;
    private static final double OLAT_RAD = OLAT * DEGRAD;

    private static final double SN = Math.log(Math.cos(SLAT1_RAD) / Math.cos(SLAT2_RAD))
            / Math.log(Math.tan(PI * 0.25 + SLAT2_RAD * 0.5) / Math.tan(PI * 0.25 + SLAT1_RAD * 0.5));
    private static final double SF = Math.pow(Math.tan(PI * 0.25 + SLAT1_RAD * 0.5), SN) * Math.cos(SLAT1_RAD) / SN;
    private static final double RO = RE_GRID * SF / Math.pow(Math.tan(PI * 0.25 + OLAT_RAD * 0.5), SN);

    public static RegionCoord toGrid(double lat, double lon) {
        double ra = Math.tan(PI * 0.25 + lat * DEGRAD * 0.5);
        ra = RE_GRID * SF / Math.pow(ra, SN);
        double theta = lon * DEGRAD - OLON_RAD;
        if (theta > PI) theta -= 2.0 * PI;
        if (theta < -PI) theta += 2.0 * PI;
        theta *= SN;

        double x1 = ra * Math.sin(theta) + XO;
        double y1 = RO - ra * Math.cos(theta) + YO;
        int x = (int) (x1 + 1.5);
        int y = (int) (y1 + 1.5);
        return new RegionCoord(String.valueOf(x), String.valueOf(y));
    }

    public static double[] toLatLon(int x, int y) {
        double xn = (x - 1) - XO;
        double yn = RO - (y - 1) + YO;
        double ra = Math.sqrt(xn * xn + yn * yn);
        if (SN < 0.0) {
            ra = -ra;
        }
        double alat = Math.pow((RE_GRID * SF / ra), (1.0 / SN));
        alat = 2.0 * Math.atan(alat) - PI * 0.5;

        double theta;
        if (Math.abs(xn) <= 0.0) {
            theta = 0.0;
        } else {
            if (Math.abs(yn) <= 0.0) {
                theta = PI * 0.5;
                if (xn < 0.0) {
                    theta = -theta;
                }
            } else {
                theta = Math.atan2(xn, yn);
            }
        }
        double alon = theta / SN + OLON_RAD;
        double lat = alat * RADDEG;
        double lon = alon * RADDEG;
        return new double[]{lat, lon};
    }

    private static final Map<String, double[]> LOCATION_GPS_MAP = new HashMap<>();

    static {
        LOCATION_GPS_MAP.put("서울", new double[]{37.5636, 126.9800});
        LOCATION_GPS_MAP.put("서울특별시", new double[]{37.5636, 126.9800});
        LOCATION_GPS_MAP.put("부산", new double[]{35.1770, 129.0770});
        LOCATION_GPS_MAP.put("부산광역시", new double[]{35.1770, 129.0770});
        LOCATION_GPS_MAP.put("대구", new double[]{35.8685, 128.6036});
        LOCATION_GPS_MAP.put("대구광역시", new double[]{35.8685, 128.6036});
        LOCATION_GPS_MAP.put("인천", new double[]{37.4532, 126.7074});
        LOCATION_GPS_MAP.put("인천광역시", new double[]{37.4532, 126.7074});
        LOCATION_GPS_MAP.put("광주", new double[]{35.1595, 126.8526});
        LOCATION_GPS_MAP.put("광주광역시", new double[]{35.1595, 126.8526});
        LOCATION_GPS_MAP.put("대전", new double[]{36.3471, 127.3866});
        LOCATION_GPS_MAP.put("대전광역시", new double[]{36.3471, 127.3866});
        LOCATION_GPS_MAP.put("울산", new double[]{35.5354, 129.3137});
        LOCATION_GPS_MAP.put("울산광역시", new double[]{35.5354, 129.3137});
        LOCATION_GPS_MAP.put("세종", new double[]{36.4800, 127.2891});
        LOCATION_GPS_MAP.put("세종특별자치시", new double[]{36.4800, 127.2891});
        LOCATION_GPS_MAP.put("경기", new double[]{37.2718, 127.0117});
        LOCATION_GPS_MAP.put("경기도", new double[]{37.2718, 127.0117});
        LOCATION_GPS_MAP.put("강원", new double[]{37.8827, 127.7320});
        LOCATION_GPS_MAP.put("강원도", new double[]{37.8827, 127.7320});
        LOCATION_GPS_MAP.put("강원특별자치도", new double[]{37.8827, 127.7320});
        LOCATION_GPS_MAP.put("충북", new double[]{36.6325, 127.4936});
        LOCATION_GPS_MAP.put("충청북도", new double[]{36.6325, 127.4936});
        LOCATION_GPS_MAP.put("충남", new double[]{36.6588, 126.6728});
        LOCATION_GPS_MAP.put("충청남도", new double[]{36.6588, 126.6728});
        LOCATION_GPS_MAP.put("전북", new double[]{35.8173, 127.1111});
        LOCATION_GPS_MAP.put("전라북도", new double[]{35.8173, 127.1111});
        LOCATION_GPS_MAP.put("전북특별자치도", new double[]{35.8173, 127.1111});
        LOCATION_GPS_MAP.put("전남", new double[]{34.8130, 126.4650});
        LOCATION_GPS_MAP.put("전라남도", new double[]{34.8130, 126.4650});
        LOCATION_GPS_MAP.put("전남광주통합특별시", new double[]{34.8130, 126.4650});
        LOCATION_GPS_MAP.put("경북", new double[]{36.5760, 128.5058});
        LOCATION_GPS_MAP.put("경상북도", new double[]{36.5760, 128.5058});
        LOCATION_GPS_MAP.put("경남", new double[]{35.2347, 128.6942});
        LOCATION_GPS_MAP.put("경상남도", new double[]{35.2347, 128.6942});
        LOCATION_GPS_MAP.put("제주", new double[]{33.4857, 126.5003});
        LOCATION_GPS_MAP.put("제주도", new double[]{33.4857, 126.5003});
        LOCATION_GPS_MAP.put("제주특별자치도", new double[]{33.4857, 126.5003});
    }

    public static RegionCoord getCoord(String location) {
        if (location == null) {
            return toGrid(37.5636, 126.9800);
        }
        String clean = location.trim();
        if (LOCATION_GPS_MAP.containsKey(clean)) {
            double[] gps = LOCATION_GPS_MAP.get(clean);
            return toGrid(gps[0], gps[1]);
        }

        for (Map.Entry<String, double[]> entry : LOCATION_GPS_MAP.entrySet()) {
            if (clean.contains(entry.getKey()) || entry.getKey().contains(clean)) {
                return toGrid(entry.getValue()[0], entry.getValue()[1]);
            }
        }

        String stripped = clean.replace("특별시", "")
                .replace("광역시", "")
                .replace("특별자치시", "")
                .replace("특별자치도", "")
                .replace("도", "")
                .replace("시", "")
                .replace("군", "")
                .replace("구", "");

        if (!stripped.isEmpty()) {
            if (LOCATION_GPS_MAP.containsKey(stripped)) {
                double[] gps = LOCATION_GPS_MAP.get(stripped);
                return toGrid(gps[0], gps[1]);
            }
            for (Map.Entry<String, double[]> entry : LOCATION_GPS_MAP.entrySet()) {
                if (entry.getKey().startsWith(stripped) || stripped.startsWith(entry.getKey())) {
                    return toGrid(entry.getValue()[0], entry.getValue()[1]);
                }
            }
        }

        return toGrid(37.5636, 126.9800);
    }
}
