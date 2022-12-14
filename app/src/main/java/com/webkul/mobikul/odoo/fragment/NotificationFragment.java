package com.webkul.mobikul.odoo.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.extra.NotificationMessageAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.database.SaveData;
import com.webkul.mobikul.odoo.databinding.FragmentNotificationBinding;
import com.webkul.mobikul.odoo.handler.generic.EmptyFragmentHandler;
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.notification.NotificationMessagesResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TYPE;

import org.greenrobot.eventbus.EventBus;


public class NotificationFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "NotificationFragment";
    private FragmentNotificationBinding mBinding;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.hideKeyboard(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApiConnection.getNotificationMessages(getContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<NotificationMessagesResponse>(getContext()) {
            @Override
            public void onNext(@NonNull NotificationMessagesResponse notificationMessagesResponse) {
                super.onNext(notificationMessagesResponse);
                if (notificationMessagesResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(getContext());
                            Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                            startActivity(i);
                        }
                    });
                } else {

                    mBinding.setData(notificationMessagesResponse);
                    if (notificationMessagesResponse.getAllNotificationMessages().isEmpty()) {

                        mBinding.emptyLayout.setTitle(getString(R.string.empty_notification));
                        mBinding.emptyLayout.setSubtitle(getString(R.string.visit_later_to_check_your_notification));
                        mBinding.emptyLayout.setEmptyImage(R.drawable.ic_vector_empty_notification);
                        mBinding.emptyLayout.setHideContinueShoppingBtn(false);
                        mBinding.emptyLayout.setHandler(new EmptyFragmentHandler(getContext()));

                    } else {

                        mBinding.progressBar.setVisibility(View.GONE);
                        new SaveData(getActivity(), notificationMessagesResponse);
                        mBinding.notificationListRv.setAdapter(new NotificationMessageAdapter(getContext(), notificationMessagesResponse.getAllNotificationMessages()));
                        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                            @Override
                            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                                System.out.println("Moving ===== ");
                                return false;
                            }

                            @Override
                            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                                ApiConnection.deleteNotificationMessage(getContext(), notificationMessagesResponse.getAllNotificationMessages().get(viewHolder.getAdapterPosition()).getId())
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(getContext()) {
                                    @Override
                                    public void onNext(@NonNull BaseResponse baseResponse) {
                                        super.onNext(baseResponse);
                                        if (baseResponse.isAccessDenied()){
                                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                    AppSharedPref.clearCustomerData(getContext());
                                                    Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                                                    startActivity(i);
                                                }
                                            });
                                        }else {

                                            if (baseResponse.isSuccess()) {
                                                notificationMessagesResponse.getAllNotificationMessages().remove(viewHolder.getAdapterPosition());
                                                mBinding.notificationListRv.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                                new SaveData(getActivity(), notificationMessagesResponse);
                                            } else {
                                                mBinding.notificationListRv.getAdapter().notifyDataSetChanged();
                                                SnackbarHelper.getSnackbar(getActivity(), getString(R.string.error_something_went_wrong), Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        super.onError(t);
                                        mBinding.notificationListRv.getAdapter().notifyDataSetChanged();

                                    }
                                });
                            }
                        });
                        swipeToDismissTouchHelper.attachToRecyclerView(mBinding.notificationListRv);
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(getContext());
        EventBus.getDefault().post(FragmentNotifier.HomeActivityFragments.NOTIFICATION_FRAGMENT);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(getContext());
    }

    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }
}
