package com.education.apictureofthedaynasa.networking;

import com.education.apictureofthedaynasa.Picture;

public interface PictureAPIResponseListener {
    void onFailure();
    void onSuccess(Picture picture);
}
