package com.webkul.mobikul.odoo.dialog_frag;

import androidx.fragment.app.DialogFragment;
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

public class BaseDialogFragment extends DialogFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "BaseDialogFragment";

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        ((BaseActivity) getContext()).mCompositeDisposable.clear();
    }
}
