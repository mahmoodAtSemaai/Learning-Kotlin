package com.webkul.mobikul.marketplace.odoo.activity;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivityAskToAdminBinding;
import com.webkul.mobikul.marketplace.odoo.handler.AskToAdminActivityHandler;
import com.webkul.mobikul.odoo.activity.BaseActivity;

/**
 * Created by aastha.gupta on 9/4/18 in Mobikul_Odoo_Application.
 */

public class AskToAdminActivity extends BaseActivity {
    ActivityAskToAdminBinding mBinding;
    private static final String TAG = "AskToAdminActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.searchView.isOpen()) {
            mBinding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ask_to_admin);
        setSupportActionBar(mBinding.toolbar);
        mBinding.setTitle(getString(R.string.ask_to_admin));
        mBinding.setHandler(new AskToAdminActivityHandler(this, mBinding));
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }
}
