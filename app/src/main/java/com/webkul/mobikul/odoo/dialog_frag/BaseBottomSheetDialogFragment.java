package com.webkul.mobikul.odoo.dialog_frag;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseBottomSheetDialogFr";

    @Override
    public void onStop() {
        super.onStop();
        ((BaseActivity) getContext()).mCompositeDisposable.clear();
    }
}
