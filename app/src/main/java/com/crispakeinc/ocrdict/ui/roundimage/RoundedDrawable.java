package com.crispakeinc.ocrdict.ui.roundimage;

import android.graphics.*;
import android.graphics.drawable.Drawable;

public class RoundedDrawable extends Drawable {
    private Paint mPaint;
    int color;
    float radius;
    private RectF mBounds = new RectF();


    public RoundedDrawable(int color, float radius) {
        mPaint = new Paint();
        this.color = color;
        this.radius = radius;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        RectF mRect = new RectF();
        mRect.left = 0f;
        mRect.top = 0f;
        mRect.right = mBounds.right;
        mRect.bottom = mBounds.bottom;
        canvas.drawRoundRect(mRect, radius, radius, mPaint);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int arg0)
    {
    }

    @Override
    public void setColorFilter(ColorFilter arg0)
    {}
}
