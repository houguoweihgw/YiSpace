package com.emdoor.yispace.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.emdoor.yispace.R;
import com.emdoor.yispace.model.LoginResponseSingleton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        if (LoginResponseSingleton.getInstance().getCurrentUser() != null){
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

        // 启用左上角的菜单按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    // 处理菜单项点击事件
    private void handleNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
        int itemId = item.getItemId();
        if (itemId == R.id.menu_all_photos) {
            // 处理关于的逻辑
            Toast.makeText(this, "全部照片", Toast.LENGTH_SHORT).show();
            loadAllPhotosFragment(newFragmentTransaction);
        } else if (itemId == R.id.menu_scene_classification) {
            // 处理登出的逻辑
            Toast.makeText(this, "场景分类", Toast.LENGTH_SHORT).show();
            loadAllPhotosFragment(newFragmentTransaction);
        }else if (itemId == R.id.menu_person_classification) {
            // 处理登出的逻辑
            Toast.makeText(this, "人物分类", Toast.LENGTH_SHORT).show();
            loadAllPhotosFragment(newFragmentTransaction);
        }else if (itemId == R.id.menu_collection) {
            // 处理登出的逻辑
            Toast.makeText(this, "我的收藏", Toast.LENGTH_SHORT).show();
            loadAllPhotosFragment(newFragmentTransaction);
        }else if (itemId == R.id.menu_recycle) {
            // 处理登出的逻辑
            Toast.makeText(this, "回收站", Toast.LENGTH_SHORT).show();
            loadAllPhotosFragment(newFragmentTransaction);
        }else if (itemId == R.id.menu_about) {
            // 处理登出的逻辑
            Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
            loadAboutFragment(newFragmentTransaction);
        } else {
            // 处理其他菜单项的逻辑
        }
        // 关闭侧滑菜单
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void loadAllPhotosFragment(FragmentTransaction fragmentTransaction) {
        // 替换 fragment_container 中的内容为 AllPhotosFragment
        AllPhotosFragment newFragment = new AllPhotosFragment();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

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
