package com.andersen.just_me.reditgallery;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/** An image view which width/height = 2. */
public final class FullWidthImageView extends ImageView {
    public FullWidthImageView(Context context) {
        super(context);
    }

    public FullWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()/2);
    }
}

