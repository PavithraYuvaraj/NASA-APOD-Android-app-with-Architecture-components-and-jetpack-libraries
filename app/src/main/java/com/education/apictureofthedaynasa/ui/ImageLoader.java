package com.education.apictureofthedaynasa.ui;

import static com.education.apictureofthedaynasa.Constants.DOWNLOAD;
import static com.education.apictureofthedaynasa.Constants.WALLPAPER;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.education.apictureofthedaynasa.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@GlideModule
public class ImageLoader extends AppGlideModule {

    private static final String TAG = "ImageLoader";
    
    public void loadImage(Context context, String urlString, ImageView imageView) {

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(urlString)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable/*context.getDrawable(R.drawable.loading1)*/)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        showAlertDialog(R.string.load_failed_message, context, urlString, imageView);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
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

    public void downloadImage(Context context, String urlString, String date) {
//        Bitmap bitmap;
        Log.d(TAG, "downloadImage: ");
        Glide.with(context)
                .asBitmap()
                .load(urlString)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.d(TAG, "onResourceReady: ");
                        /*try {
                            WallpaperManager.getInstance(context).setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                        String savedImgPath = null;
                        String fileName = date + ".jpg";
                        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "NASA");

                        boolean status = true;
                        if(!storageDir.exists()) {
                            boolean mkdir = storageDir.mkdirs();
                            Log.d(TAG, "onResourceReady: mkdir " + mkdir);
                            status = mkdir;
                        }
                        Log.d(TAG, "onResourceReady: status " + status);
                        if(status) {

                            File file = new File(storageDir, fileName);
                            savedImgPath = file.getAbsolutePath();
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
                                fileOutputStream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            addImageToDeviceGallery(savedImgPath, context);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void addImageToDeviceGallery(String savedImgPath, Context context) {
        Log.d(TAG, "addImageToDeviceGallery:savedImgPath  " + savedImgPath);
        if(savedImgPath != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(savedImgPath);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
    }

    private void showAlertDialog(int message, Context context, String urlString, ImageView imageView) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
//        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), ((dialogInterface, i) -> {
            Log.d(TAG, "showAlertDialog: ok button pressed");
            loadImage(context, urlString, imageView);
            dialogInterface.dismiss();
        }));
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
