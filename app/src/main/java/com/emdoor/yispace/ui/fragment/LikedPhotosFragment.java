package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.emdoor.yispace.event.LikedPhotoEvent;
import com.emdoor.yispace.event.RecyclePhotoEvent;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.response.LoginResponseSingleton;
import com.emdoor.yispace.response.PhotosResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.PhotoAdapter;
import com.emdoor.yispace.ui.adapter.PhotoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedPhotosFragment extends Fragment {
    private ApiService apiService;
    private final String TAG = "LikedPhotosFragment";
    private List<Photo> likedPhotoList = new ArrayList<>();
    private PhotoViewModel photoViewModel;
    private PhotoAdapter photoAdapter;
    private Toolbar toolbar;
    private FloatingActionButton upload_fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_photos, container, false);

        // 获取与Fragment关联的Activity
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        // 显示Toolbar
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().show();
        }

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("我的收藏");
        upload_fab = getActivity().findViewById(R.id.upload_button);
        upload_fab.setVisibility(View.VISIBLE);

        // 1.找到RecyclerView控件的引用
        RecyclerView recyclerView = view.findViewById(R.id.likedPhotosRecyclerView);
        // 2.加载照片数据

        // 初始化 ViewModel
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        // 尝试从 ViewModel 中获取数据
        List<Photo> cachedPhotoList = photoViewModel.getPhotoList();
        if (cachedPhotoList != null && !cachedPhotoList.isEmpty()) {
            // 使用缓存的数据
            likedPhotoList = cachedPhotoList;
            photoAdapter.updatePhotos(likedPhotoList);
        } else {
            // 没有缓存的数据，发起网络请求
            loadPhotos();
        }

        // 3.创建photoAdapter适配器
        photoAdapter = new PhotoAdapter(likedPhotoList);
        // 4.设置RecyclerView的布局管理器和适配器
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,  StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Photo photo,Boolean cancelSelect) {
                if (!cancelSelect) {
                    // 创建一个新的 Fragment 实例
                    PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment(false);
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
                else{

                }
            }
        });
        return view;
    }

    private void loadPhotos() {
        apiService = RetrofitClient.getApiService();
        // 查询照片
        apiService.collectedPhotos(LoginResponseSingleton.getInstance().getCurrentUser().getUsername()).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                if (response.isSuccessful()) {
                    PhotosResponse photosResponse = response.body();
                    if (photosResponse.getPhotos() != null) {
                        Toast.makeText(getContext(), photosResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: " + photosResponse.toString());
                        likedPhotoList = photosResponse.getPhotos();
                        // 更新适配器数据
                        if (photoAdapter != null) {
                            photoAdapter.updatePhotos(likedPhotoList);
                        }
                        photoViewModel.setPhotoList(likedPhotoList);
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }
            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPhotoRecycledEvent(LikedPhotoEvent likedPhotoEvent) {
        Log.d(TAG, "recycle photo id: "+likedPhotoEvent.getPhotoID());
        // 在你的照片列表中找到被删除的照片并移除
        Iterator<Photo> iterator = likedPhotoList.iterator();
        while (iterator.hasNext()) {
            Photo temp = iterator.next();
            Log.d(TAG, "current photo: "+ temp.getId());
            if (temp.getId() == likedPhotoEvent.getPhotoID()) {
                iterator.remove();
                break;
            }
        }
        // 通知适配器刷新
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}