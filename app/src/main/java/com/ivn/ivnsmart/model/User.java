package com.ivn.ivnsmart.model;

public class User {
    private String user;
    private String pass;
    private String macID;

    public User(String user, String pass, String macID) {
        this.user = user;
        this.pass = pass;
        this.macID = macID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMacID() {
        return macID;
    }

    public void setMacID(String macID) {
        this.macID = macID;
    }
}
