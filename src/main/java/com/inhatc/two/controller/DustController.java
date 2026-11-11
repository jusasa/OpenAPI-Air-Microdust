package com.inhatc.two.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.inhatc.two.model.DustDAO;
import com.inhatc.two.model.DustVO;

@WebServlet("/search")
public class DustController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serviceKey = request.getParameter("serviceKey");
        String returnType = request.getParameter("returnType");
        String numOfRows = "100";
        String sidoName = request.getParameter("sidoName");
        String ver = "1.0";

        DustDAO dustDAO = new DustDAO();
        List<DustVO> dustList = dustDAO.getDustList(serviceKey, returnType, numOfRows, ver, sidoName);

        request.setAttribute("dustList", dustList);
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }

}
