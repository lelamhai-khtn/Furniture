package com.example.furniture.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.furniture.fragments.CartFragment;
import com.example.furniture.fragments.HomeFragment;
import com.example.furniture.fragments.ProfileFragment;
import com.example.furniture.fragments.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behaviorResumeOnlyCurrentFragment) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
