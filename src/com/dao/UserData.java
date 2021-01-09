package com.dao;

import com.bean.*;
import com.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.UUID;

import static com.util.labDatabase.*;

public class UserData {
    public boolean register(User newUser) {
        String uuid = UUID.randomUUID().toString();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor(uuid, "sha-256");
        String Email = newUser.getEmail();
        String Password = passwordEncryptor.encode(newUser.getPassword());
        Connection DBConnection = getLabConnection();
        String sql = "INSERT INTO userdata (uuid, email, password) VALUES (" +
                "'" + uuid + "', " +
                "'" + Email + "', " +
                "'" + Password + "');";
        int flag = operateDatabase(DBConnection, sql);
        closeConnection(DBConnection);
        return flag == 1;
    }

    public boolean verify(User verifyUser) {
        String Email = verifyUser.getEmail();
        String rawPassword = verifyUser.getPassword();
        String encPassword;
        String salt;
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM userdata WHERE email='" + Email + "';";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            if (Result.next()) {
                encPassword = Result.getString("password");
                salt = Result.getString("uuid");
                PasswordEncryptor passwordEncryptor = new PasswordEncryptor(salt, "sha-256");
                return passwordEncryptor.isPasswordValid(encPassword, rawPassword);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(DBConnection);
            closeResultSet(Result);
        }
    }
}
