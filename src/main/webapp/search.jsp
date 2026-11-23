<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.inhatc.two.model.*"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${sidoName} 공기질 & 날씨 검색 결과</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --bg-color: #f8fafc;
            --card-bg: #ffffff;
            --border-color: #e2e8f0;
            --text-main: #1e293b;
            --text-sub: #64748b;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Noto Sans KR", sans-serif;
            background-color: var(--bg-color);
            color: var(--text-main);
            padding: 28px 20px 40px;
            line-height: 1.5;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        /* Top Header */
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 16px;
            background: var(--card-bg);
            padding: 22px 28px;
            border-radius: 16px;
            border: 1px solid var(--border-color);
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            margin-bottom: 20px;
        }

        .title-group h1 {
            font-size: 23px;
            font-weight: 700;
            color: #0f172a;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .title-group p {
            color: var(--text-sub);
            font-size: 14px;
            margin-top: 4px;
        }

        .btn-back {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: #f1f5f9;
            color: #334155;
            padding: 9px 16px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: 600;
            font-size: 14px;
            transition: all 0.2s;
            border: 1px solid #cbd5e1;
        }

        .btn-back:hover {
            background: #e2e8f0;
            color: #0f172a;
        }

        /* Section Card */
        .section-card {
            background: var(--card-bg);
            border-radius: 16px;
            border: 1px solid var(--border-color);
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            padding: 22px 24px;
            margin-bottom: 24px;
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
        }

        .section-title {
            font-size: 17px;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 8px;
            color: #0f172a;
        }

        .section-desc {
            font-size: 13px;
            color: var(--text-sub);
        }

        /* Weather Horizontal Scroll */
        .weather-scroll-container {
            display: flex;
            gap: 12px;
            overflow-x: auto;
            padding-bottom: 10px;
            scrollbar-width: thin;
        }

        .weather-scroll-container::-webkit-scrollbar {
            height: 6px;
        }
        .weather-scroll-container::-webkit-scrollbar-thumb {
            background: #cbd5e1;
            border-radius: 10px;
        }

        .weather-card {
            flex: 0 0 auto;
            min-width: 105px;
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            padding: 12px 10px;
            text-align: center;
            transition: transform 0.15s, border-color 0.15s;
        }

        .weather-card:hover {
            transform: translateY(-2px);
            border-color: var(--primary);
            background: #ffffff;
            box-shadow: 0 4px 10px rgba(37, 99, 235, 0.08);
        }

        .w-hour {
            font-size: 13px;
            font-weight: 600;
            color: #64748b;
            margin-bottom: 5px;
        }

        .w-desc {
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 5px;
            white-space: nowrap;
        }

        .w-temp {
            font-size: 16px;
            font-weight: 700;
            color: #1e293b;
        }

        /* Toolbar (Filter & Search) */
        .toolbar {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 18px;
        }

        .toolbar-left {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            align-items: center;
        }

        .search-box {
            position: relative;
        }

        .search-box input {
            padding: 9px 14px 9px 34px;
            border: 1px solid var(--border-color);
            border-radius: 10px;
            font-size: 14px;
            width: 220px;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .search-box input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .search-icon {
            position: absolute;
            left: 11px;
            top: 50%;
            transform: translateY(-50%);
            color: #94a3b8;
            font-size: 14px;
        }

        .select-custom {
            padding: 9px 14px;
            border: 1px solid var(--border-color);
            border-radius: 10px;
            font-size: 14px;
            background-color: #fff;
            color: var(--text-main);
            outline: none;
            cursor: pointer;
        }

        .select-custom:focus {
            border-color: var(--primary);
        }

        .view-toggle-btn {
            background: #f1f5f9;
            border: 1px solid #cbd5e1;
            padding: 8px 14px;
            border-radius: 10px;
            font-size: 13px;
            font-weight: 600;
            color: #475569;
            cursor: pointer;
            transition: all 0.2s;
        }

        .view-toggle-btn.active, .view-toggle-btn:hover {
            background: var(--primary);
            color: #ffffff;
            border-color: var(--primary);
        }

        .count-badge {
            font-size: 14px;
            color: #64748b;
        }

        .count-badge strong {
            color: var(--primary);
        }

        /* Table */
        .table-wrapper {
            overflow-x: auto;
            border-radius: 12px;
            border: 1px solid var(--border-color);
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
            text-align: left;
            background: #ffffff;
        }

        thead th {
            background-color: #f8fafc;
            color: #475569;
            font-weight: 600;
            padding: 13px 16px;
            border-bottom: 1px solid var(--border-color);
            white-space: nowrap;
        }

        tbody td {
            padding: 13px 16px;
            border-bottom: 1px solid #f1f5f9;
            color: #334155;
            vertical-align: middle;
            white-space: nowrap;
        }

        tbody tr:hover {
            background-color: #f8fafc;
        }

        .station-link {
            color: var(--primary);
            font-weight: 600;
            text-decoration: none;
            font-size: 15px;
            transition: color 0.15s;
        }

        .station-link:hover {
            color: var(--primary-hover);
            text-decoration: underline;
        }

        .btn-detail {
            display: inline-block;
            padding: 5px 12px;
            background: #eff6ff;
            color: var(--primary);
            border-radius: 8px;
            font-weight: 600;
            font-size: 13px;
            text-decoration: none;
            transition: all 0.2s;
        }

        .btn-detail:hover {
            background: var(--primary);
            color: #ffffff;
        }

        /* Grade Badges */
        .grade-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 3px 8px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: 600;
            margin-left: 6px;
        }

        .grade-1 { background: #dcfce7; color: #15803d; border: 1px solid #bbf7d0; } /* 좋음 */
        .grade-2 { background: #e0f2fe; color: #0369a1; border: 1px solid #bae6fd; } /* 보통 */
        .grade-3 { background: #ffedd5; color: #c2410c; border: 1px solid #fed7aa; } /* 나쁨 */
        .grade-4 { background: #fee2e2; color: #b91c1c; border: 1px solid #fecaca; } /* 매우나쁨 */
        .grade-none { background: #f1f5f9; color: #64748b; border: 1px solid #e2e8f0; }

        .dust-val {
            font-weight: 600;
            color: #1e293b;
        }

        /* Weather Columns in Extended View */
        .weather-col {
            display: none;
            font-size: 13px;
            text-align: center;
        }
        .show-weather .weather-col {
            display: table-cell;
        }

        .error-banner {
            background: #fef2f2;
            border: 1px solid #fecaca;
            border-radius: 14px;
            padding: 16px 20px;
            margin-bottom: 20px;
            color: #991b1b;
            font-size: 14px;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .error-banner-item {
            display: flex;
            align-items: flex-start;
            gap: 8px;
            line-height: 1.4;
        }
    </style>
</head>
<body>
<div class="container">

    <!-- 상단 헤더 -->
    <header class="page-header">
        <div class="title-group">
            <h1>📍 ${sidoName} 공기질 & 날씨 종합 현황</h1>
            <p>한국환경공단 에어코리아 미세먼지 및 기상청 단기예보 실시간 데이터</p>
        </div>
        <a href="index.jsp" class="btn-back">🔍 다른 지역 검색</a>
    </header>

    <c:if test="${not empty dustError or not empty weatherError}">
        <div class="error-banner">
            <c:if test="${not empty dustError}">
                <div class="error-banner-item">
                    <span>⚠️</span>
                    <div><strong>미세먼지 정보 수신 실패:</strong> <c:out value="${dustError}"/></div>
                </div>
            </c:if>
            <c:if test="${not empty weatherError}">
                <div class="error-banner-item">
                    <span>⚠️</span>
                    <div><strong>날씨 예보 수신 실패:</strong> <c:out value="${weatherError}"/></div>
                </div>
            </c:if>
        </div>
    </c:if>

    <!-- 상단 시간대별 날씨 예보 슬라이더 -->
    <c:if test="${not empty weatherList}">
        <section class="section-card">
            <div class="section-header">
                <div class="section-title">
                    <span>⏱️ ${sidoName} 시간대별 날씨 예보</span>
                </div>
                <span class="section-desc">가로로 스크롤하여 향후 예보를 확인하세요</span>
            </div>
            <div class="weather-scroll-container">
                <c:forEach items="${weatherList}" var="w">
                    <div class="weather-card">
                        <div class="w-hour">${w.fcstHour}</div>
                        <div class="w-desc">${w.weatherDesc}</div>
                        <div class="w-temp">${w.temp}℃</div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>
    <c:if test="${empty weatherList and not empty weatherError}">
        <section class="section-card">
            <div class="section-header">
                <div class="section-title">
                    <span>⏱️ ${sidoName} 시간대별 날씨 예보</span>
                </div>
            </div>
            <div style="text-align: center; padding: 24px; color: #dc2626; font-size: 14px;">
                날씨 예보 데이터를 불러오지 못했습니다. (<c:out value="${weatherError}"/>)
            </div>
        </section>
    </c:if>

    <!-- 측정소별 대기질 현황 -->
    <section class="section-card">
        <div class="section-header">
            <div class="section-title">
                <span>🍃 측정소별 미세먼지(대기질) 현황</span>
            </div>
            <div class="count-badge">
                검색된 측정소: <strong id="visibleCount">${dustList.size()}</strong> / ${dustList.size()}개
            </div>
        </div>

        <!-- 필터 및 검색 툴바 -->
        <div class="toolbar">
            <div class="toolbar-left">
                <div class="search-box">
                    <span class="search-icon">🔍</span>
                    <input type="text" id="stationInput" onkeyup="filterStation()" placeholder="측정소명 검색 (예: 검단)...">
                </div>

                <select id="stationSelect" class="select-custom" onchange="syncSelectAndFilter()">
                    <option value="all">전체 측정소 목록</option>
                    <c:forEach items="${dustList}" var="vo">
                        <option value="${vo.stationName}">${vo.stationName}</option>
                    </c:forEach>
                </select>
            </div>

            <div>
                <button type="button" class="view-toggle-btn" id="toggleWeatherBtn" onclick="toggleWeatherCols()">
                    📊 시간대별 전체 날씨 펼치기
                </button>
            </div>
        </div>

        <!-- 대기질 데이터 테이블 -->
        <div class="table-wrapper" id="tableContainer">
            <table id="dustTable">
                <thead>
                    <tr>
                        <th>측정소명</th>
                        <th>미세먼지 (PM-10)</th>
                        <th>초미세먼지 (PM-2.5)</th>
                        <th>현재 날씨 요약</th>
                        <th>상세 분석</th>
                        <c:forEach items="${weatherList}" var="w">
                            <th class="weather-col">${w.fcstHour}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty dustError}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 40px; color: #dc2626; font-size: 14px; font-weight: 500;">
                                    ⚠️ 미세먼지 데이터를 가져오지 못했습니다. (<c:out value="${dustError}"/>)
                                </td>
                            </tr>
                        </c:when>
                        <c:when test="${empty dustList}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 40px; color: #94a3b8;">
                                    조회된 측정소 데이터가 없습니다.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${dustList}" var="vo">
                                <tr class="data-row" data-station="${vo.stationName}">
                                    <td>
                                        <c:url var="detailUrl" value="detail">
                                            <c:param name="station" value="${vo.stationName}"/>
                                        </c:url>
                                        <a href="${detailUrl}" class="station-link">
                                            📍 ${vo.stationName}
                                        </a>
                                    </td>
                                    <td>
                                        <span class="dust-val">${vo.pm10Value}</span> <small style="color:#64748b;">㎍/㎥</small>
                                        <c:choose>
                                            <c:when test="${vo.pm10Grade == '1'}"><span class="grade-badge grade-1">좋음</span></c:when>
                                            <c:when test="${vo.pm10Grade == '2'}"><span class="grade-badge grade-2">보통</span></c:when>
                                            <c:when test="${vo.pm10Grade == '3'}"><span class="grade-badge grade-3">나쁨</span></c:when>
                                            <c:when test="${vo.pm10Grade == '4'}"><span class="grade-badge grade-4">매우나쁨</span></c:when>
                                            <c:otherwise><span class="grade-badge grade-none">-</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="dust-val">${vo.pm25Value}</span> <small style="color:#64748b;">㎍/㎥</small>
                                        <c:choose>
                                            <c:when test="${vo.pm25Grade == '1'}"><span class="grade-badge grade-1">좋음</span></c:when>
                                            <c:when test="${vo.pm25Grade == '2'}"><span class="grade-badge grade-2">보통</span></c:when>
                                            <c:when test="${vo.pm25Grade == '3'}"><span class="grade-badge grade-3">나쁨</span></c:when>
                                            <c:when test="${vo.pm25Grade == '4'}"><span class="grade-badge grade-4">매우나쁨</span></c:when>
                                            <c:otherwise><span class="grade-badge grade-none">-</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${not empty weatherList}">
                                            ${weatherList[0].weatherDesc} (${weatherList[0].temp}℃)
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${detailUrl}" class="btn-detail">
                                            상세 카드 보기 ➔
                                        </a>
                                    </td>
                                    <c:forEach items="${weatherList}" var="w">
                                        <td class="weather-col">${w.weatherDesc} (${w.temp}℃)</td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </section>

</div>

<script>
function syncSelectAndFilter() {
    var selectVal = document.getElementById("stationSelect").value;
    var input = document.getElementById("stationInput");
    if (selectVal === "all") {
        input.value = "";
    } else {
        input.value = selectVal;
    }
    filterStation();
}

function filterStation() {
    var query = document.getElementById("stationInput").value.trim().toLowerCase();
    var select = document.getElementById("stationSelect");
    var rows = document.getElementsByClassName("data-row");
    var visible = 0;

    for (var i = 0; i < rows.length; i++) {
        var station = (rows[i].getAttribute("data-station") || "").toLowerCase();
        if (query === "" || station.indexOf(query) !== -1) {
            rows[i].style.display = "";
            visible++;
        } else {
            rows[i].style.display = "none";
        }
    }

    document.getElementById("visibleCount").innerText = visible;
}

function toggleWeatherCols() {
    var container = document.getElementById("tableContainer");
    var btn = document.getElementById("toggleWeatherBtn");
    if (container.classList.contains("show-weather")) {
        container.classList.remove("show-weather");
        btn.classList.remove("active");
        btn.innerText = "📊 시간대별 전체 날씨 펼치기";
    } else {
        container.classList.add("show-weather");
        btn.classList.add("active");
        btn.innerText = "✕ 시간대별 날씨 접기";
    }
}
</script>
</body>
</html>
