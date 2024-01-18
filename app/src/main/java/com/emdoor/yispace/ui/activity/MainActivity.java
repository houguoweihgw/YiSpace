package com.emdoor.yispace.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.emdoor.yispace.R;
import com.emdoor.yispace.response.LoginResponseSingleton;
import com.emdoor.yispace.ui.fragment.AboutFragment;
import com.emdoor.yispace.ui.fragment.AllPhotosFragment;
import com.emdoor.yispace.ui.fragment.FaceClassFragment;
import com.emdoor.yispace.ui.fragment.LikedPhotosFragment;
import com.emdoor.yispace.ui.fragment.RecyclePhotosFragment;
import com.emdoor.yispace.ui.fragment.SceneClassFragment;
import com.emdoor.yispace.utils.RequestType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private androidx.appcompat.widget.SearchView searchView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        searchView = findViewById(R.id.search_view);
        fab = findViewById(R.id.upload_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理按钮点击事件的代码
            }
        });
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                //todo:  搜索操作
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: "+newText);
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
        if (itemId == R.id.menu_all_photos) {
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

    //  加载全部照片Fragment
    private void loadAllPhotosFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        AllPhotosFragment newFragment = new AllPhotosFragment(RequestType.ALL_PHOTO, null,"全部照片");
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
}
