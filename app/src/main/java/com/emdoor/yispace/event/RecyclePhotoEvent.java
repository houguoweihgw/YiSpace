package com.emdoor.yispace.event;

public class RecyclePhotoEvent {
    private int photoID;
    public RecyclePhotoEvent(int photoID) {
        this.photoID = photoID;
    }
    public int getPhotoID() {
        return photoID;
    }
    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }
}
