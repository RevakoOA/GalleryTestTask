package com.andersen.just_me.reditgallery;

import android.graphics.Point;
import android.util.Size;

import java.util.ArrayList;

/**
 * Created by just_me on 23.03.17.
 */

public class ImageData {
    public String title;
    public String author;
    public String thumbnail;
    public ArrayList<String> urls;
    public ArrayList<Point> sizes;

    public ImageData(String title, String author, String thumbnail,ArrayList<String> urls, ArrayList<Point> sizes) {
        this.title = title;
        this.author = author;
        this.thumbnail = thumbnail;
        this.urls = urls;
        this.sizes = sizes;
    }
}
