package com.webkul.mobikul.odoo.activity;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.home.NavDrawerCategoryStartAdapter;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.databinding.ActivitySubCategoryBinding;
import com.webkul.mobikul.odoo.model.generic.CategoryData;

import java.util.ArrayList;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_OBJECT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PARENT_CATEGORY;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class SubCategoryActivity extends BaseActivity {

    ActivitySubCategoryBinding mBinding;
    private static final String TAG = "SubCategoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sub_category);
        setSupportActionBar(mBinding.toolbar);
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(BUNDLE_KEY_CATEGORY_OBJECT)) {
                CategoryData data = getIntent().getExtras().getParcelable(BUNDLE_KEY_CATEGORY_OBJECT);
                String parentCategory = getIntent().getExtras().getString(BUNDLE_KEY_PARENT_CATEGORY);
                if (data != null) {
                    mBinding.setTitle(data.getName());
                    mBinding.subCategoryRecyclerView.setAdapter(new NavDrawerCategoryStartAdapter(this, data.getChildren(), parentCategory));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.searchView.isOpen()) {
            mBinding.searchView.closeSearch();
            try {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(mBinding.searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            onResume();
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MaterialSearchView.RC_MATERIAL_SEARCH_VOICE:
                switch (resultCode) {
                    case RESULT_OK:
                        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && matches.size() > 0) {
                            String searchWrd = matches.get(0);
                            if (!TextUtils.isEmpty(searchWrd)) {
                                mBinding.searchView.setQuery(searchWrd, false);
                            }
                        }
                        break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }

}
