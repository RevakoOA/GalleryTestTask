package com.andersen.just_me.reditgallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by just_me on 03.08.16.
 */
final class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_FULL_IMAGE = 0;
    private final static int TYPE_DOUBLE_IMAGE = 1;
    private final Context context;
    private final List<String> urls = new ArrayList<>();
    Picasso picasso;

    public GalleryAdapter(Context context, String[] picturesURLs, final String userID) {
        this.context = context;

        picasso = new Picasso.Builder(context).build();

        // Ensure we get a different ordering of images on each run.
        Collections.addAll(urls, picturesURLs);
        Collections.shuffle(urls);
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
                Picasso.with(context).load(position+position/2)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .fit().into(viewHolderOne.imageViewFW);
                break;

            case TYPE_DOUBLE_IMAGE:
                ImageViewTwo viewHolderTwo = (ImageViewTwo)holder;
                Picasso.with(context).load(position+position/2).placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .fit()
                        .into(viewHolderTwo.imageLeft);
                Picasso.with(context).load(position+position/2 + 1).placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .fit()
                        .into(viewHolderTwo.imageRight);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return urls.size()/2 + 1;
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

