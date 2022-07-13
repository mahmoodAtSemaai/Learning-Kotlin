package com.webkul.mobikul.odoo.fragment;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.helper.ImageHelper.encodeImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.databinding.FragmentAccountBinding;
import com.webkul.mobikul.odoo.dialog_frag.ProfilePictureDialogFragment;
import com.webkul.mobikul.odoo.handler.customer.AccountFragmentHandler;
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.ImageHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.ReferralResponse;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.request.SaveCustomerDetailRequest;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AccountFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "AccountFragment";
    public FragmentAccountBinding binding;
    public String referralCode;
    NavController navController;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        binding.customerProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppSharedPref.getCustomerProfileImage(getContext()).trim().isEmpty())
                    showEnlargedProfileImage();
            }
        });
        return binding.getRoot();
    }

    public void showEnlargedProfileImage() {
        ProfilePictureDialogFragment dialogFragment = new ProfilePictureDialogFragment();
        dialogFragment.show(getFragmentManager(), getTag());
    }

    //For Enlarge a view using a zoom animation for profile picture
/*    private void zoomImageFromThumb(final View thumbView) {
        int mShortAnimationDuration = 1000;
        final Animator[] mCurrentAnimator = {null};
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator[0] != null) {
            mCurrentAnimator[0].cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = mBinding.expandedImage;
        ImageHelper.load(expandedImageView, AppSharedPref.getCustomerProfileImage(getContext()), R.drawable.com_facebook_profile_picture_blank_square, null, true, null);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        mBinding.getRoot().findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator[0] = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator[0] = null;
            }
        });
        set.start();
        mCurrentAnimator[0] = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator[0] != null) {
                    mCurrentAnimator[0].cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator[0] = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator[0] = null;
                    }
                });
                set.start();
                mCurrentAnimator[0] = set;
            }
        });
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setWishlistEnabled(AppSharedPref.isAllowedWishlist(getActivity()));
        Helper.hideKeyboard(getContext());
        navController = Navigation.findNavController(view);

        binding.allOrdersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_accountFragment_to_orderListFragment);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setHandler(new AccountFragmentHandler(getContext(), this));
        hitApiForReferralCode();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.setCustomerName(AppSharedPref.getCustomerName(getContext()));
        binding.setIsEmailVerified(AppSharedPref.isEmailVerified(getContext()));
    }

    public void uploadFile(final Uri selectedImageUri, boolean isFromCropImage) {

        String filePath = "";
        if (!isFromCropImage) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContext().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            if (cursor == null) {
                SnackbarHelper.getSnackbar((Activity) getContext(), getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                return;
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            filePath = selectedImageUri.getPath();
        }
        SaveCustomerDetailRequest saveCustomerDetailRequest;
        if (binding.getHandler().isRequestForProfileImage()) {
            saveCustomerDetailRequest = new SaveCustomerDetailRequest(null, null, encodeImage(filePath), null);
        } else {
            saveCustomerDetailRequest = new SaveCustomerDetailRequest(null, null, null, encodeImage(filePath));
        }


        if (!TextUtils.isEmpty(filePath)) {
            ApiConnection.saveCustomerDetails(getContext(), saveCustomerDetailRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SaveCustomerDetailResponse>(getContext()) {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(getContext());
                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull SaveCustomerDetailResponse saveCustomerDetailResponse) {
                    super.onNext(saveCustomerDetailResponse);
                    if (saveCustomerDetailResponse.isAccessDenied()) {
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
                        if (saveCustomerDetailResponse.isSuccess()) {
                            binding.executePendingBindings();
                            SnackbarHelper.getSnackbar((Activity) getContext(), saveCustomerDetailResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
                            if (binding.getHandler().isRequestForProfileImage()) {
                                AppSharedPref.setCustomerProfileImage(getContext(), saveCustomerDetailResponse.getCustomerProfileImage());
                                ImageHelper.load(binding.customerProfileImage, saveCustomerDetailResponse.getCustomerProfileImage(), R.drawable.ic_men_avatar, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.PROFILE_PIC_GENERIC);

                            } else {
                                AppSharedPref.setCustomerBannerImage(getContext(), saveCustomerDetailResponse.getCustomerBannerImage());
                                ImageHelper.load(binding.profileBanner, saveCustomerDetailResponse.getCustomerBannerImage(), null, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.BANNER_SIZE_LARGE);

                            }
                        } else {
                            AlertDialogHelper.showDefaultWarningDialog(getContext(), getContext().getString(R.string.error_something_went_wrong), saveCustomerDetailResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            SnackbarHelper.getSnackbar((Activity) getContext(), getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }


    }


    public void updateProfileImageAfterDelete() {
        if (binding.getHandler().isRequestForProfileImage()) {
            ImageHelper.load(binding.customerProfileImage, "", R.drawable.ic_men_avatar, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.PROFILE_PIC_GENERIC);

        } else {
            ImageHelper.load(binding.profileBanner, "", null, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.BANNER_SIZE_LARGE);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(getContext());
        EventBus.getDefault().post(FragmentNotifier.HomeActivityFragments.ACCOUNT_FRAGMENT);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(getContext());
    }

    @NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@NonNull String title) {

    }

    public void hitApiForReferralCode() {

        ApiConnection.getReferralCode(getContext(), AppSharedPref.getCustomerId(getContext())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<ReferralResponse>(getContext()) {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull ReferralResponse response) {
                super.onNext(response);
                handleReferralResponse(response);
            }
        });
    }

    public void handleReferralResponse(ReferralResponse response) {
        if (response.getStatus() == ApplicationConstant.SUCCESS || response.getStatus() == ApplicationConstant.CREATED) {
            showReferralCode(response.getReferralCode());
        } else if (response.getStatus() == ApplicationConstant.NOT_FOUND) {
            hitApiToGenerateReferralCode(AppSharedPref.getCustomerId(getContext()));
        } else {
            SnackbarHelper.getSnackbar((Activity) getContext(), response.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }
    }

    public void hitApiToGenerateReferralCode(String userId) {
        ApiConnection.generateReferralCode(getContext(), userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<ReferralResponse>(getContext()) {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull ReferralResponse response) {
                super.onNext(response);
                handleReferralResponse(response);
            }
        });
    }

    public void showReferralCode(String code) {
        AppSharedPref.setReferralCode(getContext(), code);
        String message = getContext().getString(R.string.copy_referral_code) + ": " + code;
        binding.referralCode.setText(message);
    }

}
