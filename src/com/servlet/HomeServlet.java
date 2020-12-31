package com.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/homeServlet")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) { }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.doPost(request, response);
    }
}
