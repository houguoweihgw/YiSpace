package com.emdoor.yispace.utils;

import android.util.Base64;

public class ImageUtils {
    public static String convertToBase64(byte[] imageData) {
        // 使用Base64类进行编码
        return Base64.encodeToString(imageData, Base64.DEFAULT);
    }

    public static byte[] convertToByteArray(String base64Data) {
        // 使用Base64类进行解码
        return Base64.decode(base64Data, Base64.DEFAULT);
    }
}
