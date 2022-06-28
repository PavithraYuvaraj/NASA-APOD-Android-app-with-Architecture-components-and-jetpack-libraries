package com.education.apictureofthedaynasa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.education.apictureofthedaynasa.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        if(!NetworkUtils.isInternetAvailable(getApplicationContext())) {
            showInternetErrorDialog();
        }

        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mBinding.viewPager.setAdapter(customViewPagerAdapter);

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPager.setCurrentItem(tab.getPosition());
                Log.d(TAG, "onTabSelected: tab " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mBinding.tabLayout.selectTab(mBinding.tabLayout.getTabAt(position));
            }
        });
        setupViewPager(mBinding.viewPager);
    }

    private void setupViewPager(ViewPager2 mViewPager2) {
        CustomViewPagerAdapter customViewPagerAdapter
                = new CustomViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        Fragment fragment = new HomeFragment();
        Fragment fragment1 = new FavouritesFragment();
        customViewPagerAdapter.addFragment(fragment);
        customViewPagerAdapter.addFragment(fragment1);
        mViewPager2.setAdapter(customViewPagerAdapter);
    }

    private void showInternetErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.no_internet);
        builder.setMessage(R.string.no_internet_message);
        builder.setNegativeButton(R.string.ok, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}