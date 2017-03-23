package com.andersen.just_me.reditgallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by just_me on 03.08.16.
 */
final class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GalleryAdapter";

    private final static int TYPE_FULL_IMAGE = 0;
    private final static int TYPE_DOUBLE_IMAGE = 1;
    private final Context context;
    private List<ImageData> imageDatas = new ArrayList<>();
    Picasso picasso;
    private int displayWidth;
    private int displayHalfWidth;

    public GalleryAdapter(Context context, List<ImageData> picturesData, int displayWidth) {
        this.context = context;
        this.displayWidth = displayWidth;
        displayHalfWidth = displayWidth/2;
        picasso = new Picasso.Builder(context).build();

        // Ensure we get a different ordering of images on each run.
        imageDatas = picturesData;
        Collections.shuffle(imageDatas);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_FULL_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_width_view, parent, false);
                holder = new ImageViewOne(view);
                break;
            case TYPE_DOUBLE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_images, parent, false);
                holder = new ImageViewTwo(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_FULL_IMAGE:
                ImageViewOne viewHolderOne = (ImageViewOne)holder;
                Picasso.with(context).load(imageDatas.get(position+position/2).thumbnail)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .fit()
                        .centerCrop()
                        .into(viewHolderOne.imageViewFW, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.with(context).load(getUrlOfBetterQuality(position+position/2, true))
                                        .placeholder(viewHolderOne.imageViewFW.getDrawable()).fit().centerCrop().into(viewHolderOne.imageViewFW);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                break;

            case TYPE_DOUBLE_IMAGE:
                ImageViewTwo viewHolderTwo = (ImageViewTwo)holder;
                Picasso.with(context).load(imageDatas.get(position+position/2).thumbnail).placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .fit()
                        .centerCrop()
                        .into(viewHolderTwo.imageLeft, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.with(context).load(getUrlOfBetterQuality(position+position/2, false))
                                        .placeholder(viewHolderTwo.imageLeft.getDrawable()).fit().centerCrop().into(viewHolderTwo.imageLeft);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                Picasso.with(context).load(imageDatas.get(position+position/2 + 1).thumbnail).placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .fit()
                        .centerCrop()
                        .into(viewHolderTwo.imageRight, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.with(context).load(getUrlOfBetterQuality(position+position/2 + 1, false))
                                        .placeholder(viewHolderTwo.imageRight.getDrawable()).fit().centerCrop().into(viewHolderTwo.imageRight);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                break;
        }
    }

    private String getUrlOfBetterQuality(int id, boolean fullWidth) {
        String urlOfBetterQuality = null;
        int appropriateWidth = (fullWidth)? displayWidth: displayHalfWidth;
        ImageData imageData = imageDatas.get(id);
        // All this url for some reasons are not valid, only original image is loading
        for (int i = 0; i < imageData.sizes.size()-1 && urlOfBetterQuality == null; i++) {
            if (imageData.sizes.get(i).x >= appropriateWidth) {
                urlOfBetterQuality = imageData.urls.get(i);
            }
        }
        if (urlOfBetterQuality == null) {
            urlOfBetterQuality = imageData.urls.get(imageData.urls.size()-1);
        }
        return urlOfBetterQuality;
    }

    @Override
    public int getItemCount() {
        return imageDatas.size()/2 + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ImageViewOne extends RecyclerView.ViewHolder {
        @Bind(R.id.imageFWIV)
        FullWidthImageView imageViewFW;
        public ImageViewOne(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public static class ImageViewTwo extends RecyclerView.ViewHolder {

        @Bind(R.id.imageLeftSIV)
        SquaredImageView imageLeft;
        @Bind(R.id.imageRightSIV)
        SquaredImageView imageRight;

        public ImageViewTwo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

