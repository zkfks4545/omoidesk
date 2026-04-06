package com.kira.pj.photo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PhotoC", value = "/photo-data")
public class PhotoC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // DB에서 가져온 데이터라고 가정 (객체 리스트)

//        String jsonResponse = "[" +
//                "{\"userId\":\"user1\", \"imgName\":\"pic1.jpg\", \"title\":\"남산타워에서\"}," +
//                "{\"userId\":\"user2\", \"imgName\":\"pic2.jpg\", \"title\":\"코딩 중 졸림\"}" +
//                "]";

        String jsonResponse = "[" +
                "{\"userId\":\"user1\", \"imgName\":\"pic1.jpg\", \"title\":\"남산타워에서\", \"regDate\":\"2024.03.25\", \"content\":\"날씨가 너무 좋아서 남산타워 다녀옴! 야경이 예술이다 진짜...\"}," +
                "{\"userId\":\"user2\", \"imgName\":\"pic2.jpg\", \"title\":\"코딩 중\", \"regDate\":\"2024.03.26\", \"content\":\"세미 프로젝트 마감 직전. 죽겠다... 살려줘... 💻☕\"}," +
                "{\"userId\":\"user1\", \"imgName\":\"pic3.jpg\", \"title\":\"맛있는 점심\", \"regDate\":\"2024.03.27\", \"content\":\"오늘 점심은 돈까스! 찍먹 vs 부먹 당신의 선택은?\"}" +
                "]";

        response.getWriter().write(jsonResponse);
//        request.getRequestDispatcher("photo/photo.jsp").forward(request, response);

    }

    public void destroy() {
    }
}