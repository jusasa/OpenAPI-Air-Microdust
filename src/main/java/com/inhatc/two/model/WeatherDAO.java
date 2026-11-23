package com.inhatc.two.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherDAO {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public List<WeatherVO> getWeatherByLocation(String serviceKey, String locationName) {
        String[] dateTime = getLatestBaseDateTime();
        return getWeatherByLocation(serviceKey, dateTime[0], dateTime[1], locationName);
    }

    public List<WeatherVO> getWeatherByLocation(String serviceKey, String baseDate, String baseTime, String locationName) {
        RegionCoord coord = RegionCoord.getCoord(locationName);
        return getWeatherList(serviceKey, "1000", "1", baseDate, baseTime, coord.getNx(), coord.getNy());
    }

    public List<WeatherVO> getWeather(String serviceKey, String baseDate, String baseTime, String nx, String ny) {
        return getWeatherList(serviceKey, "1000", "1", baseDate, baseTime, nx, ny);
    }

    public List<WeatherVO> getWeatherList(String serviceKey, String baseDate, String baseTime, String nx, String ny) {
        return getWeatherList(serviceKey, "1000", "1", baseDate, baseTime, nx, ny);
    }

    public List<WeatherVO> getWeatherList(String serviceKey, String numOfRows, String pageNo, String baseDate, String baseTime, String nx, String ny) {
        List<WeatherVO> list = new ArrayList<>();
        try {
            if (baseDate == null || baseDate.trim().isEmpty() || baseTime == null || baseTime.trim().isEmpty()) {
                String[] dt = getLatestBaseDateTime();
                baseDate = dt[0];
                baseTime = dt[1];
            }

            String apiWeather = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";
            StringBuilder sb = new StringBuilder(apiWeather);
            sb.append("serviceKey=").append(serviceKey != null ? serviceKey.trim() : "").append("&");
            sb.append("pageNo=").append(pageNo != null ? pageNo.trim() : "1").append("&");
            sb.append("numOfRows=").append(numOfRows != null ? numOfRows.trim() : "1000").append("&");
            sb.append("dataType=").append("JSON").append("&");
            sb.append("base_date=").append(baseDate.trim()).append("&");
            sb.append("base_time=").append(baseTime.trim()).append("&");
            sb.append("nx=").append(nx != null ? nx.trim() : "60").append("&");
            sb.append("ny=").append(ny != null ? ny.trim() : "127");

            HttpRequest apiRequest = HttpRequest.newBuilder()
                    .uri(URI.create(sb.toString()))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
            ApiErrorHandler.validateResponse(apiResponse);

            String jsonText = apiResponse.body();
            JsonObject root = JsonParser.parseString(jsonText).getAsJsonObject();

            if (root.has("response")) {
                JsonObject responseObj = root.getAsJsonObject("response");
                if (responseObj.has("header")) {
                    JsonObject header = responseObj.getAsJsonObject("header");
                    String resultCode = getSafeString(header, "resultCode", "00");
                    if (!"00".equals(resultCode)) {
                        String resultMsg = getSafeString(header, "resultMsg", "기상청 API 오류 코드: " + resultCode);
                        throw new ApiException(resultMsg);
                    }
                }

                if (responseObj.has("body") && !responseObj.get("body").isJsonNull()) {
                    JsonObject bodyObj = responseObj.getAsJsonObject("body");
                    if (bodyObj.has("items") && !bodyObj.get("items").isJsonNull()) {
                        JsonElement itemsElement = bodyObj.get("items");
                        JsonArray docs = null;
                        if (itemsElement.isJsonArray()) {
                            docs = itemsElement.getAsJsonArray();
                        } else if (itemsElement.isJsonObject() && itemsElement.getAsJsonObject().has("item")) {
                            JsonElement itemElement = itemsElement.getAsJsonObject().get("item");
                            if (itemElement.isJsonArray()) {
                                docs = itemElement.getAsJsonArray();
                            }
                        }

                        if (docs != null) {
                            Map<String, WeatherVO> map = new LinkedHashMap<>();
                            for (int i = 0; i < docs.size(); i++) {
                                JsonObject d = docs.get(i).getAsJsonObject();
                                String category = getSafeString(d, "category", "");
                                String fcstDate = getSafeString(d, "fcstDate", "");
                                String fcstTime = getSafeString(d, "fcstTime", "");
                                String fcstValue = getSafeString(d, "fcstValue", "");

                                String key = fcstDate + "_" + fcstTime;
                                WeatherVO vo = map.computeIfAbsent(key, k -> {
                                    WeatherVO newVo = new WeatherVO();
                                    newVo.setBaseDate(d.has("baseDate") ? getSafeString(d, "baseDate", "") : "");
                                    newVo.setBaseTime(d.has("baseTime") ? getSafeString(d, "baseTime", "") : "");
                                    newVo.setFcstDate(fcstDate);
                                    newVo.setFcstTime(fcstTime);
                                    newVo.setNx(nx);
                                    newVo.setNy(ny);
                                    return newVo;
                                });

                                if (category != null) {
                                    switch (category) {
                                        case "TMP" -> vo.setTemp(fcstValue);
                                        case "PCP" -> vo.setRainAmount(fcstValue);
                                        case "SKY" -> vo.setSky(fcstValue);
                                        case "PTY" -> vo.setPty(fcstValue);
                                        default -> {
                                        }
                                    }
                                }
                            }

                            for (WeatherVO vo : map.values()) {
                                String pty = vo.getPty();
                                String sky = vo.getSky();
                                String desc = "맑음";

                                if (pty == null) {
                                    if (sky == null) {
                                        desc = "맑음(☀️)";
                                    } else {
                                        desc = switch (sky) {
                                            case "3" -> "구름많음(🌥️)";
                                            case "4" -> "흐림(☁️)";
                                            default -> "맑음(☀️)";
                                        };
                                    }
                                } else {
                                    switch (pty) {
                                        case "1" -> desc = "비(☔)";
                                        case "2" -> desc = "비/눈(🌨️/🌧️)";
                                        case "3" -> desc = "눈(🌨️)";
                                        case "4" -> desc = "소나기(🌦️)";
                                        default -> {
                                            if (sky == null) {
                                                desc = "맑음";
                                            } else {
                                                desc = switch (sky) {
                                                    case "3" -> "구름많음(🌥️)";
                                                    case "4" -> "흐림(☁️)";
                                                    default -> "맑음(☀️)";
                                                };
                                            }
                                        }
                                    }
                                }
                                vo.setWeatherDesc(desc);
                            }

                            list.addAll(map.values());
                        }
                    }
                }
            } else if (root.has("cmmMsgHeader")) {
                JsonObject cmm = root.getAsJsonObject("cmmMsgHeader");
                String msg = getSafeString(cmm, "returnAuthMsg", getSafeString(cmm, "errMsg", "공공데이터 서비스 오류"));
                throw new ApiException(msg);
            }
        } catch (ApiException e) {
            throw e;
        } catch (HttpConnectTimeoutException e) {
            throw new ApiException("기상청 API 서버 연결 시간 초과", e);
        } catch (HttpTimeoutException e) {
            throw new ApiException("기상청 API 응답 대기 시간 초과", e);
        } catch (IOException e) {
            throw new ApiException("기상청 API 통신 오류: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("기상청 API 요청이 중단되었습니다.", e);
        } catch (IllegalArgumentException e) {
            throw new ApiException("기상청 API 요청 URL 구성 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ApiException("기상청 데이터 처리 실패: " + e.getMessage(), e);
        }

        return list;
    }

    public static String[] getLatestBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int timeVal = now.getHour() * 100 + now.getMinute();
        LocalDate date = now.toLocalDate();
        String timeStr = "2300";

        if (timeVal < 210) {
            date = date.minusDays(1);
            timeStr = "2300";
        } else if (timeVal < 510) {
            timeStr = "0200";
        } else if (timeVal < 810) {
            timeStr = "0500";
        } else if (timeVal < 1110) {
            timeStr = "0800";
        } else if (timeVal < 1410) {
            timeStr = "1100";
        } else if (timeVal < 1710) {
            timeStr = "1400";
        } else if (timeVal < 2010) {
            timeStr = "1700";
        } else if (timeVal < 2310) {
            timeStr = "2000";
        } else {
            timeStr = "2300";
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new String[]{dateStr, timeStr};
    }

    private String getSafeString(JsonObject obj, String key, String defaultValue) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                String val = obj.get(key).getAsString();
                if (val != null && !val.trim().isEmpty()) {
                    return val.trim();
                }
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
