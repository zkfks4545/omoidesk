package com.kira.pj.diary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = "/diary")
public class DiaryC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        String loginUserId = (String) session.getAttribute("loginUserId");

        // ★ 팀원분 index.js 규칙(host_id)과 성현님 규칙(memberId) 둘 다 체크
        String memberId = request.getParameter("memberId");
        String hostId = request.getParameter("host_id");

        // 둘 중 하나라도 있으면 그 값을 주인으로 인정
        String targetId = memberId;
        if (targetId == null || targetId.isEmpty()) {
            targetId = hostId;
        }

        // 여전히 없으면 내 다이어리
        if (targetId == null || targetId.isEmpty()) {
            targetId = loginUserId;
        }

        // JSP로 넘겨줄 때는 'ownerId'로 통일
        request.setAttribute("ownerId", targetId);

        DiaryDAO.DDAO.getCalendar(request);
        request.getRequestDispatcher("diary/diary.jsp").forward(request, response);
    }
}