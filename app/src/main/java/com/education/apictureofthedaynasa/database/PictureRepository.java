package com.education.apictureofthedaynasa.database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.education.apictureofthedaynasa.Picture;
import com.education.apictureofthedaynasa.networking.PictureAPIResponseListener;
import com.education.apictureofthedaynasa.networking.RetrofitProvider;

import java.util.List;

public class PictureRepository {
    private static final String TAG = "PictureRepository";
    private PictureDao mPictureDao;
    private LiveData<List<Picture>> mLiveDataPictureList;
    private PictureAPIResponseListener mListener;
    private Context mContext;

    public PictureRepository(Application application, PictureAPIResponseListener listener) {
        PictureRoomDatabase database = PictureRoomDatabase.getDatabaseInstance(application);
        mPictureDao = database.mPictureDao();
        mLiveDataPictureList = mPictureDao.getSortedPictureList();
        mListener = listener;
        mContext = application.getApplicationContext();
    }

    public LiveData<List<Picture>> getAllFavouritePictures() {
        return mLiveDataPictureList;
    }

    public void insert(Picture picture) {
        PictureRoomDatabase.mDatabaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: inserting to db");
                mPictureDao.insert(picture);
            }
        });
    }

    public void deleteItem(String date) {
        PictureRoomDatabase.mDatabaseWriteExecutor.execute(() -> {
            Log.d(TAG, "deleteItem: removing from db");
            mPictureDao.delete(date);
        });
    }

    public LiveData<Boolean> isItemExisting(String date) {
        return mPictureDao.isItemExisting(date);
    }

    public void getPicForTheDate(String date) {
        Log.d(TAG, "getPicForTheDate: date " + date);
        LiveData<Picture> pictureLiveData = mPictureDao.getPicForTheDate(date);
        Log.d(TAG, "getPicForTheDate: pictureLiveData " + pictureLiveData.getValue());
        if(pictureLiveData.getValue() == null) {
            getPictureOfTheDay(date);
        } else {
            Log.d(TAG, "getPicForTheDate: else " + pictureLiveData.getValue());
            mListener.onSuccess(pictureLiveData.getValue());
        }

//        return pictureLiveData;
    }

    public void getPictureOfTheDay(String date) {
        Log.d(TAG, "getPictureOfTheDay: date "+ date);

        RetrofitProvider provider = RetrofitProvider.getInstance(mListener, mContext);
        provider.makeAPIRequest(date);
    }
}
