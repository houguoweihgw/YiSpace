package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.emdoor.yispace.model.RecycledPhotosResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.PhotoAdapter;
import com.emdoor.yispace.ui.adapter.PhotoViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclePhotosFragment extends Fragment {
    private ApiService apiService;
    private final String TAG = "RecyclePhotosFragment";
    private List<Photo> recycledPhotoList = new ArrayList<>();
    private PhotoViewModel photoViewModel;
    private PhotoAdapter photoAdapter;

    public static RecyclePhotosFragment newInstance(String param1, String param2) {
        RecyclePhotosFragment fragment = new RecyclePhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_photos, container, false);
        // 1.找到RecyclerView控件的引用
        RecyclerView recyclerView = view.findViewById(R.id.recycledPhotosRecyclerView);
        // 2.加载照片数据

        // 初始化 ViewModel
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        // 尝试从 ViewModel 中获取数据
        List<Photo> cachedPhotoList = photoViewModel.getPhotoList();
        if (cachedPhotoList != null && !cachedPhotoList.isEmpty()) {
            // 使用缓存的数据
            recycledPhotoList = cachedPhotoList;
            photoAdapter.updatePhotos(recycledPhotoList);
        } else {
            // 没有缓存的数据，发起网络请求
            loadPhotos();
        }

        // 3.创建photoAdapter适配器
        photoAdapter = new PhotoAdapter(recycledPhotoList);
        // 4.设置RecyclerView的布局管理器和适配器
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Photo photo) {
                // 创建一个新的 Fragment 实例
                PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment();

                // 如果你需要传递数据给新的 Fragment，可以使用 setArguments 方法
                Bundle bundle = new Bundle();
                bundle.putSerializable("photo", (Serializable) photo);
                photoDetailsFragment.setArguments(bundle);

                // 使用 FragmentManager 加载并显示新的 Fragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, photoDetailsFragment) // R.id.fragment_container 是你放置 Fragment 的容器的布局 ID
                        .addToBackStack(null) // 将当前 Fragment 加入回退栈，以便返回时能回到前一个 Fragment
                        .commit();
            }
        });
        return view;
    }

    private void loadPhotos() {
        apiService = RetrofitClient.getApiService();
        // 查询照片
        apiService.recycledPhotos("admin").enqueue(new Callback<RecycledPhotosResponse>() {
            @Override
            public void onResponse(Call<RecycledPhotosResponse> call, Response<RecycledPhotosResponse> response) {
                if (response.isSuccessful()) {
                    RecycledPhotosResponse photosResponse = response.body();
                    Log.d(TAG, "onResponse: " + photosResponse.getPhotos().size());
                    recycledPhotoList = photosResponse.getPhotos();
                    // 更新适配器数据
                    if (photoAdapter != null) {
                        photoAdapter.updatePhotos(recycledPhotoList);
                    }
                    photoViewModel.setPhotoList(recycledPhotoList);
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }
            @Override
            public void onFailure(Call<RecycledPhotosResponse> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}