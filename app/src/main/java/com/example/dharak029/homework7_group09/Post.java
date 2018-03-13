package com.example.dharak029.homework7_group09;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dharak029 on 11/16/2017.
 */

public class Post implements Serializable{
    String name, post, uid;
    Date time;

    public Post(String name, String post, Date time,String uid) {
        this.name = name;
        this.post = post;
        this.time = time;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Post(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
