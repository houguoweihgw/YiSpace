package com.emdoor.yispace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emdoor.yispace.R;
import com.emdoor.yispace.request.UserRegisterRequest;
import com.emdoor.yispace.response.LoginResponse;
import com.emdoor.yispace.response.LoginResponseSingleton;
import com.emdoor.yispace.service.ApiService;
import com.emdoor.yispace.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = RegisterActivity.class.getSimpleName();
    private Button registerButton;
    private EditText userText, passwordText, emailText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userText = findViewById(R.id.register_username);
        passwordText = findViewById(R.id.register_password);
        emailText = findViewById(R.id.register_email);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userText.getText().toString();
                String password = passwordText.getText().toString();
                String email = emailText.getText().toString();
                //确认不为空
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    UserRegisterRequest userRegisterRequest = new UserRegisterRequest(username, password, email);
                    ApiService apiService = RetrofitClient.getApiService();
                    apiService.register(userRegisterRequest).enqueue(new Callback<com.emdoor.yispace.response.Response>() {
                        @Override
                        public void onResponse(Call<com.emdoor.yispace.response.Response> call, Response<com.emdoor.yispace.response.Response> response) {
                            if (response.isSuccessful()) {
                                com.emdoor.yispace.response.Response registerResponse = response.body();
                                Log.d(TAG, "onResponse: " + registerResponse.toString());
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                // 跳转到LoginActivity
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 登录失败后的处理逻辑
                                Log.d(TAG, "onResponse: " + response);
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<com.emdoor.yispace.response.Response> call, Throwable t) {
                            // 网络请求失败后的处理逻辑
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(RegisterActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
