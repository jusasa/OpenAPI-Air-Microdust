<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.inhatc.two.model.*"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Insert title here</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <h1> 검색 결과 </h1>

        지역 선택:
        <select id="stationSelect" onchange="filterStation()">
            <option value="all">전체</option>
            <c:forEach items="${dustList}" var="vo">
                <option value="${vo.stationName}">${vo.stationName}</option>
            </c:forEach>
        </select>
        <br><br>

        <div style="overflow-x: auto;">
        <table border="1" id="dustTable">
            <%-- <tr>
                <th colspan="5">예보시간</th>
                <c:forEach items="${weatherList}" var="w">
                    <th>${w.fcstTime}</th>
                </c:forEach>
            </tr>
            <tr>
                <th colspan="5">날씨</th>
                <c:forEach items="${weatherList}" var="w">
                    <td>${w.weatherDesc}</td>
                </c:forEach>
            </tr>
            <tr>
                <th colspan="5">기온</th>
                <c:forEach items="${weatherList}" var="w">
                    <td>${w.temp}℃</td>
                </c:forEach>
            </tr>
            <tr>
                <th colspan="5">강수량</th>
                <c:forEach items="${weatherList}" var="w">
                    <td>${w.rainAmount}</td>
                </c:forEach>
            </tr> --%>
            <tr>
                <th>지역명</th>
                <th>pm10 값</th>
                <%-- <th>pm10 등급</th> --%>
                <th>pm25 값</th>
                <%-- <th>pm 25 등급</th> --%>
                <c:forEach items="${weatherList}" var="w">
                    <th>${w.fcstTime}</th>
                </c:forEach>
            </tr>
            <c:forEach items="${dustList}" var="vo">
                <tr class="data-row" data-station="${vo.stationName}">
                    <td>${vo.stationName}</td>
                    <td>${vo.pm10Value}</td>
                    <%-- <td>${vo.pm10Grade}</td> --%>
                    <td>${vo.pm25Value}</td>
                    <%-- <td>${vo.pm25Grade}</td> --%>
                    <c:forEach items="${weatherList}" var="w">
                        <td>${w.weatherDesc} (${w.temp}℃)</td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        </div>

        <script>
        function filterStation() {
            var selected = document.getElementById("stationSelect").value;
            var rows = document.getElementsByClassName("data-row");
            for (var i = 0; i < rows.length; i++) {
                var station = rows[i].getAttribute("data-station");
                if (selected === "all" || station === selected) {
                    rows[i].style.display = "";
                } else {
                    rows[i].style.display = "none";
                }
            }
        }
        </script>
    </body>
</html>
