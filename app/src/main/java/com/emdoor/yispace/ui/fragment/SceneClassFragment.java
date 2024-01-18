package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Scene;
import com.emdoor.yispace.response.SceneResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.SceneAdapter;
import com.emdoor.yispace.ui.adapter.SceneViewModel;
import com.emdoor.yispace.utils.RequestType;

import java.io.Serializable;
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
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("场景分类");
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
        sceneAdapter.setOnItemClickListener(new SceneAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Scene scene) {
                // 创建一个新的 Fragment 实例
                AllPhotosFragment photoDetailsFragment = new AllPhotosFragment(RequestType.SCENE_PHOTO,scene.getLabel_name(),scene.getLabel_name());
                // 如果你需要传递数据给新的 Fragment，可以使用 setArguments 方法
                Bundle bundle = new Bundle();
                bundle.putSerializable("scene", (Serializable) scene);
                photoDetailsFragment.setArguments(bundle);

                // 使用 FragmentManager 加载并显示新的 Fragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, photoDetailsFragment) // R.id.fragment_container 是你放置 Fragment 的容器的布局 ID
                        .addToBackStack(null) // 将当前 Fragment 加入回退栈，以便返回时能回到前一个 Fragment
                        .commit();
            }
        });
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
                    Toast.makeText(getContext(), sceneResponse.getMessage(), Toast.LENGTH_SHORT).show();
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