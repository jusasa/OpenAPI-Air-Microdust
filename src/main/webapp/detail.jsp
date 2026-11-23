<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${sidoName} ${station} - 미세먼지 + 날씨</title>
    <style>
        body { font-family: sans-serif; background: #f2f3f5; margin: 0; padding: 40px 16px; }
        .card { max-width: 380px; margin: 0 auto; background: #fff; border: 1px solid #ddd;
                border-radius: 14px; padding: 20px 24px; }
        .head { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 16px; }
        .head h2 { margin: 0; font-size: 22px; }
        .head span { color: #888; font-size: 13px; }
        .row { display: flex; gap: 10px; margin-bottom: 16px; }
        .box { flex: 1; background: #f6f7f9; border-radius: 10px; padding: 12px; }
        .label { color: #666; font-size: 13px; margin-bottom: 4px; }
        .value { font-size: 22px; font-weight: bold; }
        .sub { font-size: 13px; color: #555; margin-top: 4px; }
        .activity { display: flex; justify-content: space-between; align-items: center;
                    border-top: 1px solid #eee; padding-top: 14px; }
        .badge { padding: 4px 14px; border-radius: 8px; font-weight: bold; }
        .good { background: #e3f4e5; color: #1e7b34; }
        .warn { background: #fff3d6; color: #9a6400; }
        .bad  { background: #fde4e4; color: #b42318; }
        .back { display: block; text-align: center; margin-top: 18px; color: #555; }
    </style>
</head>
<body>
<div class="card">
    <div class="head">
        <h2>${sidoName} ${station}</h2>
        <span>${weather.fcstHour} 기준</span>
    </div>

    <div class="row">
        <div class="box">
            <div class="label">미세먼지 (PM-10)</div>
            <div class="value">${dust.pm10Value}</div>
            <div class="sub">${dust.pm10GradeText} · 초미세 ${dust.pm25Value}</div>
        </div>
        <div class="box">
            <div class="label">날씨</div>
            <c:choose>
                <c:when test="${weather != null}">
                    <div class="value">${weather.temp}℃</div>
                    <div class="sub">${weather.weatherDesc}</div>
                </c:when>
                <c:otherwise>
                    <div class="value">-</div>
                    <div class="sub">예보 없음</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="activity">
        <span>야외활동</span>
        <c:choose>
            <c:when test="${activity == '좋음'}"><span class="badge good">좋음</span></c:when>
            <c:when test="${activity == '주의'}"><span class="badge warn">주의</span></c:when>
            <c:otherwise><span class="badge bad">나쁨</span></c:otherwise>
        </c:choose>
    </div>
</div>
<a class="back" href="javascript:history.back()">← 검색 결과로 돌아가기</a>
</body>
</html>
