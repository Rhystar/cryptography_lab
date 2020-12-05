package com.bean;

public class UserData {
    private String Email;
    private String Password;
    private String IdentityCard;

    public UserData(String Email, String Password, String IdentityCard) {
        this.Email = Email;
        this.Password = Password;
        this.IdentityCard = IdentityCard;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setIdentityCard(String identityCard) {
        IdentityCard = identityCard;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getIdentityCard() {
        return IdentityCard;
    }
}
