package com.emdoor.yispace.event;

public class LikedPhotoEvent {
    private int photoID;
    public LikedPhotoEvent(int photoID) {
        this.photoID = photoID;
    }
    public int getPhotoID() {
        return photoID;
    }
    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }
}
