package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;

import com.example.newsapp.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreenAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_animation);

        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Set the status bar color to white
            window.setStatusBarColor(getResources().getColor(R.color.white, getTheme()));

            // Set the status bar text color to dark (black)
            WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        } else {
            // For devices running Android 6.0 (API level 23) and above
            // Set the status bar color to white
            window.setStatusBarColor(getResources().getColor(R.color.white));

            // Set the status bar text color to dark (black)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenAnimation.this, MainActivity.class));
                finish();
            }
        },3000);


    }
}