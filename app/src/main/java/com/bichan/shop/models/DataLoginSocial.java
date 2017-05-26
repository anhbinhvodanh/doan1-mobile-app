package com.bichan.shop.models;

/**
 * Created by cuong on 5/25/2017.
 */

public class DataLoginSocial {
    private String social_id;
    private String network;
    private Social social;

    public DataLoginSocial(){
        social_id = "";
        network = "";
        social = Social.NONE;
    }

    public Social getSocial() {
        return social;
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
