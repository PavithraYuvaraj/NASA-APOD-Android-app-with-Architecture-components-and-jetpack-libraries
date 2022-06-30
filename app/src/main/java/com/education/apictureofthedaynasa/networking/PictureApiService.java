package com.education.apictureofthedaynasa.networking;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface PictureApiService {
    @GET("apod")
    Call<ResponseBody> getPictureOfTheDay(@QueryMap Map<String, String> queryMap);
}
