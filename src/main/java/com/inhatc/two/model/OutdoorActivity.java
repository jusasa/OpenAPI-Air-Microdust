package com.inhatc.two.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

// 미세먼지 + 날씨를 합쳐 야외활동 지수를 판정하는 클래스
public class OutdoorActivity {
    public static final int DUST_LIMIT = 80; // PM-10 기준값

    /*
     * 야외활동 기준
     *  - 비/눈 O + 미세먼지 80 이상 -> 나쁨
     *  - 비/눈 O (미세먼지 80 미만) -> 주의
     *  - 비/눈 X + 미세먼지 80 이상 -> 주의
     *  - 비/눈 X + 미세먼지 80 미만 -> 좋음
     */
    public static String judge(DustVO dust, WeatherVO weather) {
        boolean rainOrSnow = weather != null && weather.isRainOrSnow();
        boolean highDust = isHighDust(dust);

        if (rainOrSnow && highDust) {
            return "나쁨";
        } else if (rainOrSnow || highDust) {
            return "주의";
        } else {
            return "좋음";
        }
    }

    // PM-10 값이 80 이상인지 확인 (측정값이 "-" 등 숫자가 아니면 false)
    private static boolean isHighDust(DustVO dust) {
        if (dust == null || dust.getPm10Value() == null) return false;
        try {
            return Integer.parseInt(dust.getPm10Value().trim()) >= DUST_LIMIT;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 예보 목록 중 현재 시각과 가장 가까운 예보 하나 찾기
    public static WeatherVO findNearest(List<WeatherVO> list) {
        if (list == null || list.isEmpty()) return null;

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        WeatherVO nearest = null;
        long minDiff = Long.MAX_VALUE;
        for (WeatherVO w : list) {
            try {
                LocalDateTime t = LocalDateTime.parse(w.getFcstDate() + w.getFcstTime(), fmt);
                long diff = Math.abs(java.time.Duration.between(now, t).toMinutes());
                if (diff < minDiff) {
                    minDiff = diff;
                    nearest = w;
                }
            } catch (Exception e) {
                // 날짜 형식이 이상한 항목은 건너뜀
            }
        }
        return nearest;
    }
}
