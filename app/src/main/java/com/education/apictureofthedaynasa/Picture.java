package com.education.apictureofthedaynasa;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "picture_table")
public class Picture {

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;
    @NonNull
    @ColumnInfo(name = "hdurl")
    private String mHdUrl;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;
    @NonNull
    @ColumnInfo(name = "explanation")
    private String mExplanation;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getHdUrl() {
        return mHdUrl;
    }

    public void setHdUrl(String mHdUrl) {
        this.mHdUrl = mHdUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getExplanation() {
        return mExplanation;
    }

    public void setExplanation(String mExplanation) {
        this.mExplanation = mExplanation;
    }
}
