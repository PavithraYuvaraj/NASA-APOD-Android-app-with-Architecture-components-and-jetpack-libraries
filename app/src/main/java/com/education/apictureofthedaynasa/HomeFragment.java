package com.education.apictureofthedaynasa;

import static com.education.apictureofthedaynasa.Constants.DATE_FORMAT;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.apictureofthedaynasa.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHomeBinding mBinding;
    private PictureViewModel mPictureViewModel;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    final Calendar mCalendar;

    public HomeFragment() {
        // Required empty public constructor
        mCalendar = Calendar.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPictureViewModel = new ViewModelProvider(this).get(PictureViewModel.class);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        mBinding.setPicViewModel(mPictureViewModel);
        mBinding.setLifecycleOwner(this);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Date date = new Date();
        getImageForTheDate(simpleDateFormat.format(date));

        pickDate();


        addToFavourites();

        removeFromFavourites();


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        return mBinding.getRoot();
    }

    private void initializeFavIcon() {
        mPictureViewModel.isItemExisting(mPictureViewModel.getPictureLiveData().getValue().getDate()).observe(getViewLifecycleOwner(), isPresent -> {
            if(isPresent) {
                mBinding.favIconIv.setVisibility(View.VISIBLE);
                mBinding.unFavIconIv.setVisibility(View.GONE);
            } else {
                mBinding.favIconIv.setVisibility(View.GONE);
                mBinding.unFavIconIv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void removeFromFavourites() {
        mBinding.favIconIv.setOnClickListener(v -> {
            /*mPictureViewModel.isItemExisting(mPictureViewModel.getPictureLiveData().getValue().getDate()).observe(getViewLifecycleOwner(), isPresent -> {
                Log.d(TAG, "removeFromFavourites: item presemt already.. removing from db  isPresent " + isPresent );

                if(isPresent) {
            */
            Log.d(TAG, "removeFromFavourites: item presemt already.. removing from db " );
                    mBinding.unFavIconIv.setVisibility(View.VISIBLE);
                    mBinding.favIconIv.setVisibility(View.GONE);
                    mPictureViewModel.delete(mPictureViewModel.getPictureLiveData().getValue().getDate());
             /*    }
           });*/
        });
    }

    private void addToFavourites() {
        mBinding.unFavIconIv.setOnClickListener(v -> {
        /*    mPictureViewModel.isItemExisting(mPictureViewModel.getPictureLiveData().getValue().getDate()).observe(getViewLifecycleOwner(), isPresent -> {
                Log.d(TAG, "addToFavourites: Item added to db isPresent " + isPresent);
                if(!isPresent) {*/
                    Log.d(TAG, "addToFavourites: Item added to db");
                    mBinding.favIconIv.setVisibility(View.VISIBLE);
                    mBinding.unFavIconIv.setVisibility(View.GONE);
                    mPictureViewModel.insert();
                /*}
            });*/
        });
    }

    /*
    private void addToFavourites() {
        mBinding.unFavIconIv.setOnClickListener(v -> {

                    Log.d(TAG, "addToFavourites: Item added to db");
                    mBinding.favIconIv.setVisibility(View.VISIBLE);
                    mBinding.unFavIconIv.setVisibility(View.GONE);
                    mPictureViewModel.insert();

});
        }

        */

    private void getImageForTheDate(String chosenDate) {
        mPictureViewModel.getPictureOfTheDay(chosenDate).observe(getViewLifecycleOwner(), picture -> {
//            mBinding.description.setText(picture.getExplanation());
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.loadImage(getContext(), picture.getHdUrl(), mBinding.imageView);

            initializeFavIcon();

        });
    }

    private void pickDate() {

        mDateSetListener = (datePicker, year, month, day) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            updatePickedDate();
        };

        mBinding.calendarIv.setOnClickListener(v -> {
            new DatePickerDialog(
                    getContext(),
                    mDateSetListener,
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void updatePickedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        String chosenDate = dateFormat.format(mCalendar.getTime());
        Log.d(TAG, "updatePickedDate: chosen date " + chosenDate);
        getImageForTheDate(chosenDate);
    }


}