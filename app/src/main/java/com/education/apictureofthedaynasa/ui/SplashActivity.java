package com.education.apictureofthedaynasa.ui;

import static com.education.apictureofthedaynasa.Constants.DATE_FORMAT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.education.apictureofthedaynasa.R;
import com.education.apictureofthedaynasa.viewmodel.PictureViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchHomeActivity();
            }
        }, 3000);

    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}