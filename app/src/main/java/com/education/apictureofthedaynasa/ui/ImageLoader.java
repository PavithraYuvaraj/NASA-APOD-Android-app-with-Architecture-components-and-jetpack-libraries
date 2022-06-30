package com.education.apictureofthedaynasa.ui;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.education.apictureofthedaynasa.R;

import java.io.IOException;

@GlideModule
public class ImageLoader extends AppGlideModule {
    public void loadImage(Context context, String urlString, ImageView imageView) {
        Glide.with(context)
                .load(urlString)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(context.getDrawable(R.drawable.loading))
                .into(imageView);
    }

    public void setImageAsWallpaper(Context context, String urlString) {
        Bitmap bitmap;
        Glide.with(context)
                .asBitmap()
                .load(urlString)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            WallpaperManager.getInstance(context).setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
