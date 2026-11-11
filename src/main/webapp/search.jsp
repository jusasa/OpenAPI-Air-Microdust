<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.inhatc.two.model.DustDAO, com.inhatc.two.model.DustVO"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Insert title here</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <%
            // 메서드

            // 공용 파라메터
            request.setCharacterEncoding("UTF-8");
            String serviceKey = request.getParameter("serviceKey");
            String returnType = request.getParameter("returnType");
            String numOfRows = "100";

            // 미세먼지 api 받고 매핑하기 (키가 같음으로 공유해서 사용)
            String sidoName = request.getParameter("sidoName");
            String ver = "1.0";

            // 매크로로 드롭다운 만들어서 특정 지역만 볼 수 있게 만들기 위해 따로 분리.
            //리스트에 들어가는 순서는 동일하기에 인덱스로 접근하면 무리 없을듯?
            List<DustVO> dustList = (List<DustVO>) request.getAttribute("dustList");
            if (dustList == null) {
                DustDAO dustDAO = new DustDAO();
                dustList = dustDAO.getDustList(serviceKey, returnType, numOfRows, ver, sidoName);
            }

            // 기상청 단기예보 api 가져오고 저장
            // String base_date = "";
            // String base_time = "";

            // String api_dust = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0?"

            // StringBuilder sb = new StringBuilder(api_dust);
            // sb.append("serviceKey=").append(serviceKey2).append("&");
            // sb.append("returnType=").append(returnType).append("&");
            // sb.append("numOfRows=").append(numOfRows).append("&");
            // sb.append("base_date=").append(base_date).append("&");
            // sb.append("base_time=").append(base_date).append("&");

            // String query = sb.toString();

            // HttpClient client = HttpClient.newHttpClient();
            // HttpRequest apiRequest = HttpRequest.newBuilder()
            //     .uri(URI.create(query))
            //     .build();
            // HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
            // String jsonText = apiResponse.body();
            // JsonObject root = JsonParser.parseString(jsonText).getAsJsonObject();
            // JsonArray docs = root.getAsJsonArray("items");

            // List<String> dustData = new ArrayList<>();
            // for (int i = 0; i < docs.size(); i++) {
            //     StringBuilder data = new StringBuilder();
            //     JsonObject d = docs.get(i).getAsJsonObject();
            //     String stationName = d.get("stationName").getAsString();
            //     int pm10 = d.get("pm10Value").getAsInteger();
            //     int pm10Grade = d.get("pm10Grade").getAsInteger();

            //     int pm25 = d.get("pm25Value").getAsInteger();
            //     int pm25Grade = d.get("pm25Grade").getAsInteger();
            //     data.append(stationName).append(",")
            //     .append(pm10).append(",")
            //     .append(pm10Grade).append(",")
            //     .append(pm25).append(",")
            //     .append(pm25Grade);
            //     data.toString()
            //     dustData.add(data);
            // }
        %>
        <h1> 검색 결과 </h1>

        지역 선택:
        <select id="stationSelect" onchange="filterStation()">
            <option value="all">전체</option>
            <%
                if (dustList != null) {
                    Set<String> addedStations = new LinkedHashSet<>();
                    for (DustVO vo : dustList) {
                        String name = vo.getStationName();
                        if (name != null && !name.isEmpty() && addedStations.add(name)) {
            %>
            <option value="<%= name %>"><%= name %></option>
            <%
                        }
                    }
                }
            %>
        </select>
        <br><br>

        <table border="1" id="dustTable">
        <tr><th>지역명</th><th>pm10 값</th><th>pm10 등급</th><th>pm25 값</th><th>pm 25 등급</th></tr>
        <%
            if (dustList != null) {
                for (DustVO vo : dustList) {
        %>
        <tr class="data-row" data-station="<%= vo.getStationName() %>">
            <td><%= vo.getStationName() %></td>
            <td><%= vo.getPm10Value() %></td>
            <td><%= vo.getPm10Grade() %></td>
            <td><%= vo.getPm25Value() %></td>
            <td><%= vo.getPm25Grade() %></td>
        </tr>
        <%
                }
            }
        %>
        </table>

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
