package com.util;

import java.sql.*;

public class Database {
    public static Connection getConnection(String IP, String Port, String Username, String Password, String Database) {
        Connection DatabaseConnection;
        String Url = "jdbc:mysql://" + IP + ":" + Port + "/" + Database;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            DatabaseConnection = DriverManager.getConnection(Url, Username, Password);
            return DatabaseConnection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection DatabaseConnection) {
        try {
            DatabaseConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int operateDatabase(Connection DatabaseConnection, String sql) {
        int i = 0;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = DatabaseConnection.prepareStatement(sql);
            i = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public ResultSet getResultSet(Connection DatabaseConnection, String sql) {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = DatabaseConnection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
