package com.example.furniture.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.request.transition.Transition;
import com.example.furniture.R;
import com.example.furniture.adapters.ViewPagerAdapter;
import com.example.furniture.fragments.CartFragment;
import com.example.furniture.fragments.HomeFragment;
import com.example.furniture.fragments.LocationFragment;
import com.example.furniture.fragments.NotificationFragment;
import com.example.furniture.fragments.ProfileFragment;
import com.example.furniture.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity {
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_cart);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.bottom_home){
                    viewPager.setCurrentItem(0);
                }
                if(menuItem.getItemId() == R.id.bottom_search){
                    viewPager.setCurrentItem(1);
                }
                if(menuItem.getItemId() == R.id.bottom_cart){
                    viewPager.setCurrentItem(2);
                }
                if(menuItem.getItemId() == R.id.bottom_profile){
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}