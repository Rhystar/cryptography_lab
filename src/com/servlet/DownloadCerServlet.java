package com.servlet;

import com.dao.CertificateData;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/downloadCerServlet")
public class DownloadCerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serial_number = request.getParameter("serial_number");
        System.out.println(serial_number);
        CertificateData certificateDao = new CertificateData();
        String filePath = certificateDao.getCAPath(serial_number);
        if (filePath == null) {
            request.setAttribute("msg", "此证书不存在！");
            request.getRequestDispatcher("download_cer.jsp").forward(request, response);
            return;
        }
        String filename = serial_number + ".mycer";
        ServletContext servletContext = this.getServletContext();
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len;
        while ((len = fileInputStream.read(buff)) != -1) {
            servletOutputStream.write(buff, 0, len);
        }
        fileInputStream.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
