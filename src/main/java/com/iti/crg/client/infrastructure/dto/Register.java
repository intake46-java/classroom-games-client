package com.iti.crg.client.infrastructure.dto;

public class Register {
    private String username;
    private String password;
    private String fname;
    private String lname;
    private int score;

    public Register() {
    }

    // Constructor for your current UI (Username & Password only)
    public Register(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;
        // Set defaults so the database doesn't complain
        this.fname = "New";
        this.lname = "Player";
    }

    // Constructor for future use (Full details)
    public Register(String username, String password, String fname, String lname) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
}
