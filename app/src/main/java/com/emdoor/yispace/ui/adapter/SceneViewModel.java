package com.emdoor.yispace.ui.adapter;

import androidx.lifecycle.ViewModel;

import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.model.Scene;

import java.util.List;

public class SceneViewModel extends ViewModel {
    private List<Scene> sceneList;

    public List<Scene> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<Scene> sceneList) {
        this.sceneList = sceneList;
    }
}
