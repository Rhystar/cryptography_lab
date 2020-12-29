package com.dao;

import com.bean.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDataTest {
    UserData dao = new UserData();

    @Test
    void registerTest() {
        assertTrue(dao.register(new User("registerTest", "2333")));
    }

    @Test
    void verifyTest() {
        User user1 = new User("verifyTest1", "2333");
        User user2 = new User("verifyTest2", "2333");
        User user3 = new User("verifyTest1", "wrong");
        dao.register(user1);
        assertTrue(dao.verify(user1));
        assertFalse(dao.verify(user2));
        assertFalse(dao.verify(user3));
    }
}
