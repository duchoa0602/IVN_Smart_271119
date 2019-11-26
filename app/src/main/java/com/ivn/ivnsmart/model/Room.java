package com.ivn.ivnsmart.model;

public class Room {
    private String rName;
    private int rID;
    private String rSize;
    private String rImg;


    public Room() {
    }

    public Room(String rName, int rID, String rSize, String rImg ) {
        this.rName = rName;
        this.rID = rID;
        this.rSize = rSize;
        this.rImg = rImg;

    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public String getrSize() {
        return rSize;
    }

    public void setrSize(String rSize) {
        this.rSize = rSize;
    }

    public String getrImg() {
        return rImg;
    }

    public void setrImg(String rImg) {
        this.rImg = rImg;
    }

}
