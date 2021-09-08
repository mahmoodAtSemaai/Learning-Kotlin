package com.webkul.mobikul.odoo.fragment;

import androidx.fragment.app.Fragment;
import android.util.Log;

import com.webkul.mobikul.odoo.activity.BaseActivity;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class BaseFragment extends Fragment {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseFragment";

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        // FIXME: 25/8/17 Changed for Review backpress to work.
        if (!(this instanceof ProductReviewFragment)) {
            ((BaseActivity) getContext()).mCompositeDisposable.clear();
        }
    }
}
