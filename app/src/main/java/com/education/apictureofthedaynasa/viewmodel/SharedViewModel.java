package com.education.apictureofthedaynasa.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.education.apictureofthedaynasa.Picture;

public class SharedViewModel extends ViewModel {

    private static final String TAG = "SharedViewModel";


    private final MutableLiveData<Picture> selectedItem = new MutableLiveData<>();

    public void select(Picture listItem){
        Log.d(TAG, "select: pic " + listItem.getDate());
        selectedItem.setValue(listItem);
    }

    public LiveData<Picture> getSelected(){
        return selectedItem;
    }

}
