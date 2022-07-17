package com.webkul.mobikul.odoo.ui.routing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SplashScreenActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivityRoutingBinding
import com.webkul.mobikul.odoo.ui.splash.SplashScreenActivityV1
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper

class RoutingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        routeToSplashActivity()
    }

    private fun routeToSplashActivity() {
        if (FirebaseRemoteConfigHelper.splashRevampEnabled) {
            launchSplashV1Activity()
        } else {
            launchSplashActivity()
        }
        finish()
    }

    private fun launchSplashActivity() {
        Intent(this, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    private fun launchSplashV1Activity() {
        Intent(this, SplashScreenActivityV1::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }
}