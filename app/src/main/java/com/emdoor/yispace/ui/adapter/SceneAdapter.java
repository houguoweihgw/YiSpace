package com.emdoor.yispace.ui.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.emdoor.yispace.model.Scene;
import com.emdoor.yispace.utils.ImageUtils;

import java.util.List;

public class SceneAdapter extends RecyclerView.Adapter<SceneAdapter.SceneViewHolder> {
    private final String TAG = "SceneAdapter";
    private Context context;
    private List<Scene> sceneList;

    public SceneAdapter(List<Scene> sceneList) {
        this.sceneList = sceneList;
    }
    @NonNull
    @Override
    public SceneAdapter.SceneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scene_item, parent, false);
        return new SceneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SceneViewHolder holder, int position) {
        Scene scene = sceneList.get(position);
        // 设置 ImageView 和 TextView 的内容
        // 这里需要根据你的数据模型和布局文件中的 ID 进行适配
        // 设置 ImageView 的内容为照片的二进制数据
        byte[] imageData = ImageUtils.convertToByteArray(scene.getLabel_cover()); // 假设getFileContent返回照片的二进制数据
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        holder.sceneCoverImageView.setImageBitmap(bitmap);

//        // 计算宽高比
//        float aspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
//        // 获取当前ImageView的设定高度
//        int height =  300;
//        int width = (int) (height * aspectRatio);
//        Log.d(TAG, "onBindViewHolder: "+height+" "+aspectRatio+" "+width);
//        ViewGroup.LayoutParams params = holder.sceneCoverImageView.getLayoutParams();
//        params.width = width;
//        params.height = height;
//        holder.sceneCoverImageView.setLayoutParams(params);


        Log.d(TAG, "onBindViewHolder: "+scene.getLabel_name());
        holder.sceneTitleTextView.setText(scene.getLabel_name());
        holder.sceneCountTextView.setText("该场景共有: " + scene.getLabel_count()+" 张照片");
    }

    public void updateScenes(List<Scene> newScenes) {
        sceneList = newScenes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sceneList.size();
    }

    static class SceneViewHolder extends RecyclerView.ViewHolder {
        ImageView sceneCoverImageView;
        TextView sceneTitleTextView;
        TextView sceneCountTextView;

        public SceneViewHolder(@NonNull View itemView) {
            super(itemView);
            sceneCoverImageView = itemView.findViewById(R.id.scene_cover);
            sceneTitleTextView = itemView.findViewById(R.id.scene_title);
            sceneCountTextView = itemView.findViewById(R.id.scene_count);
        }
    }


}
