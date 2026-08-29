package com.example.furniture;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity {
    FrameLayout contentFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        contentFrame = findViewById(R.id.content_frame);
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_home)
            {
                loadFragment(new HomeFragment());
                return true;
            }
            if (id == R.id.bottom_search)
            {
                loadFragment(new SearchFragment());
                return true;
            }
            if (id == R.id.bottom_ar)
            {
                loadFragment(new ARFragment());
                return true;
            }
            if (id == R.id.bottom_cart)
            {
                loadFragment(new CartFragment());
                return true;
            }
            if (id == R.id.bottom_profile)
            {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame,fragment)
                .commit();

    }
}