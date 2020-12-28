package com.bean;

public class User {
    private String Email;
    private String Password;

    public User(String Email, String Password) {
        this.Email = Email;
        this.Password = Password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

}
