package com.webkul.mobikul.odoo.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.adapter.extra.SearchHistoryAdapter;
import com.webkul.mobikul.odoo.adapter.extra.SearchSuggestionProductAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.connection.RetrofitClient;
import com.webkul.mobikul.odoo.data.entity.ProductListEntity;
import com.webkul.mobikul.odoo.data.response.ProductListResponse;
import com.webkul.mobikul.odoo.database.SearchHistoryContract;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.MaterialSearchViewBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.handler.extra.search.MaterialSearchViewHandler;
import com.webkul.mobikul.odoo.helper.AnimationHelper;
import com.webkul.mobikul.odoo.helper.BindingAdapterUtils;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.DEBOUNCE_REQUEST_TIMEOUT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_QUERY;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class MaterialSearchView extends FrameLayout {

    public static final int RC_MATERIAL_SEARCH_VOICE = 42;
    public static final int RC_CAMERA_SEARCH = 1042;
    @SuppressWarnings("unused")
    private static final String TAG = "MaterialSearchView";
    private static final int MAX_VOICE_RESULTS = 1;
    public MaterialSearchViewBinding binding;
    private Context context;
    private boolean open, isFromSubmitResult;
    private CharSequence currentQuery;

    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initializeView();
    }

    private void initializeView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.material_search_view, this, true);
        binding.setHandler(new MaterialSearchViewHandler(this));

    }

    public Context getmContext() {
        return context;
    }

    private void initSearchView() {
        isFromSubmitResult = false;
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            onSubmitQuery();
            return true;
        });

        binding.rvSearchHistory.setAdapter(new SearchHistoryAdapter(context, getSearchHistoryList(""), ""));
        RxTextView.textChangeEvents(binding.etSearch).debounce(DEBOUNCE_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<TextViewTextChangeEvent>(context) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                /*don't unsubscribe*/
            }

            @Override
            public void onNext(@NonNull TextViewTextChangeEvent textViewTextChangeEvent) {
                if (!isFromSubmitResult) {

                    ApiConnection.getProductSearchResponse(context, true, textViewTextChangeEvent.text().toString(), 0, BuildConfig.DEFAULT_NO_OF_SEARCH_PRODUCTS)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).debounce(2,TimeUnit.SECONDS).subscribe(new CustomObserver<BaseResponseNew<ProductListResponse>>(context) {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    super.onSubscribe(d);
                                    binding.pbSearch.setVisibility(VISIBLE);
                                }

                                @Override
                                public void onNext(BaseResponseNew<ProductListResponse> productListResponse) {
                                    super.onNext(productListResponse);
                                    FirebaseAnalyticsImpl.logSearchEvent(context,textViewTextChangeEvent.text().toString());
                                    Gson gson = new Gson();
                                    ProductListEntity productListEntity = gson.fromJson(gson.toJson(productListResponse.getData()), ProductListEntity.class);
                                    binding.setSearchSuggestionData(productListEntity);
                                    if (binding.rvSearchSuggestions.getAdapter() == null) {
                                        binding.rvSearchSuggestions.setAdapter(new SearchSuggestionProductAdapter(context, productListEntity.getProducts(), textViewTextChangeEvent.text().toString()));
                                    } else {
                                        ((SearchSuggestionProductAdapter) binding.rvSearchSuggestions.getAdapter()).updateData(productListEntity.getProducts(), textViewTextChangeEvent.text().toString());
                                    }
                                }

                                @Override
                                public void onError(Throwable t) {
                                    super.onError(t);
                                }

                                @Override
                                public void onComplete() {
                                    super.onComplete();
                                    binding.pbSearch.setVisibility(GONE);
                                }
                            });

                   }

            }
        });


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                ((SearchHistoryAdapter) binding.rvSearchHistory.getAdapter()).updateSearchHistory(getSearchHistoryList(charSequence.toString()), charSequence.toString());
                MaterialSearchView.this.onTextChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showSuggestions();
            }
        });
    }

    private List<String> getSearchHistoryList(String searchTerm) {
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
       return sqlLiteDbHelper.getSearchHistoryList(searchTerm);
    }

    private void deleteAllSearchHistory(){
        ((BaseActivity) context).mSqLiteDatabase.delete(SearchHistoryContract.SearchHistoryEntry.TABLE_NAME,null,null);

    }

    private void onTextChanged(@SuppressWarnings("UnusedParameters") CharSequence newText) {
        currentQuery = binding.etSearch.getText();
        if (!TextUtils.isEmpty(currentQuery)) {
            displayVoiceButton(false);
            displayClearButton(true);
        } else {
            displayClearButton(false);
            displayVoiceButton(true);
        }
    }

    private void onSubmitQuery() {
        CharSequence query = binding.etSearch.getText();
        isFromSubmitResult = true;
        Helper.hideKeyboard(context);
        if (query != null && !query.equals("") && TextUtils.getTrimmedLength(query) > 0) {

            saveQueryToDb(query.toString(), System.currentTimeMillis());

            Intent intent = new Intent(context, CatalogProductActivity.class);
            intent.setAction(Intent.ACTION_SEARCH);
            intent.putExtra(SearchManager.QUERY, query.toString());
            intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, query.toString());
            intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, SEARCH_QUERY);
            context.startActivity(intent);
            closeSearch();
        }
    }

    public synchronized void saveQueryToDb(String query, long insertTime) {
        if (!TextUtils.isEmpty(query) && insertTime > 0) {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getContext());
            SQLiteDatabase sqLiteDatabase = sqlLiteDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY, query);
            values.put(SearchHistoryContract.SearchHistoryEntry.COLUMN_INSERT_DATE, insertTime);
            sqLiteDatabase.insert(SearchHistoryContract.SearchHistoryEntry.TABLE_NAME, null, values);
        }
    }


    public void setQuery(CharSequence query, boolean submit) {
        if (query != null) {
            BindingAdapterUtils.setHtmlText(binding.etSearch, query.toString());
            binding.etSearch.setSelection(binding.etSearch.length());
            currentQuery = query;
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void closeSearch() {
        if (!open) {
            return;
        }
        binding.etSearch.setText("");
        dismissSuggestions();
        clearFocus();
        AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.flSearch.setVisibility(View.GONE);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationHelper.circleHideView(binding.llSearchBar, listenerAdapter);
        } else {
            AnimationHelper.fadeOutView(binding.flSearch);
        }
        open = false;
        setVisibility(View.GONE);
        RetrofitClient.getDispatcher().cancelAll();
    }

    public void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.hint_prompt));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_VOICE_RESULTS); // Quantity of results we want to receive
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, RC_MATERIAL_SEARCH_VOICE);
        }
    }

    public void openSearch() {
        if (open) {
            return;
        }
        binding.etSearch.setText("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.flSearch.setVisibility(View.VISIBLE);
            AnimationHelper.circleRevealView(binding.llSearchBar);
        } else {
            AnimationHelper.fadeInView(binding.flSearch);
        }
        setVisibility(View.VISIBLE);
        initSearchView();
        open = true;
    }

    public boolean isOpen() {
        return open;
    }

    private void displayVoiceButton(boolean display) {
        if (display && Helper.isVoiceAvailable(context)) {
            binding.ibActionVoice.setVisibility(View.VISIBLE);
        } else {
            binding.ibActionVoice.setVisibility(View.GONE);
        }
    }

    private void displayClearButton(boolean display) {
        binding.ibActionClear.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    private void showSuggestions() {
        binding.rvSearchSuggestions.setVisibility(View.VISIBLE);
    }

    private void dismissSuggestions() {
        binding.rvSearchSuggestions.setVisibility(View.GONE);
    }

}
