package com.webkul.mobikul.marketplace.odoo.activity;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivityBecomeSellerBinding;
import com.webkul.mobikul.marketplace.odoo.handler.BecomeSellerActivityHandler;
import com.webkul.mobikul.marketplace.odoo.model.BecomeSellerData;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.model.generic.CountryStateData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class BecomeSellerActivity extends BaseActivity {

    private ActivityBecomeSellerBinding mBinding;
    private static final String TAG = "BecomeSellerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_become_seller);
        mBinding.setData(new BecomeSellerData(BecomeSellerActivity.this));
        mBinding.setTitle(getString(R.string.become_seller_activity_title));
        mBinding.setHandler(new BecomeSellerActivityHandler(BecomeSellerActivity.this,mBinding));
        setSupportActionBar(mBinding.toolbar);
        callCountryStateData();
    }

    private void callCountryStateData() {
        ApiConnection.getCountryStateData(BecomeSellerActivity.this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<CountryStateData>(BecomeSellerActivity.this) {
            @Override
            public void onNext(@NonNull CountryStateData countryStateData) {
                super.onNext(countryStateData);

                setUpCountrySpinners(countryStateData);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void setUpCountrySpinners(CountryStateData countryStateData) {
        mBinding.countrySpinner.setAdapter(new ArrayAdapter<>(BecomeSellerActivity.this, android.R.layout.simple_spinner_dropdown_item, countryStateData.getCountryNameList(BecomeSellerActivity.this)));
        mBinding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int countryPos, long id) {
                mBinding.getData().setCountryId(countryStateData.getCountries().get(countryPos).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBinding.countrySpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }
}
