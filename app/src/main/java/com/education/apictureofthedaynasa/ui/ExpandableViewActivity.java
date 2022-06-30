package com.education.apictureofthedaynasa.ui;

import static com.education.apictureofthedaynasa.Constants.DATE_OF_FAV_PIC_TO_EXPAND;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.education.apictureofthedaynasa.viewmodel.PictureViewModel;
import com.education.apictureofthedaynasa.R;
import com.education.apictureofthedaynasa.databinding.ActivityExpandableViewBinding;

public class ExpandableViewActivity extends AppCompatActivity {

    private static final String TAG = "ExpandableViewActivity";
    private ActivityExpandableViewBinding mBinding;
    private PictureViewModel mPictureViewModel;
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_expandable_view);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_expandable_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(DATE_OF_FAV_PIC_TO_EXPAND)) {
            mDate = bundle.getString(DATE_OF_FAV_PIC_TO_EXPAND);
            Log.d(TAG, "onCreate: date " + mDate);
        }

        mPictureViewModel = new ViewModelProvider(this).get(PictureViewModel.class);

        mPictureViewModel.getPictureOfTheDay(mDate).observe(this, picture -> {
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.loadImage(getApplicationContext(), picture.getHdUrl(), mBinding.expandedImageView);

            mBinding.dateTv.setText(picture.getDate());
            mBinding.descTv.setText(picture.getExplanation());
            mBinding.titleTv.setText(picture.getTitle());
        });
/*
        mBinding.toolbarView.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24, this.getTheme()));
        mBinding.toolbarView.getNavigationIcon().onC*/
        setSupportActionBar(mBinding.toolbarView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.toolbarView.setNavigationOnClickListener(v -> {
            finish();
        });

    }
}