package com.kira.pj.main;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;

public class DBManager {
    private static BasicDataSource dataSource;
    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@10.1.82.127:1521:XE");
        dataSource.setUsername("c##kira");
        dataSource.setPassword("kira1004");
        dataSource.setMinIdle(50);        // 최소 유휴 커넥션
        dataSource.setMaxIdle(100);        // 최대 유휴 커넥션
        dataSource.setMaxOpenPreparedStatements(200); // 풀에서 열린 최대 준비된 sql문 개수


    }
    public static Connection connect() throws SQLException {
        System.out.println("you?");
        return dataSource.getConnection();
    }
//    public static Connection connect() throws SQLException, ClassNotFoundException {
//        System.out.println("db connected..!");
//        String url = "jdbc:oracle:thin:@10.1.82.127:1521:XE";
//        Class.forName("oracle.jdbc.OracleDriver");
//        Connection con = DriverManager.getConnection(url,"c##tg1004", "tg1004");
//        System.out.println("db connected..!22");
//        return con;
//    }
    public static void close(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}