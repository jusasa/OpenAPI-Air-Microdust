package com.inhatc.two.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.inhatc.two.model.ApiException;
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

        List<DustVO> dustList = new ArrayList<>();
        String dustError = null;
        try {
            DustDAO dustDAO = new DustDAO();
            dustList = dustDAO.getDustList(serviceKey, returnType, numOfRows, ver, sidoName);
        } catch (ApiException e) {
            dustError = e.getMessage();
        } catch (Exception e) {
            dustError = "미세먼지 정보를 조회하는 중 오류가 발생했습니다: " + e.getMessage();
        }

        List<WeatherVO> weatherList = new ArrayList<>();
        String weatherError = null;
        try {
            WeatherDAO weatherDAO = new WeatherDAO();
            weatherList = weatherDAO.getWeatherByLocation(serviceKey, sidoName);
        } catch (ApiException e) {
            weatherError = e.getMessage();
        } catch (Exception e) {
            weatherError = "날씨 정보를 조회하는 중 오류가 발생했습니다: " + e.getMessage();
        }

        request.setAttribute("dustList", dustList);
        request.setAttribute("weatherList", weatherList);
        request.setAttribute("sidoName", sidoName);
        request.setAttribute("dustError", dustError);
        request.setAttribute("weatherError", weatherError);

        HttpSession session = request.getSession();
        session.setAttribute("dustList", dustList);
        session.setAttribute("weatherList", weatherList);
        session.setAttribute("sidoName", sidoName);

        request.getRequestDispatcher("search.jsp").forward(request, response);
    }
}
