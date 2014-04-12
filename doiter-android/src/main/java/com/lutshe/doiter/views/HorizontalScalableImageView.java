package com.lutshe.doiter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Arsen Adzhiametov on 06-Apr-14 in IntelliJ IDEA.
 */
public class HorizontalScalableImageView extends ImageView {
    public HorizontalScalableImageView(Context context) {
        super(context);
    }

    public HorizontalScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScalableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = height * getDrawable().getIntrinsicWidth() / getDrawable().getIntrinsicHeight();
        setMeasuredDimension(width, height);
    }
}
