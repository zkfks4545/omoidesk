package com.kira.pj.photo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PhotoC", value = "/photo")
public class PhotoC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PhotoDAO.test(request);
        request.setAttribute("content","photo/photo.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);


    }

    public void destroy() {
    }
}