package com.util;

import java.sql.*;

public class DBConnect {
    String url;
    String username;
    String password;
    Connection con;
    ResultSet rs;
    PreparedStatement ps;

    public DBConnect() {
        try {
            url = "jdbc:mysql://172.17.27.26:3306/userdata";
            username = "rhystar";
            password = "raspberry";
            con = null;
            rs = null;
            ps = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int changeData(String sql) {
        int i = 0;
        try {
            ps = con.prepareStatement(sql);
            i = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public ResultSet selectSql(String sql) {
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void closeConnect() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("sql数据库关闭异常");
            e.printStackTrace();
        }
    }

}
