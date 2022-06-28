package com.education.apictureofthedaynasa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class FavouritesAdapter extends ListAdapter<Picture, FavouritesAdapter.PictureViewHolder> {

    private static final String TAG = "FavouritesAdapter";
    private Context mContext;
    private RecyclerViewOnClickListener mClickListener;


//    private ArrayList<Picture> mPictureList;

    /*public FavouritesAdapter(Context context, ArrayList<Picture> pictureArrayList) {
        this.mContext = context;
        mPictureList = pictureArrayList;
    }*/

    public FavouritesAdapter(@NonNull DiffUtil.ItemCallback<Picture> diffCallback, Context context, RecyclerViewOnClickListener clickListener) {
        super(diffCallback);
        mContext = context;
        mClickListener = clickListener;
        Log.d(TAG, "FavouritesAdapter: construcot");
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_card_view_item_layout, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Picture picture = getItem(position);
        Log.d(TAG, "onBindViewHolder: picture.getHdUrl() "+ picture.getHdUrl());
        holder.bind(picture.getHdUrl(), mClickListener, position);
    }



    public class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageView;
        private ImageView mFavImageView;
//        private ImageView mShareImageView;
        private ImageView mUseImageImageView;
        private RecyclerViewOnClickListener mRecyclerViewOnClickListener;
        private int mPosition;


        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view_favs);
            mFavImageView = itemView.findViewById(R.id.unfav);
//            mShareImageView = itemView.findViewById(R.id.share_image);
            mUseImageImageView = itemView.findViewById(R.id.use_image);
            mImageView.setOnClickListener(this);
            mFavImageView.setOnClickListener(this);
//            mShareImageView.setOnClickListener(this);
            mUseImageImageView.setOnClickListener(this);
        }

        public void bind(String url, RecyclerViewOnClickListener recyclerViewOnClickListener, int position) {
            Log.d(TAG, "bind: url " + url);
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.loadImage(mContext, url, mImageView);
            mPosition = position;
            mRecyclerViewOnClickListener = recyclerViewOnClickListener;

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: view position " + mPosition);
            mRecyclerViewOnClickListener.onItemClick(mPosition, view);
        }
    }

    static class PictureDiff extends DiffUtil.ItemCallback<Picture> {

        @Override
        public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
            return false;
        }
    }
}
