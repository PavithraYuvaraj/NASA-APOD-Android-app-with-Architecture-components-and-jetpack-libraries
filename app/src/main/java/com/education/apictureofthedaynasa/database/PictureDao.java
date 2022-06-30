package com.education.apictureofthedaynasa.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.education.apictureofthedaynasa.Picture;

import java.util.List;

@Dao
public interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Picture picture);

    @Query("DELETE FROM picture_table WHERE date = :deleteDate")
    void delete(String deleteDate);

    @Query("SELECT * FROM picture_table ORDER BY date ASC")
    LiveData<List<Picture>> getSortedPictureList();

    @Query("DELETE FROM picture_table")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT * FROM picture_table WHERE date = :date)")
    LiveData<Boolean> isItemExisting(String date);

    @Query("SELECT * FROM picture_table WHERE date =:date")
    LiveData<Picture> getPicForTheDate(String date);
}
