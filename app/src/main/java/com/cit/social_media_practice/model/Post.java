package com.cit.social_media_practice.model;

public class Post {

    String creator_id,post_id,post_txt,post_img;
    long date;

    public Post() {
    }

    public Post(String creator_id, String post_id, String post_txt, String post_img, long date) {
        this.creator_id = creator_id;
        this.post_id = post_id;
        this.post_txt = post_txt;
        this.post_img = post_img;
        this.date = date;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getPost_txt() {
        return post_txt;
    }

    public String getPost_img() {
        return post_img;
    }

    public long getDate() {
        return date;
    }
}
