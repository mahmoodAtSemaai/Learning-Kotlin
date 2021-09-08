package com.webkul.mobikul.marketplace.odoo.handler;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.odoo.adapter.customer.SignUpHandler;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentSignUpBinding;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpData;
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Webkul Software.
 * <p>
 * Android Studio version 3.0+
 * Java version 1.7+
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul.marketplace.odoo.handler
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class SingUpMarketplaceHandler extends SignUpHandler {


    public SingUpMarketplaceHandler(Context context, SignUpData data, FragmentSignUpBinding mBinding) {
        super(context, data, mBinding);
    }

    @Override
    public void viewMarketplaceTermNCond() {
        super.viewMarketplaceTermNCond();
        Log.d("TAG", "SingUpMarketplaceHandler viewMarketplaceTermNCond : ");
        MarketplaceApiConnection.getSellerTerms(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<TermAndConditionResponse>(mContext) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull TermAndConditionResponse termAndConditionResponse) {
                super.onNext(termAndConditionResponse);
                if (termAndConditionResponse.isSuccess()) {
                    LinearLayout addedLayout = new LinearLayout(mContext);
                    addedLayout.setOrientation(LinearLayout.VERTICAL);
                    WebView myWebView = new WebView(mContext);

                    Helper.enableDarkModeInWebView(mContext, myWebView);

                    String mime = "text/html";
                    String encoding = "utf-8";
                    myWebView.loadDataWithBaseURL("", TextUtils.isEmpty(termAndConditionResponse.getTermsAndConditions()) ? mContext.getString(R.string.no_terms_and_conditions_to_display) :termAndConditionResponse.getTermsAndConditions() , mime, encoding, "");
                    addedLayout.addView(myWebView);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setView(addedLayout);
                    dialog.show();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }

            @Override
            public void onComplete() {

            }
        });


    }
}
