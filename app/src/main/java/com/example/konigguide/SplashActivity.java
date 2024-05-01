package com.example.konigguide;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.first_fade_pall),
                        ContextCompat.getColor(this, R.color.second_fade_pall),
                        ContextCompat.getColor(this, R.color.third_fade_pall),
                        ContextCompat.getColor(this, R.color.fourth_fade_pall)});
        findViewById(R.id.backgroundSplash).setBackground(gradientDrawable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }
        },3500);
    }
}
