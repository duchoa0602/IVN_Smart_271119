package com.ivn.ivnsmart.model;

public class Device {
    private int mPhoto;
    private String mDeviceName;
    private String mDeviceID;
    private String mRelayID;
    private String mTimerOff;
    private String mTimerOn;
    private String mTimerCycle;
    private String mContent;
    private boolean mStatus;

    public Device(){

    }

    public Device(int mPhoto, String mDeviceName, String mDeviceID, String mRelayID, String mTimerOff, String mTimerOn, String mTimerCycle, String mContent, boolean mStatus) {
        this.mPhoto = mPhoto;
        this.mDeviceName = mDeviceName;
        this.mDeviceID = mDeviceID;
        this.mRelayID = mRelayID;
        this.mTimerOff = mTimerOff;
        this.mTimerOn = mTimerOn;
        this.mTimerCycle = mTimerCycle;
        this.mContent = mContent;
        this.mStatus = mStatus;
    }

    public Device(int mPhoto, String mDeviceName, String mContent, boolean mStatus) {
        this.mPhoto = mPhoto;
        this.mDeviceName = mDeviceName;
        this.mContent = mContent;
        this.mStatus = mStatus;
    }

    public Device(String mDeviceID,int mPhoto, String mDeviceName, String mTimerOff, String mTimerOn, boolean mStatus) {
        this.mDeviceID = mDeviceID;
        this.mPhoto = mPhoto;
        this.mDeviceName = mDeviceName;
        this.mTimerOff = mTimerOff;
        this.mTimerOn = mTimerOn;
        this.mStatus = mStatus;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTimerCycle() {
        return mTimerCycle;
    }

    public void setmTimerCycle(String mTimerCycle) {
        this.mTimerCycle = mTimerCycle;
    }

    public void setmPhoto(int mPhoto) {
        this.mPhoto = mPhoto;
    }

    public void setmDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public void setmDeviceID(String mDeviceID) {
        this.mDeviceID = mDeviceID;
    }

    public void setmRelayID(String mRelayID) {
        this.mRelayID = mRelayID;
    }

    public void setmTimerOff(String mTimerOff) {
        this.mTimerOff = mTimerOff;
    }

    public void setmTimerOn(String mTimerOn) {
        this.mTimerOn = mTimerOn;
    }

    public void setmStatus(boolean mStatus) {
        this.mStatus = mStatus;
    }

    public int getmPhoto() {
        return mPhoto;
    }

    public String getmDeviceName() {
        return mDeviceName;
    }

    public String getmDeviceID() {
        return mDeviceID;
    }

    public String getmRelayID() {
        return mRelayID;
    }

    public String getmTimerOff() {
        return mTimerOff;
    }

    public String getmTimerOn() {
        return mTimerOn;
    }

    public boolean ismStatus() {
        return mStatus;
    }
}
