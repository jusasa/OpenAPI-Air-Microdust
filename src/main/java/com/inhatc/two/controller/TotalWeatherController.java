package com.inhatc.two.controller;

import java.io.IOException;
import java.util.List;

import com.inhatc.two.model.DustDAO;
import com.inhatc.two.model.DustVO;
import com.inhatc.two.model.WeatherDAO;
import com.inhatc.two.model.WeatherVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/search"})
public class TotalWeatherController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String serviceKey = request.getParameter("serviceKey");
        String returnType = request.getParameter("returnType");
        String numOfRows = "100";
        String sidoName = request.getParameter("sidoName");
        String ver = "1.0";

        DustDAO dustDAO = new DustDAO();
        List<DustVO> dustList = dustDAO.getDustList(serviceKey, returnType, numOfRows, ver, sidoName);

        WeatherDAO weatherDAO = new WeatherDAO();
        List<WeatherVO> weatherList = weatherDAO.getWeatherByLocation(serviceKey, sidoName);

        request.setAttribute("dustList", dustList);
        request.setAttribute("weatherList", weatherList);
        request.setAttribute("sidoName", sidoName);

        // 상세 카드(detail)에서 API를 다시 부르지 않도록 세션에 보관
        HttpSession session = request.getSession();
        session.setAttribute("dustList", dustList);
        session.setAttribute("weatherList", weatherList);
        session.setAttribute("sidoName", sidoName);
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }
}
