package com.kira.pj.photo;

import com.kira.pj.main.DBManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoDAO {
    public static void test(HttpServletRequest request) {
        try {
            Connection con = DBManager.connect();
            PreparedStatement ps = con.prepareStatement("select * from pictures");
            ResultSet rs = ps.executeQuery();
            rs.next();
            System.out.println(rs.getString("p_text"));
        } catch (Exception e) {
e.printStackTrace();
        }

    }
}
