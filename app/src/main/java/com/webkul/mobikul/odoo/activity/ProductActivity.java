package com.webkul.mobikul.odoo.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.product.AlternativeProductsRvAdapter;
import com.webkul.mobikul.odoo.adapter.product.ProductImageAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.database.SaveData;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ActivityProductBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.handler.product.ProductActivityHandler;
import com.webkul.mobikul.odoo.handler.product.ProductReviewHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.ColorHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
import com.webkul.mobikul.odoo.helper.ViewHelper;
import com.webkul.mobikul.odoo.model.generic.AttributeData;
import com.webkul.mobikul.odoo.model.generic.AttributeOptionData;
import com.webkul.mobikul.odoo.model.generic.ProductCombination;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.generic.ProductVariant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.helper.FontHelper.FONT_PATH_1;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_COLOR;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_HIDDEN;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_RADIO;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_SELECT;

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

public class ProductActivity extends BaseActivity {
    public static final int RC_ADD_TO_CART = 1001;
    public static final int RC_BUY_NOW = 1002;
    @SuppressWarnings("unused")
    private static final String TAG = "ProductActivity";
    public ActivityProductBinding mBinding;
    private final CustomObserver<ProductData> mProductDataCustomObserver = new CustomObserver<ProductData>(this) {

        @Override
        public void onNext(@NonNull ProductData productData) {
            super.onNext(productData);
            if (productData.isAccessDenied()) {
                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(ProductActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        AppSharedPref.clearCustomerData(ProductActivity.this);
                        Intent i = new Intent(ProductActivity.this, SignInSignUpActivity.class);
                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ProductActivity.class.getSimpleName());
                        startActivity(i);
                    }
                });
            } else {
                setDataAfterFetchData(productData);
            }
        }

        @Override
        public void onError(@NonNull Throwable t) {
            super.onError(t);
        }

        @Override
        public void onComplete() {

        }
    };
    private boolean returnedWithResult = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setDataAfterFetchData(ProductData productData) {
        mBinding.setData(productData);
        if (TextUtils.isEmpty(productData.getName())) {
            mBinding.setTitle(getIntent().getExtras().getString(BUNDLE_KEY_PRODUCT_NAME));
        } else {
            mBinding.setTitle(productData.getName());
        }
        new SaveData(ProductActivity.this, productData);
        mBinding.setHandler(new ProductActivityHandler(ProductActivity.this, productData));
        mBinding.setReviewHandler(new ProductReviewHandler(ProductActivity.this, productData.getTemplateId()));
        mBinding.executePendingBindings();
        /*Loading Banner Data*/
        mBinding.productSliderViewPager.setAdapter(new ProductImageAdapter(ProductActivity.this, productData.getImages()));
        mBinding.productSliderDotsTabLayout.setupWithViewPager(mBinding.productSliderViewPager, true);
        loadProductAttributes(productData);
        updatePrices(productData);
        setAlternativeProductData();

    }

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        Helper.hideKeyboard(this);
        if (!returnedWithResult) {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(this);
            ProductData productData = sqlLiteDbHelper.getProductScreenData(getIntent().getExtras().getString(BUNDLE_KEY_PRODUCT_ID));
            if (!NetworkHelper.isNetworkAvailable(this) && productData != null) {
//                callback.onSuccess(homePageResponse);
                setDataAfterFetchData(productData);
            } else {
                ApiConnection.getProductData(this, getIntent().getExtras().getString(BUNDLE_KEY_PRODUCT_ID)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mProductDataCustomObserver);
            }

        } else {
            returnedWithResult = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mBinding.setData(null);

//        ApiConnection.getProductData(this, getIntent().getExtras().getString(BUNDLE_KEY_PRODUCT_ID)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mProductDataCustomObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        if (getIntent().getExtras() != null)
            FirebaseAnalyticsImpl.logViewItem(this, getIntent().getExtras().getString(BUNDLE_KEY_PRODUCT_ID), getIntent().getExtras().getString(BUNDLE_KEY_PRODUCT_NAME));
        setSupportActionBar(mBinding.toolbar);
        showBackButton(true);
        onNewIntent(getIntent());
    }

    public void loadProductAttributes(ProductData productData) {
        mBinding.attributesContainer.removeAllViews();
        for (AttributeData eachAttributeData : productData.getAttributes()) {
            /*LABELS*/
            AppCompatTextView attrLabelTv = new AppCompatTextView(ProductActivity.this);
            attrLabelTv.setTypeface(Typeface.createFromAsset(getAssets(), FONT_PATH_1), Typeface.BOLD);
            attrLabelTv.setText(eachAttributeData.getName());
            mBinding.attributesContainer.addView(attrLabelTv);
            switch (eachAttributeData.getType()) {
                case ATTR_TYPE_COLOR:

                    HorizontalScrollView colorTypeHsv = new HorizontalScrollView(this);
                    colorTypeHsv.setVerticalScrollBarEnabled(false);
                    colorTypeHsv.setHorizontalScrollBarEnabled(false);
                    RadioGroup colorTypeRg = new RadioGroup(ProductActivity.this);
                    colorTypeRg.setTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    LinearLayout.LayoutParams rgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    colorTypeRg.setPadding(ViewHelper.dpToPx(ProductActivity.this, 16), ViewHelper.dpToPx(ProductActivity.this, 8), ViewHelper.dpToPx(ProductActivity.this, 16), ViewHelper.dpToPx(ProductActivity.this, 8));
                    colorTypeRg.setLayoutParams(rgParams);
                    colorTypeRg.setOrientation(RadioGroup.HORIZONTAL);
                    List<AttributeOptionData> attributeOptionDatas = eachAttributeData.getAttributeOptionDatas();
                    for (int attrOptnDataIdx = 0; attrOptnDataIdx < attributeOptionDatas.size(); attrOptnDataIdx++) {
                        AttributeOptionData eachAttributeOptionData = attributeOptionDatas.get(attrOptnDataIdx);
                        AppCompatRadioButton colorTypeRb = new AppCompatRadioButton(ProductActivity.this);
                        colorTypeRb.setId(attrOptnDataIdx);
                        colorTypeRb.setTag(eachAttributeOptionData.getValueId());
                        colorTypeRb.setChecked(productData.isUnderDefaultCombination(eachAttributeData.getAttributeId(), eachAttributeOptionData.getValueId()));
                        colorTypeRb.setBackground(getSelector(eachAttributeOptionData.getHtmlCode()));
                        colorTypeRb.setButtonDrawable(null);
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewHelper.dpToPx(ProductActivity.this, 50), ViewHelper.dpToPx(ProductActivity.this, 50));
                        layoutParams.setMargins(0, 0, ViewHelper.dpToPx(ProductActivity.this, 8), 0);
                        colorTypeRg.addView(colorTypeRb, layoutParams);
                    }
                    colorTypeHsv.addView(colorTypeRg);
                    mBinding.attributesContainer.addView(colorTypeHsv);
                    colorTypeRg.setOnCheckedChangeListener((group, checkedId) -> updatePrices(productData));
                    break;

                case ATTR_TYPE_RADIO:
                    final RadioGroup radioTypeRg = new RadioGroup(ProductActivity.this);
                    radioTypeRg.setTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    radioTypeRg.setPadding(ViewHelper.dpToPx(ProductActivity.this, 16), ViewHelper.dpToPx(ProductActivity.this, 8), ViewHelper.dpToPx(ProductActivity.this, 16), ViewHelper.dpToPx(ProductActivity.this, 8));
                    for (int attrOptnDataIdx = 0; attrOptnDataIdx < eachAttributeData.getAttributeOptionDatas().size(); attrOptnDataIdx++) {
                        AttributeOptionData eachAttributeOptionData = eachAttributeData.getAttributeOptionDatas().get(attrOptnDataIdx);
                        AppCompatRadioButton radioTypeRb = new AppCompatRadioButton(ProductActivity.this);
                        radioTypeRb.setId(attrOptnDataIdx);
                        radioTypeRb.setTag(eachAttributeOptionData.getValueId());
                        radioTypeRb.setChecked(productData.isUnderDefaultCombination(eachAttributeData.getAttributeId(), eachAttributeOptionData.getValueId()));
                        radioTypeRb.setText(eachAttributeOptionData.getName());
                        radioTypeRg.addView(radioTypeRb);
                    }
                    mBinding.attributesContainer.addView(radioTypeRg);
                    radioTypeRg.setOnCheckedChangeListener((group, checkedId) -> updatePrices(productData));
                    break;

                case ATTR_TYPE_SELECT:
                case ATTR_TYPE_HIDDEN:
                    AppCompatSpinner selectTypeSpinner = new AppCompatSpinner(ProductActivity.this);
                    selectTypeSpinner.setTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    selectTypeSpinner.setPadding(ViewHelper.dpToPx(ProductActivity.this, 16), ViewHelper.dpToPx(ProductActivity.this, 8), ViewHelper.dpToPx(ProductActivity.this, 16), ViewHelper.dpToPx(ProductActivity.this, 8));
                    ArrayAdapter<String> attrOptnArrayAdapter = new ArrayAdapter<>(ProductActivity.this, android.R.layout.simple_spinner_item, eachAttributeData.getAttributeOptionNameArr());
                    attrOptnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectTypeSpinner.setAdapter(attrOptnArrayAdapter);
                    for (int attrOptnDataIdx = 0; attrOptnDataIdx < eachAttributeData.getAttributeOptionDatas().size(); attrOptnDataIdx++) {
                        if (productData.isUnderDefaultCombination(eachAttributeData.getAttributeId(), eachAttributeData.getAttributeOptionDatas().get(attrOptnDataIdx).getValueId())) {
                            selectTypeSpinner.setSelection(attrOptnDataIdx);
                        }
                    }
                    mBinding.attributesContainer.addView(selectTypeSpinner);
                    selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            updatePrices(productData);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
            }
        }

    }

    private String getIdUnitForVariant(ProductData productData) {
        Map<String, String> attrValueIdMap = new HashMap<>();
        for (AttributeData eachAttributeData : productData.getAttributes()) {
            switch (eachAttributeData.getType()) {
                case ATTR_TYPE_COLOR:
                    RadioGroup colorTypeRg = (mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId()));
                    if (colorTypeRg.getCheckedRadioButtonId() != -1) {
                        attrValueIdMap.put(eachAttributeData.getAttributeId(), colorTypeRg.findViewById(colorTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    }
                    break;

                case ATTR_TYPE_RADIO:
                    RadioGroup radioTypeRg = (mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId()));
                    if (radioTypeRg.getCheckedRadioButtonId() != -1) {
                        attrValueIdMap.put(eachAttributeData.getAttributeId(), radioTypeRg.findViewById(radioTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    }
                    break;

                case ATTR_TYPE_SELECT:
                case ATTR_TYPE_HIDDEN:
                    AppCompatSpinner selectTypeSpinner = (mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId()));
                    if (selectTypeSpinner.getSelectedItemPosition() != -1) {
                        attrValueIdMap.put(eachAttributeData.getAttributeId(), eachAttributeData.getAttributeOptionDatas().get(selectTypeSpinner.getSelectedItemPosition()).getValueId());
                    }
                    break;
            }
        }


        for (ProductVariant eachProductVariant : productData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                return eachProductVariant.getProductId();
            }
        }
        return null;
    }


    private String getPriceUnitForVariant(ProductData productData) {
        Map<String, String> attrValueIdMap = getAttrValueIdMap(productData);
        for (ProductVariant eachProductVariant : productData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                if (eachProductVariant.getPriceReduce().isEmpty()) {
                    mBinding.unitPriceTv.setBackground(null);
                }
                return eachProductVariant.getPriceUnit();
            }
        }
        /*default case*/
        return productData.getPriceUnit();
    }

    private String getPriceReducedForVariant(ProductData productData) {
        Map<String, String> attrValueIdMap = getAttrValueIdMap(productData);
        for (ProductVariant eachProductVariant : productData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                return eachProductVariant.getPriceReduce();
            }
        }
        /*default case*/
        return productData.getPriceReduce();
    }

    private boolean isAddedToWishlistForVariant(ProductData productData) {
        Map<String, String> attrValueIdMap = getAttrValueIdMap(productData);
        for (ProductVariant eachProductVariant : productData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                return eachProductVariant.isAddedToWishlist();
            }
        }
        /*default case*/
        return productData.isAddedToWishlist();
    }

    private List<String> getImagesForVariant(ProductData productData) {
        Map<String, String> attrValueIdMap = getAttrValueIdMap(productData);
        for (ProductVariant eachProductVariant : productData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                return eachProductVariant.getImages();
            }
        }
        /*default case*/
        return productData.getImages();
    }

    private Map<String, String> getAttrValueIdMap(ProductData productData) {
        Map<String, String> attrValueIdMap = new HashMap<>();
        for (AttributeData eachAttributeData : productData.getAttributes()) {
            switch (eachAttributeData.getType()) {
                case ATTR_TYPE_COLOR:
                    RadioGroup colorTypeRg = mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    if (colorTypeRg.getCheckedRadioButtonId() != -1) {
                        attrValueIdMap.put(eachAttributeData.getAttributeId(), colorTypeRg.findViewById(colorTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    }
                    break;
                case ATTR_TYPE_RADIO:
                    RadioGroup radioTypeRg = mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    if (radioTypeRg.getCheckedRadioButtonId() != -1) {
                        attrValueIdMap.put(eachAttributeData.getAttributeId(), radioTypeRg.findViewById(radioTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    }
                    break;
                case ATTR_TYPE_SELECT:
                case ATTR_TYPE_HIDDEN:
                    AppCompatSpinner selectTypeSpinner = mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    if (selectTypeSpinner.getSelectedItemPosition() != -1) {
                        attrValueIdMap.put(eachAttributeData.getAttributeId(), eachAttributeData.getAttributeOptionDatas().get(selectTypeSpinner.getSelectedItemPosition()).getValueId());
                    }
                    break;
            }
        }
        return attrValueIdMap;
    }

    private void updatePrices(ProductData productData) {
        if (productData.getVariants().size() > 0) {
            // mBinding.unitPriceTv.setText(getPriceUnitForVariant(productData));
            // mBinding.reducePriceTv.setText(getPriceReducedForVariant(productData));
            productData.setPriceReduce(getPriceReducedForVariant(productData));
            productData.setPriceUnit(getPriceUnitForVariant(productData));
            mBinding.setData(productData);
            mBinding.getHandler().setWishlistForvariants(isAddedToWishlistForVariant(productData));
            mBinding.wishlistIcon.setImageResource(isAddedToWishlistForVariant(productData) ? R.drawable.ic_vector_wishlist_red_24dp : R.drawable.ic_vector_wishlist_grey_24dp);
            mBinding.productSliderViewPager.setAdapter(new ProductImageAdapter(ProductActivity.this, getImagesForVariant(productData)));
            updateAddtoCart(productData);
        }
    }

    private void updateAddtoCart(ProductData productData) {
        if (getIdUnitForVariant(productData) == null) {
            mBinding.addToCartButton.setVisibility(View.GONE);
            mBinding.wishlistIcon.setVisibility(View.GONE);
            mBinding.productNotAvailableTv.setVisibility(View.VISIBLE);
        } else {
            mBinding.addToCartButton.setVisibility(View.VISIBLE);
            if (AppSharedPref.isAllowedWishlist(this))
                mBinding.wishlistIcon.setVisibility(View.VISIBLE);
            mBinding.productNotAvailableTv.setVisibility(View.GONE);
        }
    }


    public StateListDrawable getSelector(String htmlCode) {
        if (htmlCode.isEmpty()) {
            htmlCode = "#FFFFFF";
        }
        StateListDrawable res = new StateListDrawable();

        /*PRESSED STATE*/
        GradientDrawable pressedState = new GradientDrawable();
        pressedState.setShape(GradientDrawable.RECTANGLE);
        pressedState.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8});
        try {
            pressedState.setColor(Color.parseColor(htmlCode));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getSelector: color value" + htmlCode);
            pressedState.setColor(Color.parseColor("#FFFFFF"));

        }
        pressedState.setStroke(12, ColorHelper.getColor(this, R.attr.colorAccent));
        res.addState(new int[]{android.R.attr.state_pressed}, pressedState);


        /*CHECKED STATE*/
        res.addState(new int[]{android.R.attr.state_checked}, pressedState);

        /*IDLE STATE*/
        GradientDrawable idleState = new GradientDrawable();
        idleState.setShape(GradientDrawable.RECTANGLE);
        idleState.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8});
        idleState.setColor(Color.parseColor(htmlCode));
        idleState.setStroke(1, Color.LTGRAY);
        res.addState(new int[]{}, idleState);
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        returnedWithResult = true;
        switch (requestCode) {
            case RC_ADD_TO_CART:

                switch (resultCode) {
                    case RESULT_OK:
                        mBinding.getHandler().addToCart(false);
                        break;
                }
                break;

            case RC_BUY_NOW:

                switch (resultCode) {
                    case RESULT_OK:
                        mBinding.getHandler().addToCart(true);
                        break;
                }
                break;
            case MaterialSearchView.RC_CAMERA_SEARCH:
                switch (resultCode) {
                    case RESULT_OK:
                        mBinding.searchView.setQuery(data.getStringExtra(BundleConstant.CAMERA_SEARCH_HELPER), true);
                        break;
                }
                break;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setAlternativeProductData() {
        mBinding.rvAlternativeProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBinding.rvAlternativeProduct.setAdapter(new AlternativeProductsRvAdapter(this, mBinding.getData().getAlternativeProducts(), false));
    }


}
