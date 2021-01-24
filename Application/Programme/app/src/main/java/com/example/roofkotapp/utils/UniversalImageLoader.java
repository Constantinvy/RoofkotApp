package com.example.roofkotapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.roofkotapp.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UniversalImageLoader {

    private static int default_image;   // On définit une image par défaut pour les joueurs et une pour les jeux
    private Context mContext;

    public UniversalImageLoader(Context context, int def_image){
        mContext = context;
        if (def_image == 1){
            default_image = R.drawable.pls;
        }else{
            default_image = R.drawable.jeux2;
        }
    }

    public ImageLoaderConfiguration getConfig(){
        // On configure l'image loader. Il faut le faire qu'une seule fois.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_image).showImageForEmptyUri(default_image)
                .showImageOnFail(default_image).cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true).imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions).memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024).build();

        return config;
    }

    public static void setImage(String imgURL, ImageView imageView, final ProgressBar mProgressbar, String mAppend){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mAppend + imgURL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (mProgressbar != null) {
                    mProgressbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (mProgressbar != null) {
                    mProgressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (mProgressbar != null){
                    mProgressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (mProgressbar != null){
                    mProgressbar.setVisibility(View.GONE);
                }
            }
        });
    }
}
