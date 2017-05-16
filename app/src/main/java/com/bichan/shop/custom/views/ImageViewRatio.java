package com.bichan.shop.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.bichan.shop.R;

/**
 * Created by cuong on 5/16/2017.
 */

public class ImageViewRatio extends android.support.v7.widget.AppCompatImageView{
    private float b;
    private float c;
    public ImageViewRatio(Context context) {
        super(context);
    }

    public ImageViewRatio(Context context, AttributeSet attrs) {
        super(context, attrs);
        a(attrs);
    }

    public ImageViewRatio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        a(attrs);
    }

    private void a(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ImageViewRatio);
        this.b = obtainStyledAttributes.getFloat(R.styleable.ImageViewRatio_ratioHeight, -1.0f);
        this.c = obtainStyledAttributes.getFloat(R.styleable.ImageViewRatio_ratioWidth, -1.0f);
    }
    @Override
    protected void onMeasure(int i, int i2) {
        if (this.b > 0.0f && this.c > 0.0f) {
            int size;
            int i3;
            size = MeasureSpec.getSize(i);
            i3 = (int) ((((float) size) / this.b) * this.c);
            i = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            i2 = MeasureSpec.makeMeasureSpec(i3, MeasureSpec.EXACTLY);
        }

        super.onMeasure(i, i2);
    }
}
