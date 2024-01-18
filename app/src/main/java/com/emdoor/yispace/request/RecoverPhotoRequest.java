package com.emdoor.yispace.request;

public class RecoverPhotoRequest {
    private int[] selectedPhotos;
    private String username;

    // Getters and setters
    public int[] getSelectedPhotos() {
        return selectedPhotos;
    }
    public void setSelectedPhotos(int[] selectedPhotos) {
        this.selectedPhotos = selectedPhotos;
    }
    public String getUsername() {
       return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "DeletePhotoRequest [selectedPhotos=" + selectedPhotos + ", username=" + username + "]";
    }
}
