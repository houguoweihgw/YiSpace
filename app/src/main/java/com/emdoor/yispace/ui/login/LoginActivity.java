package com.emdoor.yispace.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emdoor.yispace.MainActivity;
import com.emdoor.yispace.R;
import com.emdoor.yispace.model.LoginResponse;
import com.emdoor.yispace.model.User;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";
    private Button loginButton;
    private EditText userText, passwordText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        userText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 登录逻辑
                String username = userText.getText().toString();
                String password = passwordText.getText().toString();
                Log.d(TAG, "onClick: " + username + " " + password);
                ApiService apiService = RetrofitClient.getApiService();
                User loginUser = new User(username, password);
                apiService.login(loginUser).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            Log.d(TAG, "onResponse: " + loginResponse.toString());
                            // 跳转到MainActivity，并传入user
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user", loginResponse.getUsername());
                            startActivity(intent);
                            finish();
                        } else {
                            // 登录失败后的处理逻辑
                            Log.d(TAG, "onResponse: " + response);
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // 网络请求失败后的处理逻辑
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(LoginActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}