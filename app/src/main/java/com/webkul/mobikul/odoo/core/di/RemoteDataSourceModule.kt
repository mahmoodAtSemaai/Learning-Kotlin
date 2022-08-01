package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.*
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.*
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun authRemoteDataSource(
            authServices: AuthServices,
            @ApplicationContext context: Context,
            gson: Gson,
            appPreferences: AppPreferences
    ): AuthRemoteDataSource = AuthRemoteDataSource(authServices, context, gson, appPreferences)

    @Provides
    @Singleton
    fun addressRemoteDataSource(
            addressServices: AddressServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): AddressRemoteDataSource = AddressRemoteDataSource(addressServices, gson, appPreferences)

    @Provides
    @Singleton
    fun addressCountryStateRemoteDataSource(
            countryStateServices: CountryStateServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): CountryStateRemoteDataSource = CountryStateRemoteDataSource(countryStateServices, gson, appPreferences)

    @Provides
    @Singleton
    fun termsConditionRemoteDataSource(
            termsConditionServices: TermsConditionServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): TermsConditionRemoteDataSource = TermsConditionRemoteDataSource(termsConditionServices, gson, appPreferences)

    @Provides
    @Singleton
    fun authenticationRemoteDataSource(
            authenticationServices: AuthenticationServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): AuthenticationRemoteDataSource = AuthenticationRemoteDataSource(authenticationServices, gson, appPreferences)

    @Provides
    @Singleton
    fun splashRemoteDataSource(
            splashServices: SplashServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): SplashRemoteDataSource = SplashRemoteDataSource(splashServices, gson, appPreferences)

    @Provides
    @Singleton
    fun homeRemoteDataSource(
            homePageServices: HomePageServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): HomePageRemoteDataSource = HomePageRemoteDataSource(homePageServices, gson, appPreferences)

    @Provides
    @Singleton
    fun userAnalyticsRemoteDataSource(
            splashServices: SplashServicesV1,
            @ApplicationContext context: Context,
            gson: Gson,
            appPreferences: AppPreferences
    ): UserAnalyticsRemoteDataSource = UserAnalyticsRemoteDataSource(splashServices, context, gson, appPreferences)

    @Provides
    @Singleton
    fun userHomeRemoteDataSource(
            homeServicesV1: HomeServicesV1,
            gson: Gson,
            appPreferences: AppPreferences
    ): HomeRemoteDataSource = HomeRemoteDataSource(homeServicesV1, gson, appPreferences)

    @Provides
    @Singleton
    fun userSplashRemoteDataSourceV1(
            splashServices: SplashServicesV1,
            gson: Gson,
            appPreferences: AppPreferences
    ): SplashPageRemoteDataSource = SplashPageRemoteDataSource(splashServices, gson, appPreferences)


    @Provides
    @Singleton
    fun chatChannelRemoteDataSource(
            chatServices: ChatServices,
            gson: Gson,
            appPreferences: AppPreferences
    ): ChatChannelRemoteDataSource = ChatChannelRemoteDataSource(chatServices, gson, appPreferences)

    @Provides
    @Singleton
    fun fcmTokenRemoteDataSource(
            fcmTokenServices: FCMTokenServices,
            @ApplicationContext context: Context,
            gson: Gson,
            appPreferences: AppPreferences
    ): FCMTokenRemoteDataSource = FCMTokenRemoteDataSource(fcmTokenServices, context, gson, appPreferences)
}