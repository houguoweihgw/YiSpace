package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

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
import com.emdoor.yispace.model.Face;
import com.emdoor.yispace.response.FaceResponse;
import com.emdoor.yispace.response.LoginResponseSingleton;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.FaceAdapter;
import com.emdoor.yispace.ui.adapter.FaceViewModel;
import com.emdoor.yispace.utils.RequestType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceClassFragment extends Fragment {
    private static  final String TAG = "FaceClassFragment";
    private ApiService apiService;
    private List<Face> faceList = new ArrayList<>();
    private FaceAdapter faceAdapter;
    private FaceViewModel faceViewModel;
    private Toolbar toolbar;
    private FloatingActionButton upload_fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face_class, container, false);
        // 1.找到RecyclerView控件的引用
        RecyclerView recyclerView = view.findViewById(R.id.faceClassRecyclerView);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("人物分类");
        upload_fab = getActivity().findViewById(R.id.upload_button);
        upload_fab.setVisibility(View.VISIBLE);
        // 2.加载照片数据
        // 初始化 ViewModel
        faceViewModel = new ViewModelProvider(this).get(FaceViewModel.class);
        // 尝试从 ViewModel 中获取数据
        List<Face> cachedFaceList = faceViewModel.getFaceList();
        if (cachedFaceList != null && !cachedFaceList.isEmpty()) {
            // 使用缓存的数据
            faceList = cachedFaceList;
            faceAdapter.updateScenes(faceList);
        } else {
            // 没有缓存的数据，发起网络请求
            loadFaces();
        }
        // 3.创建photoAdapter适配器
        faceAdapter = new FaceAdapter(faceList);
        // 4.设置RecyclerView的布局管理器和适配器
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(faceAdapter);

        faceAdapter.setOnItemClickListener(new FaceAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Face face) {
                // 创建一个新的 Fragment 实例
                AllPhotosFragment photoDetailsFragment = new AllPhotosFragment(RequestType.FACE_PHOTO,face.getFace_name(),face.getFace_name());
                // 如果你需要传递数据给新的 Fragment，可以使用 setArguments 方法
                Bundle bundle = new Bundle();
                bundle.putSerializable("face", (Serializable) face);
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


    private void loadFaces() {
        apiService = RetrofitClient.getApiService();
        // 查询照片
        apiService.faceClusters(LoginResponseSingleton.getInstance().getCurrentUser().getUsername()).enqueue(new Callback<FaceResponse>() {
            @Override
            public void onResponse(Call<FaceResponse> call, Response<FaceResponse> response) {
                if (response.isSuccessful()) {
                    FaceResponse facesResponse = response.body();
                    if (facesResponse.getFaces()!=null) {
                        Toast.makeText(getContext(), facesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: " + facesResponse.toString());
                        faceList = facesResponse.getFaces();
                        // 更新适配器数据
                        if (faceAdapter != null) {
                            faceAdapter.updateScenes(faceList);
                        }
                        faceViewModel.setFaceList(faceList);
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }
            @Override
            public void onFailure(Call<FaceResponse> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}