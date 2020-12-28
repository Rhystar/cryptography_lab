package com.util;

import java.sql.Connection;

import static com.util.Database.getConnection;

public class labDatabase extends Database {
    public static Connection getLabConnection() {
        return getConnection("172.17.27.26", "3306", "rhystar", "raspberry", "cryptography_lab");
    }
}
