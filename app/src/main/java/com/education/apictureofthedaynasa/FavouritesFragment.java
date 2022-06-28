package com.education.apictureofthedaynasa;

import static com.education.apictureofthedaynasa.Constants.DATE_OF_FAV_PIC_TO_EXPAND;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.apictureofthedaynasa.databinding.FragmentFavouritesBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment implements RecyclerViewOnClickListener{

    private static final String TAG = "FavouritesFragment";

    private PictureViewModel mPictureViewModel;
    private ArrayList<Picture> mPictureList;
    private FragmentFavouritesBinding mBinding;

    private RecyclerView mRecyclerView;
    private FavouritesAdapter mFavouritesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPictureViewModel = new ViewModelProvider(this).get(PictureViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false);

        mRecyclerView = mBinding.recyclerView;
        if(mPictureList == null) {
            mPictureList = new ArrayList<>();
        }


        mFavouritesAdapter = new FavouritesAdapter(new FavouritesAdapter.PictureDiff(), getContext(), this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mFavouritesAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mPictureViewModel.getAllFavourites().observe(getViewLifecycleOwner(), pictures -> {
            Log.d(TAG, "onCreateView: size " + pictures.size());
            mFavouritesAdapter.submitList(pictures);
            mFavouritesAdapter.notifyDataSetChanged();
        });

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_favourites, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onItemClick(int position, View view) {
        int id = view.getId();
        switch (id) {
            case R.id.unfav:
                deleteItemFromDB(position);
                break;
           /* case R.id.share_image:
                shareImage(position, view);
                break;*/
            case R.id.use_image:
                showAlertDialog(
                        R.string.use_as_wallpaper,
                        R.string.use_as_wallpaper_msg,
                        R.string.use_as_wallpaper_positive_button,
                        R.string.use_as_wallpaper_negative_button,
                        position);
                break;
            case R.id.image_view_favs:
                launchExpandedActivity(position);
                break;
            default:
                break;

        }
    }

    private void launchExpandedActivity(int position) {
        Log.d(TAG, "launchExpandedActivity: date " + mFavouritesAdapter.getCurrentList().get(position).getDate());
        Intent intent = new Intent(getActivity(), ExpandableViewActivity.class);
        intent.putExtra(DATE_OF_FAV_PIC_TO_EXPAND, mFavouritesAdapter.getCurrentList().get(position).getDate());
        startActivity(intent);
    }

    private void deleteItemFromDB(int position) {

        Log.d(TAG, "deleteItemFromDB: " + mFavouritesAdapter.getCurrentList().get(position).getDate());

        //Log.d(TAG, "deleteItemFromDB: position " + mPictureList.get(position).getDate());
        mPictureViewModel.delete(mFavouritesAdapter.getCurrentList().get(position).getDate());
    }

    private void useImage(int position) {
        Log.d(TAG, "useImage: position " + position);

        String url = mFavouritesAdapter.getCurrentList().get(position).getHdUrl();
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.setImageAsWallpaper(getContext(), url);
    }

    private void shareImage(int position, View view) {

        Log.d(TAG, "shareImage: view " + view.getId());

        try {
//            Bitmap bitmap = view.getDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap( 200, 200, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
            view.draw(c);
            Log.d(TAG, "shareImage: bitmap " + bitmap);
            File cachePath = new File(getContext().getCacheDir(), "imageview");
            cachePath.mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(cachePath + "/"+mFavouritesAdapter.getCurrentList().get(position).getDate()+".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(getContext().getCacheDir(), "imageview");
        File newFile = new File(imagePath, mFavouritesAdapter.getCurrentList().get(position).getDate()+".png");
        Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".provider", newFile);
        if(contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, getContext().getContentResolver().getType(contentUri));
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }

        /*Log.d(TAG, "shareImage: position " + position);
        Bitmap bitmap = view.getDrawingCache();
        File root = getContext().getFilesDir();
        Log.d(TAG, "shareImage: root "  + root);
        File cachePath = new File(root.getAbsolutePath() + "/"+mFavouritesAdapter.getCurrentList().get(position).getDate()+".jpeg");
        try {
            cachePath.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                        getContext(),
                        getContext().
                                getApplicationContext().
                                getPackageName() + ".provider",
                        cachePath));
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share via"));*/
    }

    private void showAlertDialog(int title, int message, int positive, int negative, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negative, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.setPositiveButton(positive, ((dialogInterface, i) -> {
            useImage(position);
            dialogInterface.dismiss();
        }));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}