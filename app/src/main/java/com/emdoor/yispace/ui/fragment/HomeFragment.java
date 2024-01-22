package com.emdoor.yispace.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.response.PhotosResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.HomePagerAdapter;
import com.emdoor.yispace.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private  static final String TAG = "HomeFragment";
    private ApiService apiService;
    private List<Photo> welcomePhotos = new ArrayList<>();
    private ViewPager homeVp;
    private LinearLayout pointLayout;
    //声明Viewpager的数据源
    private List<ImageView> ivList;
    //声明管理指示器小圆点集合
    private List<ImageView>pointList;

    //完成定时装置，实现自动滑动的效果
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            if (msg.what==1) {
                //获取当前viewpager显示的页面
                int currentItem= homeVp.getCurrentItem();
                //判断是否为最后一张，如果是，回到第一张
                if (currentItem==ivList.size()-1) {
                    homeVp.setCurrentItem(0);
                }else {
                    currentItem++;
                    homeVp.setCurrentItem(currentItem);
                }
                //形成循环
                handler.sendEmptyMessageDelayed(1,2500);
            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("亿空间");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        loadPhotos();
        initView(view);
        return view;
    }

    /*设置viewPager的监听器函数*/
    private void setVPListener() {
        homeVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pointList.size(); i++) {
                    pointList.get(i).setImageResource(R.drawable.shape_point_normal);
                }
                pointList.get(position).setImageResource(R.drawable.shape_point_selector);
            }
        });
    }

    /*设置ViewPager显示的页面*/
    private void initPager() {
        ivList=new ArrayList<>();
        pointList=new ArrayList<>();
        for (int i = 0; i < welcomePhotos.size() ; i++) {
            Photo  photo=welcomePhotos.get(i);
            byte[] imageData = ImageUtils.convertToByteArray(photo.getFileContent()); // 假设getFileContent返回照片的二进制数据
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            ImageView iv = new ImageView(getContext());
            iv.setImageBitmap(bitmap);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 使用CENTER_CROP 保持原始宽高比例
            //设置图片view的宽高
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);

            //将图片view加载到集合当中
            ivList.add(iv);
            //创建图片对应的指示器小圆点
            ImageView piv=new ImageView(getContext());
            piv.setImageResource(R.drawable.shape_point_normal);
            LinearLayout.LayoutParams plp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            plp.setMargins(20,0,0,0);
            piv.setLayoutParams(plp);
            //将小圆点加到布局当中
            pointLayout.addView(piv);
            //为了便于操作，将小圆点添加到统一管理的集合中
            pointList.add(piv);
        }
        //默认第一个小圆点是获取焦点的状态
        pointList.get(0).setImageResource(R.drawable.shape_point_selector);
        HomePagerAdapter HomePagerAdapter = new HomePagerAdapter(getContext(), ivList);
        homeVp.setAdapter(HomePagerAdapter);
    }

    /*初始化控件操作*/
    private void initView(View view) {
        homeVp=view.findViewById(R.id.homefrag_vp);
        pointLayout=view.findViewById(R.id.homefrag_layout);
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
                    initPager();
                    setVPListener();
                    //延迟五秒钟发送一条消息，通知可以切换viewpager的图片了
                    handler.sendEmptyMessageDelayed(1,5000);
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