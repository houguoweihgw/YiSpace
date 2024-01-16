package com.emdoor.yispace.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.utils.ImageUtils;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private final String  TAG = "PhotoAdapter";
    private List<Photo> photoList;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(Photo photo);
    }

    public PhotoAdapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        // 设置 ImageView 和 TextView 的内容
        // 这里需要根据你的数据模型和布局文件中的 ID 进行适配
        // 设置 ImageView 的内容为照片的二进制数据
        byte[] imageData = ImageUtils.convertToByteArray(photo.getFileContent()); // 假设getFileContent返回照片的二进制数据
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        holder.photoImage.setImageBitmap(bitmap);

        // 计算宽高比
        float aspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        // 根据宽高比调整 ImageView 的高度
        int width = ((Activity) holder.photoImage.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 2;
        int height = (int) (width / aspectRatio);
        ViewGroup.LayoutParams params = holder.photoImage.getLayoutParams();
        params.width = width;
        params.height = height;
        holder.photoImage.setLayoutParams(params);

        Log.d(TAG, "onBindViewHolder: "+photo.getTitle());
        holder.photoTitle.setText(photo.getTitle());

        // 为 ImageView 设置点击监听器
        // 设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 触发点击事件回调
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(photo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void updatePhotos(List<Photo> newPhotos) {
        photoList = newPhotos;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImage;
        TextView photoTitle;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.photo_image);
            photoTitle = itemView.findViewById(R.id.photo_title);
        }
    }
}
