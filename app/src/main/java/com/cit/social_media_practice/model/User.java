package com.cit.social_media_practice.model;

public class User {

    String  user_name, user_email,user_password, user_cover, user_profile,user_id;


    public User() {
    }

    public User(String user_name, String user_email, String user_password, String user_cover, String user_profile, String user_id) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_cover = user_cover;
        this.user_profile = user_profile;
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_cover() {
        return user_cover;
    }

    public void setUser_cover(String user_cover) {
        this.user_cover = user_cover;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
