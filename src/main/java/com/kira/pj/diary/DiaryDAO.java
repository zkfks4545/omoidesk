package com.kira.pj.diary;

import com.kira.pj.main.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class DiaryDAO {


    public static final DiaryDAO DDAO = new DiaryDAO();
    public Connection con = null;
    private DiaryDAO() {

    }
    private  ArrayList<DiaryDTO> diaries;

    public void getCalendar(HttpServletRequest req) {

        // 1. 현재 날짜 구하기
        Calendar cal = Calendar.getInstance();

        // 2. 사용자가 보낸 년도(y), 월(m) 파라미터 받기
        String y = req.getParameter("y");
        String m = req.getParameter("m");

        // 파라미터가 없으면(null) 현재 날짜의 년/월을 사용
        int year = (y == null) ? cal.get(Calendar.YEAR) : Integer.parseInt(y);
        int month = (m == null) ? cal.get(Calendar.MONTH) : Integer.parseInt(m) - 1;

        // 3. 계산을 위해 Calendar 객체 설정
        cal.set(year, month, 1);

        // 다시 정확한 년/월을 뽑음 (13월 입력 시 다음 해 1월로 자동 계산됨)
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH); // 0~11

        int startDay = cal.get(Calendar.DAY_OF_WEEK); // 1일의 요일 (1:일 ~ 7:토)
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 4. JSP로 보낼 데이터 바구니(request)에 담기
        req.setAttribute("startDay", startDay);
        req.setAttribute("lastDay", lastDay);
        req.setAttribute("curYear", curYear);
        req.setAttribute("curMonth", curMonth + 1);

        // [이전/다음 달 화살표 링크용 데이터]
        req.setAttribute("prevYear", (curMonth == 0) ? curYear - 1 : curYear);
        req.setAttribute("prevMonth", (curMonth == 0) ? 12 : curMonth);
        req.setAttribute("nextYear", (curMonth == 11) ? curYear + 1 : curYear);
        req.setAttribute("nextMonth", (curMonth == 11) ? 1 : curMonth + 2);

        // 5. 모드 제어 (d: 날짜 클릭, mode: 글쓰기)
        String d = req.getParameter("d");
        String mode = req.getParameter("mode");

        if ("write".equals(mode)) {
            req.setAttribute("showMode", "write"); // 글쓰기 모드
            req.setAttribute("selectedDay", d);
        } else if (d != null) {
            req.setAttribute("showMode", "list"); // 날짜별 일기 목록 모드
            req.setAttribute("selectedDay", d);

            // DB 대신 사용할 임시 데이터
            ArrayList<String> posts = new ArrayList<>();
            posts.add(curYear + "년 " + (curMonth+1) + "월 " + d + "일의 첫 기록");
            req.setAttribute("posts", posts);
        } else {
            req.setAttribute("showMode", "calendar"); // 그냥 기본 달력 모드

        }
    }

    // 전체조회
    public void selectAllDiary(HttpServletRequest req) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM diary_test ORDER BY d_date DESC";
        ArrayList<DiaryDTO> diaries = new ArrayList<>();
        try {
            con = DBManager.connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // 자바에서 날짜를 예쁘게 바꿔줄 도구 준비
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                DiaryDTO dto = new DiaryDTO();
                dto.setD_no(rs.getInt("d_no"));
                dto.setD_id(rs.getString("d_id"));

                // ★ 핵심: DB에서 원본 DATE를 꺼낸 다음, 자바가 문자열로 예쁘게 바꿈!
                java.sql.Date dbDate = rs.getDate("d_date");
                String formattedDate = sdf.format(dbDate);
                dto.setD_date(formattedDate); // DTO에는 String으로 쏙 들어감

                dto.setD_title(rs.getString("d_title"));
                dto.setD_txt(rs.getString("d_txt"));

                diaries.add(dto);
            }
            System.out.println(diaries);
            req.setAttribute("diaries", diaries);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(con, pstmt, rs);
        }
    }

        // 일기 등록 기능 (Create)
        public void insertDiary(HttpServletRequest req) {
            Connection con = null;
            PreparedStatement pstmt = null;
            String sql = "insert into diary_test values (diary_seq.nextval, ?, ?, ?, ?, SYSDATE)";

            try {
                con = DBManager.connection();
                pstmt = con.prepareStatement(sql);
                req.setCharacterEncoding("UTF-8");

                String no = req.getParameter("d_no");
                String id = req.getParameter("d_id");
                String date = req.getParameter("d_date");
                String title = req.getParameter("d_title");
                String txt = req.getParameter("d_txt");
                String createdAt = req.getParameter("d_created_at");

                // 3. 날짜 예쁘게 합치기 (예: 2026-4-6 -> 2026-04-06)
                // 월과 일이 한 자리수일 때 앞에 0을 붙여주면 나중에 DB에서 정렬하기 편해요!
                String formattedMonth = String.format("%02d", Integer.parseInt(id));
                String formattedDay = String.format("%02d", Integer.parseInt(date));
                String fullDate = no + "-" + formattedMonth + "-" + formattedDay;


                // 5. 빈칸(?)에 데이터 쏙쏙 채워 넣기
                pstmt.setString(1, id); // 나중에 로그인한 사람 아이디로 바꾸면 됩니다!
                pstmt.setString(2, fullDate);
                pstmt.setString(3, title);
                pstmt.setString(4, txt);

                // 6. DB에 실행 명령 내리기!
                if (pstmt.executeUpdate() == 1) {
                    System.out.println("일기 등록 성공! ദ്ദി(⩌ᴗ⩌ )");
                }

            } catch (Exception e) {
                System.out.println("일기 등록 실패 ㅠㅠ");
                e.printStackTrace();
            } finally {
                DBManager.close(con, pstmt, null);
            }
        }
    }










