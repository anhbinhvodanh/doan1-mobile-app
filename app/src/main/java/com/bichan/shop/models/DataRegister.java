package com.bichan.shop.models;

import com.blankj.utilcode.util.EncryptUtils;

/**
 * Created by cuong on 5/24/2017.
 */

public class DataRegister {
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String social_id;
    private String network;
    private Social social;

    public DataRegister(){
        email = "";
        firstname = "";
        lastname = "";
        password = "";
        social_id = "";
        social = Social.NONE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return EncryptUtils.encryptMD5ToString(password).toLowerCase();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getNetwork() {
        switch (social){
            case FACEBOOK:
                network = "fb";
                break;
            case GOOGLE:
                network = "google";
                break;
            default:
                network = "";
        }
        return network;
    }

    public void setSocial(Social social) {
        this.social = social;
    }
}
