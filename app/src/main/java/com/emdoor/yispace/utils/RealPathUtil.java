package com.emdoor.yispace.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class RealPathUtil {

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            filePath = getRealPathFromURI_Q(context, uri);
        } else {
            filePath = getRealPathFromURI_BelowQ(context, uri);
        }
        return filePath;
    }

    @SuppressLint("NewApi")
    private static String getRealPathFromURI_Q(Context context, Uri uri) {
        String filePath = null;
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH);
                String relativePath = cursor.getString(columnIndex);
                cursor.close();

                filePath = Environment.DIRECTORY_DCIM + "/" + relativePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private static String getRealPathFromURI_BelowQ(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return filePath;
    }
}

