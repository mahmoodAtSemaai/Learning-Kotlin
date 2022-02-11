package com.webkul.mobikul.odoo.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.databinding.ActivityUserApprovalBinding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.IntentHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.BaseResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_approval.*

class UserApprovalActivity : BaseActivity() {
    private lateinit var binding: ActivityUserApprovalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_approval)
        binding.lifecycleOwner = this
        binding.btnLogout.setOnClickListener { signOut() }
        binding.llWhatsapp.setOnClickListener { IntentHelper.goToWhatsApp(this) }
    }

    private fun signOut() {
        ApiConnection.signOut(this).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<BaseResponse?>(this) {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    AlertDialogHelper.showDefaultProgressDialog(this@UserApprovalActivity)
                }

                override fun onNext(baseResponse: BaseResponse) {
                    super.onNext(baseResponse)
                    if (baseResponse.isSuccess) {
                        StyleableToast.makeText(
                            this@UserApprovalActivity,
                            baseResponse.message,
                            Toast.LENGTH_SHORT,
                            R.style.GenericStyleableToast
                        ).show()
                        AppSharedPref.clearCustomerData(this@UserApprovalActivity)
                        val intent = Intent(
                            this@UserApprovalActivity,
                            SplashScreenActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        this@UserApprovalActivity.startActivity(intent)
                    } else {
                        SnackbarHelper.getSnackbar(
                            (this@UserApprovalActivity as Activity),
                            baseResponse.message,
                            Snackbar.LENGTH_LONG,
                            SnackbarHelper.SnackbarType.TYPE_WARNING
                        ).show()
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                }

                override fun onComplete() {}
            })
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}