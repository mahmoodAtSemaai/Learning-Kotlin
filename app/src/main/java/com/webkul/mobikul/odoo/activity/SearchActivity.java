package com.webkul.mobikul.odoo.activity;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.DEBOUNCE_REQUEST_TIMEOUT;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.MAX_SEARCH_HISTORY_RESULT;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.REQUEST_TIMEOUT;
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
    private final Context context = this;
    private boolean open, isFromSubmitResult;
    private CharSequence currentQuery;

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

                            ApiConnection.getSearchResponse(context, textViewTextChangeEvent.text().toString(), 0, BuildConfig.DEFAULT_NO_OF_SEARCH_PRODUCTS)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).debounce(REQUEST_TIMEOUT, TimeUnit.SECONDS).subscribe(new CustomObserver<CatalogProductResponse>(context) {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    super.onSubscribe(d);
                                    binding.pbSearch.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onNext(@NonNull CatalogProductResponse catalogProductResponse) {
                                    super.onNext(catalogProductResponse);
                                    FirebaseAnalyticsImpl.logSearchEvent(context, textViewTextChangeEvent.text().toString());
                                    binding.setSearchSuggestionData(catalogProductResponse);
                                    if (binding.rvSearchSuggestions.getAdapter() == null) {
                                        binding.rvSearchSuggestions.setAdapter(new SearchSuggestionProductAdapter(context, catalogProductResponse.getProducts(), textViewTextChangeEvent.text().toString()));
                                    } else {
                                        ((SearchSuggestionProductAdapter) binding.rvSearchSuggestions.getAdapter()).updateData(catalogProductResponse.getProducts(), textViewTextChangeEvent.text().toString());
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable t) {
                                    super.onError(t);
                                }

                                @Override
                                public void onComplete() {
                                    binding.pbSearch.setVisibility(View.GONE);
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
                SearchActivity.this.onTextChanged(charSequence);
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
        Cursor cursor = ((BaseActivity) context).mSqLiteDatabase.query(
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

    private void deleteAllSearchHistory() {
        ((BaseActivity) context).mSqLiteDatabase.delete(SearchHistoryContract.SearchHistoryEntry.TABLE_NAME, null, null);

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
