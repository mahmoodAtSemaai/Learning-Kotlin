package com.webkul.mobikul.odoo.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by shubham.agarwal on 9/2/17. @Webkul Software Pvt. Ltd
 */


public class ImageHelper {

    @SuppressWarnings("unused")
    private static final String TAG = "ImageHelper";
//    public Context mContext;
//    public ImageHelper(Context context){
//        mContext = context;
//    }
    public static void load(@NonNull ImageView view, String imageUrl, int placeHolder, DiskCacheStrategy strategy, boolean skipMemoryCache, ImageType imageType) {
        load(view, imageUrl, ResourcesCompat.getDrawable(view.getResources(), placeHolder, null), strategy, skipMemoryCache, imageType);
    }

    public static void load(@NonNull ImageView view, String imageUrl, Drawable placeHolder, @Nullable DiskCacheStrategy strategy, boolean skipMemoryCache, ImageType imageType) {
        if (strategy == null) {
            strategy = DiskCacheStrategy.AUTOMATIC;
        }
        int width = 0;
        int height = 0;
        if (imageType != null) {
            switch (imageType) {
                case BANNER_SIZE_SMALL:
                    imageUrl += "/" + Helper.getScreenWidth() + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.banner_size_small));
                    width = Helper.getScreenWidth();
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.banner_size_small));
                    break;

                case BANNER_SIZE_GENERIC:
                    imageUrl += "/" + Helper.getScreenWidth() + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.banner_size_generic));
                    width = Helper.getScreenWidth();
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.banner_size_generic));
                    break;

                case BANNER_SIZE_LARGE:
                    imageUrl += "/" + Helper.getScreenWidth() + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.banner_size_large));
                    width = Helper.getScreenWidth();
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.banner_size_large));
                    break;


                case PRODUCT_TINY:
                    imageUrl += "/" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_tiny)) + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_tiny));
                    width = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_tiny));
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_tiny));
                    break;

                case PRODUCT_SMALL:
                    imageUrl += "/" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small)) + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small));
                    width = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small));
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small));
                    break;

                case PRODUCT_GENERIC:
                    imageUrl += "/" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_generic)) + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_generic));
                    Log.i(TAG, "load: "+imageUrl + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_generic)));
                    width = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_generic));
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_generic));
                    break;

                case PRODUCT_LARGE:
                    imageUrl += "/" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_large)) + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_large));
                    width = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_large));
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_large));
                    break;

                case PROFILE_PIC_SMALL:
                    imageUrl += "/" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small)) + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small));
                    width = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small));
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.product_image_small));
                    break;

                case PROFILE_PIC_GENERIC:
                    imageUrl += "/" + ((int) view.getContext().getResources().getDimension(R.dimen.profile_pic_generic)) + "x" + ((int) view.getContext().getResources().getDimension(R.dimen.profile_pic_generic));
                    width = ((int) view.getContext().getResources().getDimension(R.dimen.profile_pic_generic));
                    height = ((int) view.getContext().getResources().getDimension(R.dimen.profile_pic_generic));
                    break;
            }
        }

//      for using dontAnimate: http://stackoverflow.com/questions/36384789/glide-not-loading-real-image-and-stuck-with-placeholder
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(placeHolder)
                .override(height)
                .diskCacheStrategy(strategy)
                .skipMemoryCache(skipMemoryCache)
                .dontAnimate()
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//
//
//                })
                .into(view);


//        int finalWidth = width;
//        int finalHeight = height;
//        Glide.with(view.getContext()).asBitmap().
//                load(imageUrl)
//                .diskCacheStrategy(strategy)
//                .skipMemoryCache(skipMemoryCache)
//                .placeholder(placeHolder)
//                .dontAnimate()
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        // resource is your loaded Bitmap\
//                        if (imageType == null)
//                            view.setImageBitmap(Bitmap.createScaledBitmap(resource, finalWidth, finalHeight, false));
//                        else
//                            view.setImageBitmap(resource);
//
//                        return true;
//                    }
//                }).submit();
    }


    public static ArrayList<String> getUploadImageOptions(Context context) {
        ArrayList<String> alertDialogItems = new ArrayList<>();
        alertDialogItems.add(context.getString(R.string.add_image));
//        if (Helper.hasCamera(context)) {
//            alertDialogItems.add(context.getString(R.string.camera_pick));
//        }
        alertDialogItems.add(context.getString(R.string.delete_my_image));
        alertDialogItems.add(context.getString(R.string.cancel_small));
        return alertDialogItems;
    }

    public static Uri getImageUri(Context context, Bitmap imageBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, BuildConfig.DEFAULT_NAME_CUSTOMER_PROFILE_IMAGE_WITH_EXTENTION, null);
        return Uri.parse(path);
    }

    public static Bitmap resizeBitmap(Bitmap getBitmap, int maxSize) {
        int width = getBitmap.getWidth();
        int height = getBitmap.getHeight();
        double x;

        if (width >= height && width > maxSize) {
            x = width / height;
            width = maxSize;
            height = (int) (maxSize / x);
        } else if (height >= width && height > maxSize) {
            x = height / width;
            height = maxSize;
            width = (int) (maxSize / x);
        }
        return Bitmap.createScaledBitmap(getBitmap, width, height, false);
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        //Base64.de
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public enum ImageType {
        BANNER_SIZE_SMALL, BANNER_SIZE_GENERIC, BANNER_SIZE_LARGE, PRODUCT_TINY, PRODUCT_SMALL, PRODUCT_GENERIC, PRODUCT_LARGE, PROFILE_PIC_SMALL, PROFILE_PIC_GENERIC
    }
}
