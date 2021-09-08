package com.webkul.mobikul.odoo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.webkul.mobikul.odoo.R;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class BadgeDrawable extends Drawable {

    @SuppressWarnings("unused")
    private static final String TAG = "BadgeDrawable";

    private final Paint mOuterBackgroundColor;
    private final Paint mInnerBackgroundColor;
    private final Paint mTextPaint;
    private final Rect mTxtRect = new Rect();

    private String mCount = "";
    private boolean mCanDrawBadge;

    @SuppressWarnings("WeakerAccess")
    public BadgeDrawable(Context context) {
        float mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);

        mOuterBackgroundColor = new Paint();
        mOuterBackgroundColor.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.badge_background_color));
        mOuterBackgroundColor.setAntiAlias(true);
        mOuterBackgroundColor.setStyle(Paint.Style.FILL);

        mInnerBackgroundColor = new Paint();
        mInnerBackgroundColor.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.grey_400));
        mInnerBackgroundColor.setAntiAlias(true);
        mInnerBackgroundColor.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (!mCanDrawBadge) {
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.
        /*Using Math.max rather than Math.min */

        float radius = ((Math.max(width, height) / 2)) / 2;
        float centerX = (width - radius - 1) + 5;
        float centerY = radius - 5;
        if (mCount.length() <= 2) {
            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, (int) (radius + 7.5), mInnerBackgroundColor);
            canvas.drawCircle(centerX, centerY, (int) (radius + 5.5), mOuterBackgroundColor);
        } else {
            canvas.drawCircle(centerX, centerY, (int) (radius + 8.5), mInnerBackgroundColor);
            canvas.drawCircle(centerX, centerY, (int) (radius + 6.5), mOuterBackgroundColor);
//	        	canvas.drawRoundRect(radius, radius, radius, radius, 10, 10, mOuterBackgroundColor);
        }
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        if (mCount.length() > 2) {
            canvas.drawText("99+", centerX, textY, mTextPaint);
        } else {
            canvas.drawText(mCount, centerX, textY, mTextPaint);
        }
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    @SuppressWarnings("WeakerAccess")
    public void setCount(String count) {
        mCount = count;
        mCanDrawBadge = !count.equalsIgnoreCase("0");
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}