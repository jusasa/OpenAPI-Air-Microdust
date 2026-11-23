package com.inhatc.two.controller;

import java.io.IOException;
import java.util.List;

import com.inhatc.two.model.DustVO;
import com.inhatc.two.model.OutdoorActivity;
import com.inhatc.two.model.WeatherVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 검색 결과에서 지역명(측정소)을 클릭하면 한 카드 화면(detail.jsp)을 보여줌
@WebServlet({"/detail"})
public class DetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String station = request.getParameter("station");

        // search에서 세션에 저장해 둔 목록 꺼내기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("dustList") == null) {
            response.sendRedirect("index.jsp"); // 세션 만료 시 처음으로
            return;
        }
        List<DustVO> dustList = (List<DustVO>) session.getAttribute("dustList");
        List<WeatherVO> weatherList = (List<WeatherVO>) session.getAttribute("weatherList");
        String sidoName = (String) session.getAttribute("sidoName");

        // 클릭한 측정소의 미세먼지 찾기
        DustVO dust = null;
        for (DustVO vo : dustList) {
            if (vo.getStationName().equals(station)) {
                dust = vo;
                break;
            }
        }

        // 현재 시각과 가장 가까운 날씨 + 야외활동 판정
        WeatherVO weather = OutdoorActivity.findNearest(weatherList);
        String activity = OutdoorActivity.judge(dust, weather);

        request.setAttribute("sidoName", sidoName);
        request.setAttribute("station", station);
        request.setAttribute("dust", dust);
        request.setAttribute("weather", weather);
        request.setAttribute("activity", activity);
        request.getRequestDispatcher("detail.jsp").forward(request, response);
    }
}
