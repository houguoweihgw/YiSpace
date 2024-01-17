package com.emdoor.yispace.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.request.DeletePhotoRequest;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.PhotoAdapter;
import com.emdoor.yispace.utils.DateUtils;
import com.emdoor.yispace.utils.ImageUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoDetailsFragment extends Fragment {
    private Photo photo;
    private ImageView imageView;
    private RelativeLayout overlayLayout;
    private ImageView metaDataImageView;
    private ImageButton isLikedImageView;
    private ImageButton deleteImageView;
    private ApiService apiService;
    private TextView nameTextView;
    private static final String TAG = "PhotoDetailsFragment";

    public static PhotoDetailsFragment newInstance() {
        return new PhotoDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo_details, container, false);
        imageView = view.findViewById(R.id.imageViewDetails);
        overlayLayout = view.findViewById(R.id.overlayLayout);
        nameTextView = view.findViewById(R.id.imageViewDetails_name);
        isLikedImageView = view.findViewById(R.id.isLiked);
        deleteImageView = view.findViewById(R.id.delete);
        metaDataImageView = view.findViewById(R.id.more);
        // 从 Bundle 中获取 Photo 对象
        if (getArguments() != null) {
            photo = (Photo) getArguments().getSerializable("photo");
            Log.d(TAG, "onCreateView: " + photo.toString());
            // 现在你可以使用 photo 对象进行操作
            byte[] imageData = ImageUtils.convertToByteArray(photo.getFileContent()); // 假设getFileContent返回照片的二进制数据
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
            nameTextView.setText(photo.getTitle());
        }

        if (photo.isCollected()){
            isLikedImageView.setImageResource(R.drawable.ic_like);
        }
        else {
            isLikedImageView.setImageResource(R.drawable.ic_nolike);
        }
        // 设置点击照片事件监听器
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换涂层的可见性
                toggleOverlayVisibility();
            }
        });
        //  设置点击点赞按钮事件监听器
        isLikedImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 在这里执行点击后的操作，例如切换照片源
                if (photo.isCollected()) {
                    // 如果已经点赞，取消点赞，切换为未点赞的照片
                    isLikedImageView.setImageResource(R.drawable.ic_nolike);
                    togglePhotoCollected(photo);
                } else {
                    // 如果未点赞，执行点赞操作，切换为点赞的照片
                    isLikedImageView.setImageResource(R.drawable.ic_like);
                    togglePhotoCollected(photo);
                }
                // 切换点赞状态
                photo.setCollected(!photo.isCollected());
            }
        });
        // 设置点击详情按钮事件监听器
        metaDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
                View bottomView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout, null);
                Log.d(TAG, "onClick: "+photo.getMetadata().toString());
                // 设置照片存储大小
                TextView sizeView = bottomView.findViewById(R.id.photo_size);
                sizeView.setText("照片大小: "+DateUtils.kbToMb(photo.getMetadata().getFileSize())+"MB"+" ("+photo.getMetadata().getFileSize()+"字节)");
                // 设置照片尺寸
                TextView resolutionView = bottomView.findViewById(R.id.photo_dimension);
                resolutionView.setText("照片尺寸: "+photo.getMetadata().getImageLength()+" x "+photo.getMetadata().getImageWidth());
                // 设置相机品牌
                TextView brandView = bottomView.findViewById(R.id.camera_brand);
                brandView.setText("相机品牌: "+photo.getMetadata().getMake());
                // 设置相机型号
                TextView modelView = bottomView.findViewById(R.id.camera_model);
                modelView.setText("相机型号: "+photo.getMetadata().getModel());
                // 设置照片拍摄时间
                TextView dateView = bottomView.findViewById(R.id.taken_date);
                dateView.setText("照片拍摄时间: "+ DateUtils.formatDateTime(photo.getMetadata().getDateTaken()) );
                // 设置曝光时间
                TextView exposureView = bottomView.findViewById(R.id.camera_exposure_time);
                exposureView.setText("曝光时间: "+photo.getMetadata().getExposureTime());
                // 设置光圈值
                TextView apertureView = bottomView.findViewById(R.id.camera_aperture);
                apertureView.setText("光圈值: f/"+photo.getMetadata().getAperture());
                // 设置ISO感光度
                TextView isoView = bottomView.findViewById(R.id.camera_iso);
                isoView.setText("ISO感光度: "+photo.getMetadata().getIso());
                // 设置焦距
                TextView focalLengthView = bottomView.findViewById(R.id.camera_focal_length);
                focalLengthView.setText("焦距: "+photo.getMetadata().getFocalLength()+"mm");
                // 设置拍摄地点
                TextView locationView = bottomView.findViewById(R.id.taken_coordinate);
                locationView.setText("地理坐标: "+DateUtils.getLatitude(photo.getMetadata().getLatitude()) +"/"+
                        DateUtils.getLongitude(photo.getMetadata().getLongitude()) +" ("+
                        DateUtils.getAltitude(photo.getMetadata().getAltitude())+")" );
                // 设置照片标签
                TextView labelView = bottomView.findViewById(R.id.photo_tag);
                labelView.setText("标签: "+photo.getMetadata().getSceneTags());
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
            }
        });
        // 设置点击删除按钮时间事件监听器
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto(photo);
            }
        });
        return view;
    }

    private void togglePhotoCollected(Photo photo) {
        apiService = RetrofitClient.getApiService();
        apiService.toggleCollected("admin", String.valueOf(photo.getId())).enqueue(new Callback<com.emdoor.yispace.response.Response>() {
            @Override
            public void onResponse(Call<com.emdoor.yispace.response.Response> call, Response<com.emdoor.yispace.response.Response> response) {
                if (response.isSuccessful()) {
                    com.emdoor.yispace.response.Response photosResponse = response.body();
                    Log.d(TAG, "onResponse: " + photosResponse.toString());
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }
            @Override
            public void onFailure(Call<com.emdoor.yispace.response.Response> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePhoto(Photo photo) {
        apiService = RetrofitClient.getApiService();
        DeletePhotoRequest request = new DeletePhotoRequest();
        request.setUsername("admin");
        int[] photoIDs = {photo.getId()};
        request.setPhotoIDs(photoIDs);
        apiService.deleteSelectedPhotos(request).enqueue(new Callback<com.emdoor.yispace.response.Response>() {
            @Override
            public void onResponse(Call<com.emdoor.yispace.response.Response> call, Response<com.emdoor.yispace.response.Response> response) {
                if (response.isSuccessful()) {
                    com.emdoor.yispace.response.Response photosResponse = response.body();
                    // 通过EventBus发送删除照片到AllPhotosFragment
                    EventBus.getDefault().post(photo);
                    Log.d(TAG, "onResponse: to be deleted"+photo.getId());
                    // 返回到上一个Fragment
                    getActivity().getSupportFragmentManager().popBackStack();
                    Log.d(TAG, "onResponse: " + photosResponse.toString());
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }
            @Override
            public void onFailure(Call<com.emdoor.yispace.response.Response> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void toggleOverlayVisibility() {
        if (overlayLayout.getVisibility() == View.VISIBLE) {
            // 如果涂层可见，则隐藏
            overlayLayout.setVisibility(View.GONE);
            //设置imageView背景为黑色
            imageView.setBackgroundColor(Color.BLACK);
        } else {
            // 如果涂层不可见，则显示
            overlayLayout.setVisibility(View.VISIBLE);
            //设置imageView背景为透明
            imageView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}