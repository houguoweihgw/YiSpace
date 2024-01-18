package com.emdoor.yispace.request;

public class RemovePhotoRequest {
    private int[] selectedPhotos;
    private String username;

    // Getters and setters
    public int[] getSelectedPhotos() {
        return selectedPhotos;
    }
    public void setSelectedPhotos(int[] SelectedPhotos) {
        this.selectedPhotos = SelectedPhotos;
    }
    public String getUsername() {
       return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "RemovePhotoRequest [selectedPhotos=" + selectedPhotos + ", username=" + username + "]";
    }
}
