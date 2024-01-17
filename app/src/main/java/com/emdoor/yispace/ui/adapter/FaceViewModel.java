package com.emdoor.yispace.ui.adapter;

import androidx.lifecycle.ViewModel;

import com.emdoor.yispace.model.Face;
import com.emdoor.yispace.model.Scene;

import java.util.List;

public class FaceViewModel extends ViewModel {
    private List<Face> faceList;

    public List<Face> getFaceList() {
        return faceList;
    }

    public void setFaceList(List<Face> faceList) {
        this.faceList = faceList;
    }
}
