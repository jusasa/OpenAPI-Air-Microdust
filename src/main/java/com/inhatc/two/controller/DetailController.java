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

@WebServlet({"/detail"})
public class DetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String station = request.getParameter("station");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("dustList") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<DustVO> dustList = (List<DustVO>) session.getAttribute("dustList");
        List<WeatherVO> weatherList = (List<WeatherVO>) session.getAttribute("weatherList");
        String sidoName = (String) session.getAttribute("sidoName");

        DustVO dust = null;
        if (dustList != null && station != null) {
            for (DustVO vo : dustList) {
                if (station.equals(vo.getStationName())) {
                    dust = vo;
                    break;
                }
            }
        }

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
