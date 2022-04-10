package com.webkul.mobikul.odoo.activity;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.DEBOUNCE_REQUEST_TIMEOUT;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.MAX_SEARCH_HISTORY_RESULT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_QUERY;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.extra.SearchHistoryAdapter;
import com.webkul.mobikul.odoo.adapter.extra.SearchSuggestionProductAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.connection.RetrofitClient;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.database.SearchHistoryContract;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.MaterialSearchViewBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AnimationHelper;
import com.webkul.mobikul.odoo.helper.BindingAdapterUtils;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    public static final int RC_MATERIAL_SEARCH_VOICE = 42;
    public static final int RC_CAMERA_SEARCH = 1042;
    @SuppressWarnings("unused")
    private static final String TAG = "MaterialSearchView";
    private static final int MAX_VOICE_RESULTS = 1;
    public MaterialSearchViewBinding binding;
    private final Context mContext = this;
    private boolean mOpen, isFromSubmitResult;
    private CharSequence mCurrentQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.material_search_view);
    }

    private void initSearchView() {
        isFromSubmitResult = false;
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            onSubmitQuery();
            return true;
        });

        binding.seachHistoryRv.setAdapter(new SearchHistoryAdapter(mContext, getSearchHistoryList(""), ""));
        RxTextView.textChangeEvents(binding.etSearch).debounce(DEBOUNCE_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
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
                                    binding.mainProgressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onNext(@NonNull CatalogProductResponse catalogProductResponse) {
                                    super.onNext(catalogProductResponse);
                                    Log.d(TAG, "onNext: ");
                                    FirebaseAnalyticsImpl.logSearchEvent(mContext,textViewTextChangeEvent.text().toString());
                                    binding.setSearchSuggestionData(catalogProductResponse);
                                    if (binding.suggestionProductsRv.getAdapter() == null) {
                                        binding.suggestionProductsRv.setAdapter(new SearchSuggestionProductAdapter(mContext, catalogProductResponse.getProducts(), textViewTextChangeEvent.text().toString()));
                                    } else {
                                        ((SearchSuggestionProductAdapter) binding.suggestionProductsRv.getAdapter()).updateData(catalogProductResponse.getProducts(), textViewTextChangeEvent.text().toString());
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable t) {
                                    super.onError(t);
                                }

                                @Override
                                public void onComplete() {
                                    binding.mainProgressBar.setVisibility(View.GONE);
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
                ((SearchHistoryAdapter) binding.seachHistoryRv.getAdapter()).updateSearchHistory(getSearchHistoryList(charSequence.toString()), charSequence.toString());
                SearchActivity.this.onTextChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etSearch.setOnFocusChangeListener((v, hasFocus) -> {
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
        mCurrentQuery = binding.etSearch.getText();
        if (!TextUtils.isEmpty(mCurrentQuery)) {
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
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(this);
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
        binding.etSearch.setText("");
        dismissSuggestions();
//        clearFocus();
        AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.searchLayout.setVisibility(View.GONE);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationHelper.circleHideView(binding.searchBar, listenerAdapter);
        } else {
            AnimationHelper.fadeOutView(binding.searchLayout);
        }
        mOpen = false;
        //setVisibility(View.GONE);
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
        binding.etSearch.setText("");
//        mBinding.etSearch.requestFocus();

//        ((SearchHistoryAdapter) mBinding.seachHistoryRv.getAdapter()).updateSearchHistory(getSearchHistoryList(""), "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.searchLayout.setVisibility(View.VISIBLE);
            AnimationHelper.circleRevealView(binding.searchBar);
        } else {
            AnimationHelper.fadeInView(binding.searchLayout);
        }
        //setVisibility(View.VISIBLE);
        initSearchView();
        mOpen = true;
    }

    public boolean isOpen() {
        return mOpen;
    }

    private void displayVoiceButton(boolean display) {
        if (display && Helper.isVoiceAvailable(mContext)) {
            binding.actionVoice.setVisibility(View.VISIBLE);
        } else {
            binding.actionVoice.setVisibility(View.GONE);
        }
    }

    private void displayClearButton(boolean display) {
        binding.actionClear.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    private void showSuggestions() {
        binding.suggestionProductsRv.setVisibility(View.VISIBLE);
    }

    private void dismissSuggestions() {
        binding.suggestionProductsRv.setVisibility(View.GONE);
    }
}
