package com.emdoor.yispace.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.Photo;
import com.emdoor.yispace.request.UploadPhotoRequest;
import com.emdoor.yispace.response.LoginResponseSingleton;
import com.emdoor.yispace.response.RecycledPhotosResponse;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;
import com.emdoor.yispace.ui.fragment.AboutFragment;
import com.emdoor.yispace.ui.fragment.AllPhotosFragment;
import com.emdoor.yispace.ui.fragment.FaceClassFragment;
import com.emdoor.yispace.ui.fragment.HomeFragment;
import com.emdoor.yispace.ui.fragment.LikedPhotosFragment;
import com.emdoor.yispace.ui.fragment.RecyclePhotosFragment;
import com.emdoor.yispace.ui.fragment.SceneClassFragment;
import com.emdoor.yispace.utils.RealPathUtil;
import com.emdoor.yispace.utils.RequestType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import com.emdoor.yispace.response.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private androidx.appcompat.widget.SearchView searchView;
    private FloatingActionButton fab;
    private ApiService apiService;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 2; // 可以是任何非负整数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 加载首页fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
        loadHomeFragment(newFragmentTransaction);
        toolbar();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        searchView = findViewById(R.id.search_view);
        fab = findViewById(R.id.upload_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理按钮点击事件的代码
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 申请权限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    // 从相册选择照片（支持多选）
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // 允许多选
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            }
        });

        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                //搜索操作
                AllPhotosFragment photoDetailsFragment = new AllPhotosFragment(RequestType.SCENE_PHOTO, query, query);
                // 使用 FragmentManager 加载并显示新的 Fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, photoDetailsFragment) // R.id.fragment_container 是你放置 Fragment 的容器的布局 ID
                        .addToBackStack(null) // 将当前 Fragment 加入回退栈，以便返回时能回到前一个 Fragment
                        .commit();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                //todo:  搜索推荐
                return false;
            }
        });
        if (LoginResponseSingleton.getInstance().getCurrentUser() != null) {
            Log.d(TAG, "onCreate: " + LoginResponseSingleton.getInstance().getCurrentUser().toString());
            // 展示侧边栏的用户名
            String username = LoginResponseSingleton.getInstance().getCurrentUser().getUsername();
            // 获取 NavigationView 的 HeaderView
            View headerView = navigationView.getHeaderView(0);
            // 通过 HeaderView 找到 side_navi_user_textView
            TextView sideNaviUserTextView = headerView.findViewById(R.id.side_navi_user_textView);
            // 在这里你可以使用 sideNaviUserTextView 进行操作，例如设置文本
            sideNaviUserTextView.setText(username);
        }

        // 设置 NavigationView 的菜单项点击事件监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });
        // 设置 ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressLint("ResourceAsColor")
    public void toolbar() {
        toolbar = findViewById(R.id.toolbar);
        //标题
        toolbar.setTitle("亿空间");
        //左侧图标
        toolbar.setNavigationIcon(R.drawable.ic_launcher_background);
        //背景颜色
        toolbar.setBackgroundResource(R.color.primary);
        //设置toolbar对象
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    // 处理菜单项点击事件
    private void handleNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            // 处理关于的逻辑
            loadHomeFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_all_photos) {
            // 处理关于的逻辑
            loadAllPhotosFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_scene_classification) {
            // 处理登出的逻辑
            loadSceneClassFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_person_classification) {
            // 处理登出的逻辑
            loadFaceClassFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_collection) {
            // 处理登出的逻辑
            loadLikedPhotosFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_recycle) {
            // 处理登出的逻辑
            loadRecycledPhotosFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_about) {
            // 处理登出的逻辑
            loadAboutFragment(newFragmentTransaction);
        } else {
            // 处理其他菜单项的逻辑
        }
        // 关闭侧滑菜单
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    //  加载首页Fragment
    private void loadHomeFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        HomeFragment newFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //  加载全部照片Fragment
    private void loadAllPhotosFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        AllPhotosFragment newFragment = new AllPhotosFragment(RequestType.ALL_PHOTO, null, "全部照片");
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //  加载场景分类Fragment
    private void loadSceneClassFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        SceneClassFragment newFragment = new SceneClassFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //  加载人物分类Fragment
    private void loadFaceClassFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        FaceClassFragment newFragment = new FaceClassFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //  加载收藏照片Fragment
    private void loadLikedPhotosFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        LikedPhotosFragment newFragment = new LikedPhotosFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //  加载回收站Fragment
    private void loadRecycledPhotosFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        RecyclePhotosFragment newFragment = new RecyclePhotosFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //   加载关于Fragment
    private void loadAboutFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AboutFragment
        AboutFragment newFragment = new AboutFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理 ActionBarDrawerToggle 的点击事件
        if (item.getItemId() == android.R.id.home) {
            // 打开或关闭侧滑菜单
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                // 用户选择了多个照片
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    // 处理单个照片的逻辑
                    handleSelectedImage(uri);
                }
            } else {
                // 用户只选择了一个照片
                Uri uri = data.getData();
                // 处理单个照片的逻辑
                handleSelectedImage(uri);
            }
        }
    }

    private void handleSelectedImage(Uri uri) {
        String imagePath = null;
        Log.d(TAG, "uri=: " + uri);
        Log.d(TAG, "getUri`s Authority: " + uri.getAuthority());
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果document的类型是uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d(TAG, "docId=: " + docId);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                // 解析出数字格式的ID
                String id = docId.split(":")[1];
                Log.d(TAG, "id=: " + id);
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Log.d(TAG, "selection=: " + selection);
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                Log.d(TAG, "imagePath=: " + imagePath);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUIri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                Log.d(TAG, "contentUIri: " + contentUIri);
                imagePath = getImagePath(contentUIri, null);
                Log.d(TAG, "imagePath=: " + imagePath);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            // 在获取图像路径后，将其封装为MultipartBody.Part
            if (imagePath != null) {
                uploadPhoto("admin", imagePath);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
            // 在获取图像路径后，将其封装为MultipartBody.Part
            if (imagePath != null) {
                uploadPhoto("admin", imagePath);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
            // 在获取图像路径后，将其封装为MultipartBody.Part
            if (imagePath != null) {
                uploadPhoto("admin", imagePath);
            }
        }
    }


    private void uploadPhoto(String username, String imagePath) {
        apiService = RetrofitClient.getApiService();
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        // 将用户名作为 @Field 传递，以明确指定内容类型
        RequestBody usernameRequestBody = RequestBody.create(MediaType.parse("text/plain"), username);
        // 发起上传请求
        Call<Response> call = apiService.uploadPhoto(usernameRequestBody, filePart);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    Response photosResponse = response.body();
                    Toast.makeText(MainActivity.this, photosResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onResponse: " + photosResponse.getMessage());
                    // 处理成功的响应
                } else {
                    // 处理错误的响应
                    Log.d(TAG, "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                // 网络请求失败后的处理逻辑
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        String[] projection = {MediaStore.Images.Media._ID};
        // 通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                long imageId = cursor.getLong(columnIndex);
                // 通过imageId获取实际路径
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
                Cursor imageCursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                if (imageCursor != null) {
                    if (imageCursor.moveToFirst()) {
                        path = imageCursor.getString(imageCursor.getColumnIndex(filePathColumn[0]));
                    }
                    imageCursor.close();
                }
            }
            cursor.close();
        }
        Log.d(TAG, "getImagePath: " + path);
        return path;
    }
}
