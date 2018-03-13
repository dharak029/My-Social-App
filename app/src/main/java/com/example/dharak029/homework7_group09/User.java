/**
 * Assignment - InClass10
 * File Name - User.java
 * Dharak Shah,Viranchi Deshpande
 */
package com.example.dharak029.homework7_group09;

import java.io.Serializable;

/**
 * Created by dharak029 on 11/13/2017.
 */

public class User implements Serializable {

    String fname, lname, email, dob;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public User(String fname, String lname, String email, String dob) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.dob = dob;
    }

    public User(String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public User() {
    }
}