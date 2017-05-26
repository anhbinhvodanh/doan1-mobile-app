package com.bichan.shop.models;

/**
 * Created by cuong on 5/25/2017.
 */

public class DataLogin {
    private String email;
    private String password;

    public DataLogin(){
        email = "";
        password = "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
