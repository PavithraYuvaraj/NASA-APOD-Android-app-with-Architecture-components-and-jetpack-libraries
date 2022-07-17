package com.education.apictureofthedaynasa.networking;

import static com.education.apictureofthedaynasa.Constants.API_KEY;
import static com.education.apictureofthedaynasa.Constants.API_KEY_VALUE;
import static com.education.apictureofthedaynasa.Constants.BASE_URL;
import static com.education.apictureofthedaynasa.Constants.DATE;
import static com.education.apictureofthedaynasa.Constants.EXPLANATION;
import static com.education.apictureofthedaynasa.Constants.HDURL;
import static com.education.apictureofthedaynasa.Constants.TITLE;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.education.apictureofthedaynasa.utils.NetworkUtils;
import com.education.apictureofthedaynasa.Picture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private static final String TAG = "RetrofitProvider";

    private PictureApiService mPictureApiService;
    private PictureAPIResponseListener mPictureAPIResponseListener;
    private Context mContext;
    private static RetrofitProvider mRetrofitProvider;
    private static OkHttpClient mOkHttpClient;

    public static RetrofitProvider getInstance(PictureAPIResponseListener listener, Context context) {
        if(mRetrofitProvider == null) {
            mRetrofitProvider = new RetrofitProvider(listener, context);
        }
        return mRetrofitProvider;
    }

    private RetrofitProvider(PictureAPIResponseListener listener, Context context) {
        mPictureAPIResponseListener = listener;
        mContext = context;
        if(mPictureApiService == null) {
            Log.d(TAG, "RetrofitProvider: inside private ");
            getPictureApiService();
        }
    }

    public void getPictureApiService() {

        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .cache(new Cache(mContext.getCacheDir(), 10 * 1204 * 1204))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if(NetworkUtils.isInternetAvailable(mContext)) {
                        request = request.newBuilder()
                                .header("Cache-Control", "public, max-age=" + 60).build();
                    } else {
                        request = request.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                    }
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
        mPictureApiService = retrofit.create(PictureApiService.class);
    }

    public Picture makeAPIRequest(String date) {
        Log.d(TAG, "makeAPIRequest: date " + date);
        Call<ResponseBody> call = mPictureApiService.getPictureOfTheDay(getQueryMap(date));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: call.request().url() " + call.request().url());
                if(response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        Picture picture = constructPictureFromResponse(responseString);
                        mPictureAPIResponseListener.onSuccess(picture);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "onResponse: response code " + response.code());
                    mPictureAPIResponseListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: onFailure " + t.getMessage());
                mPictureAPIResponseListener.onFailure();
            }
        });
        return null;
    }

    private Map<String, String> getQueryMap(String date) {
        Map<String, String> map = new HashMap<>();
        map.put(API_KEY, API_KEY_VALUE);
        map.put(DATE, date);
        map.put("thumbs", "true");
        return map;
    }

    private Picture constructPictureFromResponse(String response) {
        try {
            Log.d(TAG, "constructPictureFromResponse: " + response);
            Picture picture = new Picture();
            JSONObject responseJson = new JSONObject(response);
            if(responseJson.has(DATE)) {
                picture.setDate(responseJson.getString(DATE));
            }
            if (responseJson.has(TITLE)) {
                picture.setTitle(responseJson.getString(TITLE));
            }
            if (responseJson.has(EXPLANATION)) {
                picture.setExplanation(responseJson.getString(EXPLANATION));
            }
            if (responseJson.has(HDURL)) {
                picture.setHdUrl(responseJson.getString(HDURL));
            }
            return picture;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
