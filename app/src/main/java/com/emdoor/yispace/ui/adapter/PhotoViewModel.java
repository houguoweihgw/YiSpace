package com.emdoor.yispace.ui.adapter;

import androidx.lifecycle.ViewModel;

import com.emdoor.yispace.model.Photo;

import java.util.List;

public class PhotoViewModel extends ViewModel {
    private List<Photo> photoList;

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
