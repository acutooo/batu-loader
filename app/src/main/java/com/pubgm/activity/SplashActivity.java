package com.pubgm.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pubgm.Component.Utils;
import com.pubgm.R;
import com.pubgm.utils.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class SplashActivity extends ActivityCompat {

    public static boolean mahyong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView loadingImg = findViewById(R.id.loading);
        
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(loadingImg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mahyong = true;
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
