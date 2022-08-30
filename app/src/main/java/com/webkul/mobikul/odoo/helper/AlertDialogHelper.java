package com.webkul.mobikul.odoo.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.BaseLocationActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by shubham.agarwal on 9/2/17. @Webkul Software Pvt. Ltd
 */

public class AlertDialogHelper {

    @SuppressWarnings("unused")
    private static final String TAG = "AlertDialogHelper";

    public static void showDefaultProgressDialog(Context context) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            ((BaseActivity) context).mSweetAlertDialog.setTitleText(context.getString(R.string.please_wait));
            ((BaseActivity) context).mSweetAlertDialog.getProgressHelper().setBarColor(ColorHelper.getColor(context, R.attr.colorAccent));
            ((BaseActivity) context).mSweetAlertDialog.setCancelable(false);
            ((BaseActivity) context).mSweetAlertDialog.show();
        }
    }

    public static void showProgressDialogWithText(Context context, String titleText) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            ((BaseActivity) context).mSweetAlertDialog.setTitleText(titleText);
            ((BaseActivity) context).mSweetAlertDialog.getProgressHelper().setBarColor(ColorHelper.getColor(context, R.attr.colorAccent));
            ((BaseActivity) context).mSweetAlertDialog.setCancelable(false);
            ((BaseActivity) context).mSweetAlertDialog.show();
        }
    }

    @SuppressWarnings("unused")
    public static SweetAlertDialog getAlertDialog(Context context, int alertType, String titleText, String contentText, boolean isCancelable, boolean isShow) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, alertType);
        sweetAlertDialog.setTitleText(titleText);
        if (!contentText.isEmpty())
            sweetAlertDialog.setContentText(contentText);
        if (alertType == SweetAlertDialog.PROGRESS_TYPE)
            sweetAlertDialog.getProgressHelper().setBarColor(ColorHelper.getColor(context, R.attr.colorAccent));
        sweetAlertDialog.setCancelable(isCancelable);
        if (isShow) {
            sweetAlertDialog.show();
        }
        return sweetAlertDialog;
    }

    public static void dismiss(Context context) {
        if (context instanceof BaseActivity) {
            dismiss(((BaseActivity) context).mSweetAlertDialog);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static void dismiss(SweetAlertDialog sweetAlertDialog) {
        if (sweetAlertDialog != null) {
            sweetAlertDialog.dismiss();
        }
    }

    public static void showDefaultNormalDialog(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showDefaultSuccessDialog(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showDefaultErrorDialog(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showDefaultErrorDialog(Context context, String title, String message, SweetAlertDialog.OnSweetClickListener listener) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(listener).show();
    }

    @SuppressWarnings("unused")
    public static void showDefaultWarningDialog(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showDefaultSuccessOneLinerDialog(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setContentText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showDefaultSuccessOneLinerDialog(Context context,String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showDefaultErrorOneLinerDialog(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .setConfirmClickListener(Dialog::dismiss).show();
    }

    public static void showPermissionDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    public static void showDefaultWarningDialogWithDismissListener(Context context, String title, String message, SweetAlertDialog.OnSweetClickListener listener) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(listener).show();
    }

    public static void showDefaultWarningDialogWithDismissListener(Context context, String title, String message, SweetAlertDialog.OnSweetClickListener confirmListener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(confirmListener).show();
    }

}
