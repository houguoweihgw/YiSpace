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
import com.emdoor.yispace.model.Face;
import com.emdoor.yispace.model.Scene;
import com.emdoor.yispace.utils.ImageUtils;

import java.util.List;

public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.FaceViewHolder>{
    private  static final String TAG = "FaceAdapter";
    private List<Face> faceList;

    private  OnItemClickListener onItemClickListener;

    public FaceAdapter(List<Face> faceList) {
        this.faceList = faceList;
    }

    public interface OnItemClickListener {
        void onItemClick(Face face);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public FaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.face_item, parent, false);
        return new FaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaceViewHolder holder, int position) {
        Face face = faceList.get(position);
        // 设置 ImageView 和 TextView 的内容
        // 这里需要根据你的数据模型和布局文件中的 ID 进行适配
        // 设置 ImageView 的内容为照片的二进制数据
        byte[] imageData = ImageUtils.convertToByteArray(face.getFace_cover()); // 假设getFileContent返回照片的二进制数据
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        holder.faceCoverImageView.setImageBitmap(bitmap);

        // 计算宽高比
        float aspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        // 根据宽高比调整 ImageView 的高度
        int width = ((Activity) holder.faceCoverImageView.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 2;
        int height = (int) (width / aspectRatio);
        ViewGroup.LayoutParams params = holder.faceCoverImageView.getLayoutParams();
        params.width = width;
        params.height = height;
        holder.faceCoverImageView.setLayoutParams(params);

        Log.d(TAG, "onBindViewHolder: "+face.getFace_name());
        holder.faceTitleTextView.setText(face.getFace_name());
        holder.faceCountTextView.setText("共有 " + face.getFace_count()+" 张照片");

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 触发点击事件回调
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(face);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faceList.size();
    }

    public void updateScenes(List<Face> newFaces) {
        faceList = newFaces;
        notifyDataSetChanged();
    }


    static class  FaceViewHolder extends RecyclerView.ViewHolder {
        ImageView faceCoverImageView;
        TextView faceTitleTextView;
        TextView faceCountTextView;
        public FaceViewHolder(@NonNull View itemView) {
            super(itemView);
            faceCoverImageView = itemView.findViewById(R.id.face_cover);
            faceTitleTextView = itemView.findViewById(R.id.face_title);
            faceCountTextView = itemView.findViewById(R.id.face_count);
        }
    }
}
