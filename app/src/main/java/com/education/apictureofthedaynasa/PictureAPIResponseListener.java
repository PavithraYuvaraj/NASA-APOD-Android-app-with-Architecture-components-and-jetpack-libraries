package com.education.apictureofthedaynasa;

public interface PictureAPIResponseListener {
    void onFailure();
    void onSuccess(Picture picture);
}
