package com.webkul.mobikul.odoo.helper;

import androidx.databinding.BindingAdapter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by shubham.agarwal on 15/12/16. @Webkul Software Pvt. Ltd
 */

public class BindingAdapterUtils {

    @SuppressWarnings("unused")
    private static final String TAG = "BindingAdapterUtils";


    @BindingAdapter(value = {"imageUrl", "placeholder", "diskCacheStrategy", "skipMemoryCache", "imageType"}, requireAll = false)
    public static void setImageUrl(ImageView view, String imageUrl, Drawable placeholder, DiskCacheStrategy strategy, boolean skipMemoryCache, ImageHelper.ImageType imageType) {
        if (view == null) {
            return;
        }
        ImageHelper.load(view, imageUrl, placeholder, strategy, skipMemoryCache, imageType);
    }

    @BindingAdapter("srcCompat")
    public static void srcCompat(ImageButton view, Drawable icon) {
        if (view == null) {
            return;
        }
        view.setImageDrawable(icon);
    }


    @SuppressWarnings("unused")
    @BindingAdapter("imageDrawableId")
    public static void setImageByDrawableId(ImageView view, int drawableId) {
        view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), drawableId)); }

    @BindingAdapter("srcCompat")
    public static void setImageDrawable(ImageView view, int drawableId) {
        view.setImageResource(drawableId);
    }


//    @BindingAdapter("textErrorColor")
//    public static void setTextErrorColor(StripeEditText stripeEditText, int colorId) {
//        stripeEditText.setErrorColor(ContextCompat.getColor(stripeEditText.getContext(), colorId));
//    }


    @BindingAdapter("error")
    public static void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }


    @BindingAdapter("underline")
    public static void setUnderline(AppCompatTextView textView, boolean isUnderline) {
        if (isUnderline) {
            textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @BindingAdapter("htmlText")
    public static void setHtmlText(TextView textView, String htmlText) {
        if (htmlText == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            textView.setText(Html.fromHtml(htmlText));
        }
    }


    @BindingAdapter({"font"})
    public static void setFont(TextView textView, String fontPath) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), fontPath));
    }

    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter(value = {"visibility", "visibilityType"}, requireAll = false)
    public static void setVisibility(View view, boolean isVisible, int visibilityType) {
        view.setVisibility(isVisible ? View.VISIBLE : visibilityType == View.INVISIBLE ? View.INVISIBLE : View.GONE);
    }


    @BindingAdapter("layout_marginBottom")
    public static void setLayoutMarginBottom(ViewGroup view, float marginBottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.bottomMargin = (int) marginBottom;
    }

    @BindingAdapter("textStyle")
    public static void setTextStyle(TextView textView, String textStyle) {
        switch (textStyle) {
            case "bold":
                textView.setTypeface(null, Typeface.BOLD);
                break;
            default:
                textView.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    @BindingAdapter("minRating")
    public static void setMinimumRating(AppCompatRatingBar ratingBar, String minRating) {
        float MinimumRating = Float.parseFloat(minRating);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (rating < MinimumRating) {
                ratingBar1.setRating(MinimumRating);
            }
        });
    }

    /**
     * For directly passing HTML content in webview
     *
     * @param webView WebView on which content has to be loaded
     * @param content The HTML content to be loaded
     */
    @BindingAdapter({"loadUrl"})
    public static void loadUrl(WebView webView, String content) {
        String mime = "text/html";
        String encoding = "utf-8";
        webView.loadDataWithBaseURL("", content, mime, encoding, null);
    }


//    @InverseBindingAdapter(attribute = "ratingText")
//    public static void getRatingText(AppCompatTextView textView, int rating) {
//        textView.setText(ApplicationSingleton.getInstance().getRatingStatus().get(rating -1).get(1));
//    }

}