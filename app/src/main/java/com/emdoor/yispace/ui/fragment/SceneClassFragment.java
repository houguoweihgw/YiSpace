package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.model.PhotosResponse;
import com.emdoor.yispace.model.Scene;
import com.emdoor.yispace.model.SceneResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.PhotoAdapter;
import com.emdoor.yispace.ui.adapter.PhotoViewModel;
import com.emdoor.yispace.ui.adapter.SceneAdapter;
import com.emdoor.yispace.ui.adapter.SceneViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SceneClassFragment extends Fragment {
    private final String  TAG = "SceneClassFragment";
    private ApiService apiService;

    private List<Scene> sceneList = new ArrayList<>();
    private SceneAdapter  sceneAdapter;
    private SceneViewModel sceneViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene_class, container, false);
        // 1.找到RecyclerView控件的引用
        RecyclerView recyclerView = view.findViewById(R.id.SceneClassRecyclerView);
        // 2.加载照片数据

        // 初始化 ViewModel
        sceneViewModel = new ViewModelProvider(this).get(SceneViewModel.class);
        // 尝试从 ViewModel 中获取数据
        List<Scene> cachedSceneList = sceneViewModel.getSceneList();
        if (cachedSceneList != null && !cachedSceneList.isEmpty()) {
            // 使用缓存的数据
            sceneList = cachedSceneList;
            sceneAdapter.updateScenes(sceneList);
        } else {
            // 没有缓存的数据，发起网络请求
            loadScenes();
        }
        // 3.创建photoAdapter适配器
        sceneAdapter = new SceneAdapter(sceneList);
        // 4.设置RecyclerView的布局管理器和适配器
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sceneAdapter);
        return  view;
    }

    private void loadScenes() {
        apiService = RetrofitClient.getApiService();
        // 查询照片
        apiService.sceneLabels("admin").enqueue(new Callback<SceneResponse>() {
            @Override
            public void onResponse(Call<SceneResponse> call, Response<SceneResponse> response) {
                if (response.isSuccessful()) {
                    SceneResponse sceneResponse = response.body();
                    Log.d(TAG, "onResponse: " + sceneResponse.toString());
                    sceneList = sceneResponse.getLabels();
                    // 更新适配器数据
                    if (sceneAdapter != null) {
                        sceneAdapter.updateScenes(sceneList);
                    }
                    sceneViewModel.setSceneList(sceneList);
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }
            @Override
            public void onFailure(Call<SceneResponse> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}