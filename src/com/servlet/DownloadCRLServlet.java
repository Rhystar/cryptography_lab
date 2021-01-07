package com.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/downloadCRLServlet")
public class DownloadCRLServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String crl_name = request.getParameter("filename");
        ServletContext servletContext = this.getServletContext();
        String file_path = servletContext.getRealPath("/download");
        String file_name = file_path + "/" + crl_name;
        String mimeType = servletContext.getMimeType(file_name);
        response.setHeader("content-type", mimeType);
        response.setHeader("content-disposition", "attachment;filename=" + "crl.xml");
        FileInputStream fileInputStream = new FileInputStream(new File(file_name));
        ServletOutputStream servletOutputStream = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len = 0;
        while ((len = fileInputStream.read(buff)) != -1) {
            servletOutputStream.write(buff, 0, len);
        }
        fileInputStream.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
