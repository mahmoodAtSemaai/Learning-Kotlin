package com.webkul.mobikul.odoo.dialog_frag;

import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.DialogFragmentProductAddedToBagBinding;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_MESSAGE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class ProductAddedToBagDialogFrag extends BaseDialogFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "ProductAddedToBagDialog";
    private static ProductAddedToBagDialogFrag productAddedToBagDialogFrag;

    public static ProductAddedToBagDialogFrag newInstance(String productName, String message) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_PRODUCT_NAME, productName);
        args.putString(BUNDLE_KEY_MESSAGE, message);
        productAddedToBagDialogFrag = new ProductAddedToBagDialogFrag();
        productAddedToBagDialogFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        productAddedToBagDialogFrag.setArguments(args);
        return productAddedToBagDialogFrag;
    }

    public static void dismissDialog() {
        if (productAddedToBagDialogFrag != null) {
            productAddedToBagDialogFrag.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogFragmentProductAddedToBagBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_product_added_to_bag, container, false);
        binding.setProductName(getArguments().getString(BUNDLE_KEY_PRODUCT_NAME));
        binding.setMessage(getArguments().getString(BUNDLE_KEY_MESSAGE));
        return binding.getRoot();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        productAddedToBagDialogFrag = null;
    }
}
