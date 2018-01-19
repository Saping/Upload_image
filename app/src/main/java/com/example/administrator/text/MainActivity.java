package com.example.administrator.text;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);

    }

    public void play(View view) {
        //添加动态权限     让你点击是否允许当前应用是否可以访问你的媒体资源时候

        //这段话 的意思   如果读的权限没有授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //就跳到让用户选择是否授权  给个码走返回方法
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        } else {
            //有权限时候 我要上传我的方法
            upload();
        }


    }

    //当我申请权限的时候,调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户选择了同意授权     走我的方法
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                upload();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied  权限拒绝", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void upload() {
        File file = new File(Environment.getExternalStorageDirectory(), "/aaa/yi.jpg");
        Log.d("TAG", file.getAbsolutePath() + "----------" + file.exists());
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", "4123");
        OkHttp3Util.uploadFile("https://www.zhaoapi.cn/file/upload", file, "long.jpg", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "----------" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("TAG", "----------");
                if (response.isSuccessful()) {
                    final String string = response.body().string();
                    Log.d("TAG", "----------" + string);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, string + "上传", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

//                OkHttp3Util.doGet("https://www.zhaoapi.cn/user/getUserInfo?uid=4123", new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            String string = response.body().string();
//
//                            Gson gson = new Gson();
//                            UserBean userBean = gson.fromJson(string, UserBean.class);
//
//                            Glide.with(MainActivity.this).load(userBean.getData().getIcon()).into(image);
//
//                        }
//                    }
//                });
            }
        });
    }
}
