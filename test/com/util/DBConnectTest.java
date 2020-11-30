package com.util;

import org.junit.jupiter.api.Test;


class DBConnectTest {
    @Test
    public void ConnectTest(){
        var DB = new DBConnect();
        DB.init();
        DB.closeConnect();
    }
}