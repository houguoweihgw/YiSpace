package com.emdoor.yispace.model;

/**
 *  登录响应单例类
 *  创建一个包含用户信息的User类，将其实例存储在一个全局的单例对象中，然后在登录成功后将这个单例对象传递给主页面
 */
public class LoginResponseSingleton {
    private static LoginResponseSingleton instance;
    private LoginResponse currentUser;

    private LoginResponseSingleton() {
        // private constructor to prevent instantiation
    }

    public static LoginResponseSingleton getInstance() {
        if (instance == null) {
            instance = new LoginResponseSingleton();
        }
        return instance;
    }

    public LoginResponse getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginResponse currentUser) {
        this.currentUser = currentUser;
    }
}
