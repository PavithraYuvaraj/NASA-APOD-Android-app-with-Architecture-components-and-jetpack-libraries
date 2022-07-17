package com.education.apictureofthedaynasa.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.apictureofthedaynasa.R;
import com.education.apictureofthedaynasa.databinding.FragmentExpandedViewBinding;
import com.education.apictureofthedaynasa.viewmodel.PictureViewModel;
import com.education.apictureofthedaynasa.viewmodel.SharedViewModel;

public class ExpandedViewFragment extends Fragment {

    private static final String TAG = "ExpandedViewFragment";

    private SharedViewModel mSharedViewModel;
    private FragmentExpandedViewBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_expanded_view, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expanded_view, container, false);
        Log.d(TAG, "onCreateView: expanded");

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mBinding.setSharedViewModel(mSharedViewModel);
        mBinding.setLifecycleOwner(this);

        mSharedViewModel.getSelected().observe(requireActivity(), picture -> {
            Log.d(TAG, "observe: picture " + picture.getDate());
            if(isAdded()) {
                ImageLoader imageLoader = new ImageLoader();
                imageLoader.loadImage(requireActivity(), picture.getHdUrl(), mBinding.expandedImageView);
            }
        });


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}