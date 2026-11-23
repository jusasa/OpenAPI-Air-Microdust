package com.inhatc.two.model;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DustDAO {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public List<DustVO> getDustList(String serviceKey, String returnType, String sidoName) {
        return getDustList(serviceKey, returnType, "100", "1.0", sidoName);
    }

    public List<DustVO> getDustList(String serviceKey, String returnType, String numOfRows, String ver, String sidoName) {
        List<DustVO> list = new ArrayList<>();
        try {
            String apiDust = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?";
            StringBuilder sb = new StringBuilder(apiDust);
            sb.append("serviceKey=").append(serviceKey != null ? serviceKey.trim() : "").append("&");
            sb.append("returnType=").append(returnType != null ? returnType.trim() : "json").append("&");
            sb.append("numOfRows=").append(numOfRows != null ? numOfRows.trim() : "100").append("&");
            sb.append("ver=").append(ver != null ? ver.trim() : "1.0").append("&");
            sb.append("sidoName=").append(sidoName != null ? URLEncoder.encode(sidoName.trim(), StandardCharsets.UTF_8) : "");

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
                        String resultMsg = getSafeString(header, "resultMsg", "API 오류 코드: " + resultCode);
                        throw new ApiException(resultMsg);
                    }
                }

                if (responseObj.has("body") && !responseObj.get("body").isJsonNull()) {
                    JsonObject bodyObj = responseObj.getAsJsonObject("body");
                    if (bodyObj.has("items") && !bodyObj.get("items").isJsonNull()) {
                        JsonElement itemsElement = bodyObj.get("items");
                        if (itemsElement.isJsonArray()) {
                            JsonArray docs = itemsElement.getAsJsonArray();
                            for (int i = 0; i < docs.size(); i++) {
                                JsonObject d = docs.get(i).getAsJsonObject();
                                String stationName = getSafeString(d, "stationName", "알 수 없음");
                                String pm10 = getSafeString(d, "pm10Value", "-");
                                String pm10Grade = getSafeString(d, "pm10Grade", "-");
                                String pm25 = getSafeString(d, "pm25Value", "-");
                                String pm25Grade = getSafeString(d, "pm25Grade", "-");

                                list.add(new DustVO(stationName, pm10, pm10Grade, pm25, pm25Grade));
                            }
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
            throw new ApiException("미세먼지 API 서버 연결 시간 초과", e);
        } catch (HttpTimeoutException e) {
            throw new ApiException("미세먼지 API 응답 대기 시간 초과", e);
        } catch (IOException e) {
            throw new ApiException("미세먼지 API 통신 오류: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("미세먼지 API 요청이 중단되었습니다.", e);
        } catch (IllegalArgumentException e) {
            throw new ApiException("미세먼지 API 요청 URL 구성 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ApiException("미세먼지 데이터 처리 실패: " + e.getMessage(), e);
        }

        return list.stream()
                .sorted((vo1, vo2) -> {
                    String s1 = vo1.getStationName() != null ? vo1.getStationName() : "";
                    String s2 = vo2.getStationName() != null ? vo2.getStationName() : "";
                    return s1.compareTo(s2);
                })
                .toList();
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
