package com.bichan.shop.Prefs;

import com.bichan.shop.models.DataLogin;
import com.bichan.shop.models.DataLoginSocial;
import com.bichan.shop.models.Social;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by cuong on 5/26/2017.
 */

public class PrefsUser{
    private static final String customerId_key = "customerId_key";
    private static final String firstname_key = "firstname_key";
    private static final String lastname_key = "lastname_key";
    private static final String email_key = "email_key";
    private static final String telephone_key = "telephone_key";
    private static final String date_added_key = "date_added_key";
    private static final String password_key = "password_key";
    private static final String google_key = "google_key";
    private static final String facebook_key = "facebook_key";

    private static final String cart_num_key = "cart_num_key";
    private static final String wish_num_key = "wish_num_key";

    public static void logout(){
        Prefs.remove(customerId_key);
        Prefs.remove(firstname_key);
        Prefs.remove(lastname_key);
        Prefs.remove(email_key);
        Prefs.remove(telephone_key);
        Prefs.remove(date_added_key);
        Prefs.remove(password_key);
        Prefs.remove(google_key);
        Prefs.remove(facebook_key);
        Prefs.remove(cart_num_key);
        Prefs.remove(wish_num_key);
    }


    public static int updateCartNum(boolean increase){
        int num = getCartNum();
        if(increase){
            setCartNum(++num);
        }else{
            setCartNum(--num);
        }
        return getCartNum();
    }

    public static int getWishNum(){
        return Prefs.getInt(wish_num_key, 0);
    }

    public static void setWishNum(int num){
        Prefs.putInt(wish_num_key, num);
    }

    public static int updateWishNum(boolean increase){
        int num = getWishNum();
        if(increase){
            setWishNum(++num);
        }else{
            setWishNum(--num);
        }
        return getWishNum();
    }

    public static int getCartNum(){
        return Prefs.getInt(cart_num_key, 0);
    }

    public static void setCartNum(int num){
        Prefs.putInt(cart_num_key, num);
    }

    public static boolean isLogin(){
        String password = getPassword();
        String email = getEmail();
        if(password.equals("") || email.equals(""))
            return false;
        return true;
    }

    public static boolean isLoginSocial(){
        String googleId = getGoogleId();
        String facebookId = getFacebookId();
        return !googleId.equals("") || !facebookId.equals("");
    }

    public static DataLoginSocial getSocial(){
        String googleId = getGoogleId();
        String facebookId = getFacebookId();
        DataLoginSocial dataLoginSocial = new DataLoginSocial();
        if(!googleId.equals("")){
            dataLoginSocial.setSocial(Social.GOOGLE);
            dataLoginSocial.setSocial_id(googleId);
            return  dataLoginSocial;
        }
        if(!facebookId.equals("")){
            dataLoginSocial.setSocial(Social.FACEBOOK);
            dataLoginSocial.setSocial_id(facebookId);
            return  dataLoginSocial;
        }
        return null;
    }

    public static DataLogin getDataLogin(){
        String email = getEmail();
        String password = getPassword();
        DataLogin dataLogin = new DataLogin();
        dataLogin.setPassword(password);
        dataLogin.setEmail(email);
        return dataLogin;
    }

    public static String getGoogleId(){
        return Prefs.getString(google_key, "");
    }

    public static String getFacebookId(){
        return Prefs.getString(facebook_key, "");
    }


    public static void setSocial(Social social, String id){
        switch (social){
            case FACEBOOK:
                Prefs.putString(facebook_key, id);
                break;
            case GOOGLE:
                Prefs.putString(google_key, id);
                break;
        }
    }

    public static String getPassword() {
        return Prefs.getString(password_key, "");
    }

    public static void setPassword(String password) {
        Prefs.putString(password_key, password);
    }

    public static String getCustomerId() {
        return Prefs.getString(customerId_key, "");
    }

    public static void setCustomerId(String customerId) {
        Prefs.putString(customerId_key, customerId);
    }

    public static String getFirstname() {
        return Prefs.getString(firstname_key, "");
    }

    public static void setFirstname(String firstname) {
        Prefs.putString(firstname_key, firstname);
    }

    public static String getLastname() {
        return Prefs.getString(lastname_key, "");
    }

    public static void setLastname(String lastname) {
        Prefs.putString(lastname_key, lastname);
    }

    public static String getEmail() {
        return Prefs.getString(email_key, "");
    }

    public static void setEmail(String email) {
        Prefs.putString(email_key, email);
    }

    public static String getTelephone() {
        return Prefs.getString(telephone_key, "");
    }

    public static void setTelephone(String telephone) {
        Prefs.putString(telephone_key, telephone);
    }

    public static String getDate_added() {
        return Prefs.getString(date_added_key, "");
    }

    public static void setDate_added(String date_added) {
        Prefs.putString(date_added_key, date_added);
    }
}

