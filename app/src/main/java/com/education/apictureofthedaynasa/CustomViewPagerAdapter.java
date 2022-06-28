package com.education.apictureofthedaynasa;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomViewPagerAdapter extends FragmentStateAdapter {

    private static final String TAG = "CustomViewPagerAdapter";
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public CustomViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Log.d(TAG, "createFragment: position " + position);

        /*if(position == 0) {
            return new HomeFragment();
        }
        return new FavouritesFragment();*/
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
//        return 2;
    }
}
