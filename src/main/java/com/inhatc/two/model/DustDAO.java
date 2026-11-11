package com.inhatc.two.model;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
            sb.append("serviceKey=").append(serviceKey != null ? serviceKey : "").append("&");
            sb.append("returnType=").append(returnType != null ? returnType : "json").append("&");
            sb.append("numOfRows=").append(numOfRows != null ? numOfRows : "100").append("&");
            sb.append("ver=").append(ver != null ? ver : "1.0").append("&");
            sb.append("sidoName=").append(sidoName != null ? URLEncoder.encode(sidoName, StandardCharsets.UTF_8) : "");

            HttpRequest apiRequest = HttpRequest.newBuilder()
                    .uri(URI.create(sb.toString()))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
            String jsonText = apiResponse.body();

            if (jsonText != null && !jsonText.trim().isEmpty() && jsonText.trim().startsWith("{")) {
                JsonObject root = JsonParser.parseString(jsonText).getAsJsonObject();
                if (root != null && root.has("response") && !root.get("response").isJsonNull()) {
                    JsonObject responseObj = root.getAsJsonObject("response");
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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
