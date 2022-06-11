package com.webkul.mobikul.odoo.handler.product;

import android.content.Context;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.fragment.FullScreenImageScrollFragment;
import com.webkul.mobikul.odoo.helper.FragmentHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 28/12/16. @Webkul Software Pvt. Ltd
 */

public class ProductImageHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "ProductSliderHandler";
    private Context mContext;
    private List<String> mImages;
    private int mPosition;


    public ProductImageHandler(Context context, List<String> images, int position) {
        mContext = context;
        mImages = images;
        mPosition = position;
    }

}
