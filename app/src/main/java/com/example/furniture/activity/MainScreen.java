package com.example.furniture.activity;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends BaseActivity {
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main_screen;
    }

    @Override
    protected void init() {
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

    @Override
    protected void setTitle() {
        // The title is rendered by the layout.
    }

    @Override
    protected void data() {
        // Main screen data is loaded by its fragments.
    }

    @Override
    protected void applyWindowInsets(View view, Insets systemBars) {
        view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
    }
}