package com.kira.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset-pw")
public class ResetPwC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (UserDAO.DAO.resetPassword(request)) {
            
            request.getSession().setAttribute("msg", "비밀번호 변경 성공");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.getRequestDispatcher("user/reset-pw.jsp").forward(request, response);
        }
    }
}