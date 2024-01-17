package com.emdoor.yispace.request;

public class DeletePhotoRequest {
    private int[] photoIDs;
    private String username;

    // Getters and setters
    public int[] getPhotoIDs() {
        return photoIDs;
    }
    public void setPhotoIDs(int[] photoIDs) {
        this.photoIDs = photoIDs;
    }
    public String getUsername() {
       return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "DeletePhotoRequest [photoIDs=" + photoIDs + ", username=" + username + "]";
    }
}
