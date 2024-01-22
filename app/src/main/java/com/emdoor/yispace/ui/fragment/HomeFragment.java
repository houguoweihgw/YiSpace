package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.response.PhotosResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private  static final String TAG = "HomeFragment";
    private ApiService apiService;
    private List<Photo> welcomePhotos = new ArrayList<>();
    private ViewPager2 viewPager2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("亿空间");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private void loadPhotos() {
        apiService = RetrofitClient.getApiService();
        // 查询照片
        apiService.welcomePhotos("admin").enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                if (response.isSuccessful()) {
                    PhotosResponse photosResponse = response.body();
                    Toast.makeText(getContext(), photosResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse welcome photo size：" + photosResponse.getPhotos().size());
                    welcomePhotos = photosResponse.getPhotos();
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

}