package com.webkul.mobikul.odoo.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.adapter.extra.SearchHistoryAdapter;
import com.webkul.mobikul.odoo.adapter.extra.SearchSuggestionProductAdapter;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.connection.RetrofitClient;
import com.webkul.mobikul.odoo.database.SearchHistoryContract;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.MaterialSearchViewBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.handler.extra.search.MaterialSearchViewHandler;
import com.webkul.mobikul.odoo.helper.AnimationHelper;
import com.webkul.mobikul.odoo.helper.BindingAdapterUtils;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.request.SearchRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.DEBOUNCE_REQUEST_TIMEOUT;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.MAX_SEARCH_HISTORY_RESULT;
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
    public MaterialSearchViewBinding mBinding;
    private Context mContext;
    private boolean mOpen, isFromSubmitResult;
    private CharSequence mCurrentQuery;

    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initializeView();
    }

    private void initializeView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.material_search_view, this, true);
        mBinding.setHandler(new MaterialSearchViewHandler(this));
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                initSearchView();
//            }
//        }, 5000);
////        initSearchView();
    }

    public Context getmContext() {
        return mContext;
    }

    private void initSearchView() {
        isFromSubmitResult = false;
        mBinding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            onSubmitQuery();
            return true;
        });

        mBinding.seachHistoryRv.setAdapter(new SearchHistoryAdapter(mContext, getSearchHistoryList(""), ""));
        RxTextView.textChangeEvents(mBinding.etSearch).debounce(DEBOUNCE_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<TextViewTextChangeEvent>(mContext) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                /*don't unsubscribe*/
            }

            @Override
            public void onNext(@NonNull TextViewTextChangeEvent textViewTextChangeEvent) {
                if (!isFromSubmitResult) {

                    ApiConnection.getSearchResponse(mContext, textViewTextChangeEvent.text().toString(), 0, BuildConfig.DEFAULT_NO_OF_SEARCH_PRODUCTS)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).debounce(2, TimeUnit.SECONDS).subscribe(new CustomObserver<CatalogProductResponse>(mContext) {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            super.onSubscribe(d);
                            mBinding.mainProgressBar.setVisibility(VISIBLE);
                        }

                        @Override
                        public void onNext(@NonNull CatalogProductResponse catalogProductResponse) {
                            super.onNext(catalogProductResponse);
                            Log.d(TAG, "onNext: ");
                            FirebaseAnalyticsImpl.logSearchEvent(mContext,textViewTextChangeEvent.text().toString());
                            mBinding.setSearchSuggestionData(catalogProductResponse);
                            if (mBinding.suggestionProductsRv.getAdapter() == null) {
                                mBinding.suggestionProductsRv.setAdapter(new SearchSuggestionProductAdapter(mContext, catalogProductResponse.getProducts(), textViewTextChangeEvent.text().toString()));
                            } else {
                                ((SearchSuggestionProductAdapter) mBinding.suggestionProductsRv.getAdapter()).updateData(catalogProductResponse.getProducts(), textViewTextChangeEvent.text().toString());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                        }

                        @Override
                        public void onComplete() {
                            mBinding.mainProgressBar.setVisibility(GONE);
                        }
                    });
                }

            }
        });


        mBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                ((SearchHistoryAdapter) mBinding.seachHistoryRv.getAdapter()).updateSearchHistory(getSearchHistoryList(charSequence.toString()), charSequence.toString());
                MaterialSearchView.this.onTextChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBinding.etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            // If we gain focus, show keyboard and show suggestions.
            if (hasFocus) {
//                Helper.showKeyboard(mBinding.etSearch);
                showSuggestions();
            }
        });
    }

    private List<String> getSearchHistoryList(String searchTerm) {
        Cursor cursor = ((BaseActivity) mContext).mSqLiteDatabase.query(
                SearchHistoryContract.SearchHistoryEntry.TABLE_NAME,
                new String[]{SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY},
                SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY + " LIKE ?",
                new String[]{"%" + searchTerm + "%"},
                null,
                null,
                SearchHistoryContract.SearchHistoryEntry.COLUMN_INSERT_DATE + " DESC"
                , MAX_SEARCH_HISTORY_RESULT
        );

        List<String> searchQueries = new ArrayList<>();
        while (cursor.moveToNext()) {
            searchQueries.add(cursor.getString(cursor.getColumnIndexOrThrow(SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY)));
        }
        cursor.close();
        return searchQueries;
    }

    private void deleteAllSearchHistory(){
        ((BaseActivity) mContext).mSqLiteDatabase.delete(SearchHistoryContract.SearchHistoryEntry.TABLE_NAME,null,null);

    }

    private void onTextChanged(@SuppressWarnings("UnusedParameters") CharSequence newText) {
        mCurrentQuery = mBinding.etSearch.getText();
        if (!TextUtils.isEmpty(mCurrentQuery)) {
            displayVoiceButton(false);
            displayClearButton(true);
        } else {
            displayClearButton(false);
            displayVoiceButton(true);
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mBinding.etSearch.getText();
        isFromSubmitResult = true;
        Helper.hideKeyboard(mContext);
        if (query != null && !query.equals("") && TextUtils.getTrimmedLength(query) > 0) {

            saveQueryToDb(query.toString(), System.currentTimeMillis());

            Intent intent = new Intent(mContext, CatalogProductActivity.class);
            intent.setAction(Intent.ACTION_SEARCH);
            intent.putExtra(SearchManager.QUERY, query.toString());
            intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, query.toString());
            intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, SEARCH_QUERY);
            mContext.startActivity(intent);
            closeSearch();
//            mBinding.etSearch.setText("");
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
            BindingAdapterUtils.setHtmlText(mBinding.etSearch, query.toString());
            mBinding.etSearch.setSelection(mBinding.etSearch.length());
            mCurrentQuery = query;
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void closeSearch() {
        if (!mOpen) {
            return;
        }
        mBinding.etSearch.setText("");
        dismissSuggestions();
        clearFocus();
        AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.searchLayout.setVisibility(View.GONE);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationHelper.circleHideView(mBinding.searchBar, listenerAdapter);
        } else {
            AnimationHelper.fadeOutView(mBinding.searchLayout);
        }
        mOpen = false;
        setVisibility(View.GONE);
        RetrofitClient.getDispatcher().cancelAll();
    }

    public void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mContext.getString(R.string.hint_prompt));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_VOICE_RESULTS); // Quantity of results we want to receive
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, RC_MATERIAL_SEARCH_VOICE);
        }
    }

    public void openSearch() {
        if (mOpen) {
            return;
        }
        mBinding.etSearch.setText("");
//        mBinding.etSearch.requestFocus();

//        ((SearchHistoryAdapter) mBinding.seachHistoryRv.getAdapter()).updateSearchHistory(getSearchHistoryList(""), "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.searchLayout.setVisibility(View.VISIBLE);
            AnimationHelper.circleRevealView(mBinding.searchBar);
        } else {
            AnimationHelper.fadeInView(mBinding.searchLayout);
        }
        setVisibility(View.VISIBLE);
        initSearchView();
        mOpen = true;
    }

    public boolean isOpen() {
        return mOpen;
    }

    private void displayVoiceButton(boolean display) {
        if (display && Helper.isVoiceAvailable(mContext)) {
            mBinding.actionVoice.setVisibility(View.VISIBLE);
        } else {
            mBinding.actionVoice.setVisibility(View.GONE);
        }
    }

    private void displayClearButton(boolean display) {
        mBinding.actionClear.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    private void showSuggestions() {
        mBinding.suggestionProductsRv.setVisibility(View.VISIBLE);
    }

    private void dismissSuggestions() {
        mBinding.suggestionProductsRv.setVisibility(View.GONE);
    }

}
