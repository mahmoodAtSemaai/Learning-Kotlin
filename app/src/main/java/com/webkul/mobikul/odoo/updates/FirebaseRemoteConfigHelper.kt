package com.webkul.mobikul.odoo.updates

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.helper.Helper

object FirebaseRemoteConfigHelper {
    private const val KEY_APP_REMOTE_VERSION = "remoteAppVersion"
    private const val KEY_APP_REMOTE_CHAT = "feature_sellerChat_enabled"
    private const val KEY_AUTH_REVAMP_ENABLED = "feature_revamp_arch_auth_screen_enabled"

    private const val currentAppVersion = BuildConfig.CURRENT_APP_VERSION

    init {
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = BuildConfig.REMOTE_CONFIG_MIN_FETCH_INTERVAL_SECONDS
        }
        Firebase.remoteConfig.apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            setConfigSettingsAsync(settings)
                    .addOnSuccessListener { fetchRemoteConfig(this) }
        }
    }

    private fun fetchRemoteConfig(firebaseRemoteConfig: FirebaseRemoteConfig) {
        firebaseRemoteConfig.fetchAndActivate()
    }

    @JvmStatic
    fun init() = Unit

    private val remoteAppVersion = Firebase.remoteConfig[KEY_APP_REMOTE_VERSION].asString()
    private val remoteChatFeature = Firebase.remoteConfig[KEY_APP_REMOTE_CHAT].asBoolean()

    @JvmStatic
    val authRevampEnabled = Firebase.remoteConfig[KEY_AUTH_REVAMP_ENABLED].asBoolean()


    @JvmStatic
    val isUpdateAvailable = Helper.isRemoteVersionHigher(remoteAppVersion, currentAppVersion)

    @JvmStatic
    val isChatFeatureEnabled = remoteChatFeature
}