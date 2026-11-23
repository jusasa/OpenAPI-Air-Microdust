package com.inhatc.two.model;

import java.io.ByteArrayInputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ApiErrorHandler {

    public static String extractXmlErrorMessage(String xmlText) {
        if (xmlText == null) {
            return null;
        }
        String trimmed = xmlText.trim();
        if (!trimmed.startsWith("<")) {
            return null;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // XXE 방지 설정
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(trimmed.getBytes(StandardCharsets.UTF_8)));

            String[] targetTags = {"returnAuthMsg", "errMsg", "resultMsg"};
            for (String tag : targetTags) {
                NodeList nodes = doc.getElementsByTagName(tag);
                if (nodes.getLength() > 0) {
                    String text = nodes.item(0).getTextContent();
                    if (text != null && !text.trim().isEmpty()) {
                        return text.trim();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "공공데이터 API 오류 (XML 응답 수신)";
    }

    public static void validateResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        String body = response.body();

        if (statusCode != 200) {
            String xmlError = extractXmlErrorMessage(body);
            if (xmlError != null) {
                throw new ApiException("HTTP " + statusCode + " - " + xmlError);
            }
            throw new ApiException("HTTP 요청 실패 (상태 코드: " + statusCode + ")");
        }

        if (body == null || body.trim().isEmpty()) {
            throw new ApiException("API 응답 데이터가 비어 있습니다.");
        }

        String trimmed = body.trim();
        if (trimmed.startsWith("<")) {
            String xmlError = extractXmlErrorMessage(trimmed);
            throw new ApiException(xmlError != null ? xmlError : "API 오류 응답 (XML 형식 수신)");
        }

        if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) {
            throw new ApiException("API 응답 형식이 올바르지 않습니다.");
        }
    }
}
