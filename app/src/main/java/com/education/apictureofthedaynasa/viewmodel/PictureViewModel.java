package com.education.apictureofthedaynasa.viewmodel;

import static com.education.apictureofthedaynasa.Constants.DATE_FORMAT;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.education.apictureofthedaynasa.utils.NetworkUtils;
import com.education.apictureofthedaynasa.Picture;
import com.education.apictureofthedaynasa.networking.PictureAPIResponseListener;
import com.education.apictureofthedaynasa.database.PictureRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PictureViewModel extends AndroidViewModel implements PictureAPIResponseListener {

    private static final String TAG = "PictureViewModel";

    private PictureRepository mPictureRepository;

    private MutableLiveData<Picture> mPictureMutableLiveData;

    private LiveData<List<Picture>> mPictureList;

    private MutableLiveData<String> mTitle;

    private Context mContext;

    public PictureViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "PictureViewModel: constructor");
        mPictureRepository = new PictureRepository(application, this);
        mPictureList = mPictureRepository.getAllFavouritePictures();
        mContext = application.getApplicationContext();
        if(mPictureMutableLiveData == null) {
            mPictureMutableLiveData = new MutableLiveData<>();
            mTitle = new MutableLiveData<>();
        }
        //mCurrentFavouritePicture = new MutableLiveData<>();
    }

    public MutableLiveData<Picture> getPictureOfTheDay(String date) {
        Log.d(TAG, "getPictureOfTheDay: date "+ date);


       /* if(mPictureMutableLiveData == null) {
            mPictureMutableLiveData = new MutableLiveData<>();
            mTitle = new MutableLiveData<>();
        }
        RetrofitProvider provider = new RetrofitProvider(this);
        provider.makeAPIRequest(date);*/

        /*if(date == null) {
            if(mPictureMutableLiveData.getValue() != null) {
                date = mPictureMutableLiveData.getValue().getDate();
            }
        }*/
        mPictureRepository.getPicForTheDate(date);

        return mPictureMutableLiveData;
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "onFailure: failed");
        if(NetworkUtils.isInternetAvailable(mContext)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            mPictureRepository.getPictureOfTheDay(new SimpleDateFormat(DATE_FORMAT, Locale.US).format(calendar.getTime()));
        }
    }

    @Override
    public void onSuccess(Picture picture) {
        Log.d(TAG, "onSuccess: picture title " + picture.getTitle());
        mPictureMutableLiveData.postValue(picture);
        mTitle.postValue(picture.getTitle());
    }

    public MutableLiveData<String> getTitle() {
        return mTitle;
    }

    public MutableLiveData<Picture> getPictureLiveData() {
        return mPictureMutableLiveData;
    }

    public LiveData<List<Picture>> getAllFavourites() {
        return mPictureList;
    }

    public void insert() {
        Log.d(TAG, "insert: " + mPictureMutableLiveData.getValue().getTitle());
        mPictureRepository.insert(mPictureMutableLiveData.getValue());
    }

    public void delete(String date) {
        Log.d(TAG, "delete: removing from db");
        mPictureRepository.deleteItem(date);
    }

    public LiveData<Boolean> isItemExisting(String date) {
        return mPictureRepository.isItemExisting(date);
    }

}
