package com.example.konigguide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.konigguide.databinding.ActivityMainBinding;
import com.example.konigguide.R.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new TranslatorFragment());

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId()== id.translator){
                replaceFragment(new TranslatorFragment());
                findViewById(id.bottomNavView).setBackgroundResource(drawable.back_tab_first);
            }if (item.getItemId()== id.weather){
                replaceFragment(new WeatherFragment());
                findViewById(id.bottomNavView).setBackgroundResource(drawable.back_tab_second);
            }if (item.getItemId()== id.map){
                replaceFragment(new MapFragment());
                findViewById(id.bottomNavView).setBackgroundResource(drawable.back_tab_third);
            }if (item.getItemId()==id.profile){
                replaceFragment(new ProfileFragment());
                findViewById(id.bottomNavView).setBackgroundResource(drawable.back_tab_fourth);
            }
            return true;
        });


        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.first_fade_pall),
                        ContextCompat.getColor(this, R.color.second_fade_pall),
                        ContextCompat.getColor(this, R.color.third_fade_pall),
                        ContextCompat.getColor(this, R.color.fourth_fade_pall)});
        findViewById(R.id.backgroundMain).setBackground(gradientDrawable);

    }
    private void replaceFragment(Fragment frag){
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT =FM.beginTransaction();
        FT.replace(R.id.frameLayout,frag);
        FT.commit();
    }
}
