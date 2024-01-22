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
import android.widget.TextView;
import android.widget.Toast;

import com.emdoor.yispace.R;
import com.emdoor.yispace.event.RecyclePhotoEvent;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.request.DeletePhotoRequest;
import com.emdoor.yispace.request.RecoverPhotoRequest;
import com.emdoor.yispace.request.RemovePhotoRequest;
import com.emdoor.yispace.response.RecycledPhotosResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.adapter.PhotoAdapter;
import com.emdoor.yispace.ui.adapter.PhotoViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclePhotosFragment extends Fragment {
    private ApiService apiService;
    private final String TAG = "RecyclePhotosFragment";
    private List<Photo> recycledPhotoList = new ArrayList<>();
    private PhotoViewModel photoViewModel;
    private PhotoAdapter photoAdapter;
    private FloatingActionButton del_fab;
    private FloatingActionButton rec_fab;
    private Toolbar toolbar;
    private ArrayList<Integer> selectedPhotos = new ArrayList<>();

    public static RecyclePhotosFragment newInstance(String param1, String param2) {
        RecyclePhotosFragment fragment = new RecyclePhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("回收站");
        del_fab = getActivity().findViewById(R.id.delete_button);
        del_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹出确认对话框
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
                View bottomView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_confirm, null);
                TextView confirmView = bottomView.findViewById(R.id.confirm_delete);
                confirmView.setText("确认彻底删除所选照片");
                confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        removeSelectedPhotos();
                    }
                });

                TextView cancelView = bottomView.findViewById(R.id.cancel_delete);
                cancelView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
            }
        });

        // 恢复按钮
        rec_fab = getActivity().findViewById(R.id.recycle_button);
        rec_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  弹出确认对话框
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
                View bottomView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_confirm, null);
                TextView confirmView = bottomView.findViewById(R.id.confirm_delete);
                confirmView.setText("确认从回收站恢复所选照片");
                confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        recycleSelectedPhotos();
                    }
                });

                TextView cancelView = bottomView.findViewById(R.id.cancel_delete);
                cancelView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_photos, container, false);

        // 获取与Fragment关联的Activity
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        // 显示Toolbar
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().show();
        }

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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,  StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Photo photo,Boolean cancelSelect) {
                if (!cancelSelect) {
                    // 创建一个新的 Fragment 实例
                    PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment(true);

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
                else {
                    // 处理取消选择逻辑
                    photo.setSelected(false);
                    // 使用Stream找到目标ID的索引
                    int indexToRemove = IntStream.range(0, selectedPhotos.size())
                            .filter(i -> selectedPhotos.get(i) == photo.getId())
                            .findFirst()
                            .orElse(-1);
                    // 如果找到了目标ID，执行删除操作
                    if (indexToRemove != -1) {
                        selectedPhotos.remove(indexToRemove);
                    }
//                    Toast.makeText(getContext(), "已选择 " + selectedPhotos.size() + " 张照片", Toast.LENGTH_SHORT).show();
                    //  更新FAB状态
                    if (selectedPhotos.size() >0) {
                        String  title = selectedPhotos.size() + " 张照片已选择";
                        toolbar.setTitle(title);
                        del_fab.setVisibility(View.VISIBLE);
                        rec_fab.setVisibility(View.VISIBLE);
                    } else {
                        toolbar.setTitle("回收站");
                        del_fab.setVisibility(View.GONE);
                        rec_fab.setVisibility(View.GONE);
                    }
                }
            }
        });
        photoAdapter.setOnItemLongClickListener(new PhotoAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClick(Photo photo) {
                // 长按事件处理逻辑
                photo.setSelected(true);
                selectedPhotos.add(photo.getId());
//                Toast.makeText(getContext(), "已选择 " + selectedPhotos.size() + " 张照片", Toast.LENGTH_SHORT).show();
                //  更新FAB状态
                if (selectedPhotos.size() >0) {
                    String  title = selectedPhotos.size() + " 张照片已选择";
                    toolbar.setTitle(title);
                    del_fab.setVisibility(View.VISIBLE);
                    rec_fab.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setTitle("回收站");
                    del_fab.setVisibility(View.GONE);
                    rec_fab.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void removeSelectedPhotos() {
        apiService = RetrofitClient.getApiService();
        RemovePhotoRequest request = new RemovePhotoRequest();
        request.setUsername("admin");
        int selectedNumber = selectedPhotos.size();
        int[] photoIDs = selectedPhotos.stream().mapToInt(Integer::intValue).toArray();
        request.setSelectedPhotos(photoIDs);
        apiService.batchDeletePhotos(request).enqueue(new Callback<com.emdoor.yispace.response.Response>() {
            @Override
            public void onResponse(Call<com.emdoor.yispace.response.Response> call, Response<com.emdoor.yispace.response.Response> response) {
                if (response.isSuccessful()) {
                    com.emdoor.yispace.response.Response photosResponse = response.body();
                    Toast.makeText(getContext(), "成功清除 " + selectedNumber + " 张照片", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + photosResponse.toString());
                    // 刷新照片列表
                    for (int photoID : selectedPhotos) {
                        Iterator<Photo> iterator = recycledPhotoList.iterator();
                        while (iterator.hasNext()) {
                            Photo temp = iterator.next();
                            Log.d(TAG, "current photo: "+ temp.getId());
                            if (temp.getId() == photoID) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    // 通知适配器刷新
                    if (photoAdapter != null) {
                        photoAdapter.notifyDataSetChanged();
                    }
                    toolbar.setTitle("回收站");
                    selectedPhotos.clear();
                    del_fab.setVisibility(View.GONE);
                    rec_fab.setVisibility(View.GONE);
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

    private void recycleSelectedPhotos() {
        apiService = RetrofitClient.getApiService();
        RecoverPhotoRequest request = new RecoverPhotoRequest();
        request.setUsername("admin");
        int[] photoIDs = selectedPhotos.stream().mapToInt(Integer::intValue).toArray();
        request.setSelectedPhotos(photoIDs);
        apiService.recoverBatchPhotos(request).enqueue(new Callback<com.emdoor.yispace.response.Response>() {
            @Override
            public void onResponse(Call<com.emdoor.yispace.response.Response> call, Response<com.emdoor.yispace.response.Response> response) {
                if (response.isSuccessful()) {
                    com.emdoor.yispace.response.Response photosResponse = response.body();
                    Toast.makeText(getContext(), photosResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + photosResponse.toString());
                    // 刷新照片列表
                    for (int photoID : selectedPhotos) {
                        Iterator<Photo> iterator = recycledPhotoList.iterator();
                        while (iterator.hasNext()) {
                            Photo temp = iterator.next();
                            Log.d(TAG, "current photo: "+ temp.getId());
                            if (temp.getId() == photoID) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    // 通知适配器刷新
                    if (photoAdapter != null) {
                        photoAdapter.notifyDataSetChanged();
                    }
                    toolbar.setTitle("回收站");
                    selectedPhotos.clear();
                    rec_fab.setVisibility(View.GONE);
                    del_fab.setVisibility(View.GONE);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPhotoRecycledEvent(RecyclePhotoEvent recyclePhotoEvent) {
        Log.d(TAG, "recycle photo id: "+recyclePhotoEvent.getPhotoID());
        // 在你的照片列表中找到被删除的照片并移除
        Iterator<Photo> iterator = recycledPhotoList.iterator();
        while (iterator.hasNext()) {
            Photo temp = iterator.next();
            Log.d(TAG, "current photo: "+ temp.getId());
            if (temp.getId() == recyclePhotoEvent.getPhotoID()) {
                iterator.remove();
                break;
            }
        }
        // 通知适配器刷新
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
    }

    private void loadPhotos() {
        apiService = RetrofitClient.getApiService();
        // 查询照片
        apiService.recycledPhotos("admin").enqueue(new Callback<RecycledPhotosResponse>() {
            @Override
            public void onResponse(Call<RecycledPhotosResponse> call, Response<RecycledPhotosResponse> response) {
                if (response.isSuccessful()) {
                    RecycledPhotosResponse photosResponse = response.body();
                    Toast.makeText(getContext(), photosResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}