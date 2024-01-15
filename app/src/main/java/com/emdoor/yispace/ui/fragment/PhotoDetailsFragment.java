package com.emdoor.yispace.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.utils.ImageUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PhotoDetailsFragment extends Fragment {
    private Photo photo;
    private ImageView imageView;
    private RelativeLayout overlayLayout;
    private ImageView metaDataImageView;

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
        // 设置点击事件监听器
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换涂层的可见性
                toggleOverlayVisibility();
            }
        });

        metaDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
                View bottomView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout, null);
                TextView brandView = bottomView.findViewById(R.id.camera_brand);
                brandView.setText("相机品牌: "+photo.getMetadata().getMake());
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
            }
        });
        return view;
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