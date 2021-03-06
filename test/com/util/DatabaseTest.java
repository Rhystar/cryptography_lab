package com.util;

import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {
    @Test
    public void getConnectionTest() {
        Connection RaspberryPi = Database.getConnection("172.17.27.26", "3306", "rhystar", "raspberry", "cryptography_lab");
        assertNotNull(RaspberryPi);
        Database.closeConnection(RaspberryPi);
    }
}
