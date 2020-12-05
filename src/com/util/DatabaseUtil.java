package com.util;

import java.sql.*;

public class DatabaseUtil {
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

    public static boolean closeConnection(Connection DatabaseConnection) {
        if (DatabaseConnection != null) {
            try {
                DatabaseConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public int operateDatabase(Connection DatabaseConnection, String Operation) {
        int i = 0;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = DatabaseConnection.prepareStatement(Operation);
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
            resultSet = preparedStatement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
