package com.util;

import java.sql.Connection;

import static com.util.Database.getConnection;

public class labDatabase extends Database {
    public static Connection getLabConnection() {
        return getConnection("192.168.50.162", "3306", "rhystar", "raspberry", "cryptography_lab");
    }
}
