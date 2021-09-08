package com.webkul.mobikul.odoo.dialog_frag;

import android.annotation.TargetApi;
import androidx.databinding.DataBindingUtil;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.FragmentFingerprintAuthenticationDialogBinding;
import com.webkul.mobikul.odoo.handler.customer.LoginFragmentHandler;
import com.webkul.mobikul.odoo.handler.fingerprint.FingerprintHandler;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment implements FingerprintHandler.Callback {

    //    Button mCancelButton;
//    ImageView iv;
//    TextView tv;
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintHandler mFingerprintHandler;
    private FragmentFingerprintAuthenticationDialogBinding mBinding;
    private Runnable mResetErrorTextRunnable = new Runnable() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void run() {
            mBinding.fingerprintText.setText("Touch Sencor");
            mBinding.fingerprintIcon.setImageResource(R.drawable.ic_fp_40px);
        }
    };

    public static FingerprintAuthenticationDialogFragment newInstance() {
        return new FingerprintAuthenticationDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(android.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fingerprint_authentication_dialog, container, false);
//        View v = inflater.inflate(R.layout.fragment_fingerprint_authentication_dialog, container, false);
//        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

//        iv = v.findViewById(R.id.fingerprint_icon);
//        tv = v.findViewById(R.id.fingerprint_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fm = getActivity().getSystemService(FingerprintManager.class);
            mFingerprintHandler = new FingerprintHandler(
                    fm,
                    getContext(),
                    null, this);

            mFingerprintHandler.startAuth(fm, mCryptoObject);

        }


        return mBinding.getRoot();
    }

    /**
     * Sets the crypto object to be passed in when authenticating with fingerprint.
     */
    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    @Override
    public void onAuthenticated() {
        dismiss();
        LoginFragmentHandler loginFragmentHandler = new LoginFragmentHandler(getContext(), null);
        loginFragmentHandler.fingerprintLogin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mFingerprintHandler.stopListening();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onError() {
        showError("Some error occurred. Try again.");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showError(CharSequence error) {
        mBinding.fingerprintIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mBinding.fingerprintText.setText(error);
        mBinding.fingerprintText.removeCallbacks(mResetErrorTextRunnable);
        mBinding.fingerprintText.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

}
