package com.webkul.mobikul.odoo.adapter.extra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.ItemNotificationMessageBinding;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.notification.NotificationMessageData;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

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

public class NotificationMessageAdapter extends RecyclerView.Adapter<NotificationMessageAdapter.ViewHolder> {
    private final Context mContext;
    private final List<NotificationMessageData> mAllNotificationMessages;

    public NotificationMessageAdapter(Context context, List<NotificationMessageData> allNotificationMessages) {
        mContext = context;
        mAllNotificationMessages = allNotificationMessages;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_notification_message, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setData(mAllNotificationMessages.get(position));
        holder.mBinding.notificationBellToggle.setOnClickListener(v -> {
            ApiConnection.updateNotificationMessage(mContext, mAllNotificationMessages.get(position).getId(), !holder.mBinding.notificationBellToggle.isChecked()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<BaseResponse>(mContext) {
                        @Override
                        public void onNext(@NonNull BaseResponse baseResponse) {
                            super.onNext(baseResponse);
                            if (baseResponse.isAccessDenied()) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(mContext);
                                        Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity) mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            }
                        }
                    });
        });
        NotificationMessageData notificationTemporaryData = mAllNotificationMessages.get(position);
        holder.mBinding.cardView.setOnClickListener(v -> {
            AnalyticsImpl.INSTANCE.trackNotificationClicked(notificationTemporaryData.getId(),
                    notificationTemporaryData.getCreateDate(),
                    notificationTemporaryData.getBanner(),
                    notificationTemporaryData.getBody(),
                    notificationTemporaryData.getTitle());
        });
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mAllNotificationMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationMessageBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
