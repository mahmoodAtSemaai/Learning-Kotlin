package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AddressRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AuthRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.TermsConditionRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
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
            gson: Gson
    ): AuthRemoteDataSource = AuthRemoteDataSource(authServices, context, gson)

    @Provides
    @Singleton
    fun addressRemoteDataSource(
            addressServices: AddressServices,
            gson: Gson
    ): AddressRemoteDataSource = AddressRemoteDataSource(addressServices, gson)

    @Provides
    @Singleton
    fun termsConditionRemoteDataSource(
            termsConditionServices: TermsConditionServices,
            gson: Gson
    ): TermsConditionRemoteDataSource = TermsConditionRemoteDataSource(termsConditionServices, gson)

    @Provides
    @Singleton
    fun authenticationRemoteDataSource(
            authenticationServices: AuthenticationServices,
            gson: Gson
    ): AuthenticationRemoteDataSource = AuthenticationRemoteDataSource(authenticationServices, gson)

    @Provides
    @Singleton
    fun splashRemoteDataSource(
            splashServices: SplashServices,
            gson: Gson
    ): SplashRemoteDataSource = SplashRemoteDataSource(splashServices, gson)

    @Provides
    @Singleton
    fun homeRemoteDataSource(
            homePageServices: HomePageServices,
            gson: Gson
    ): HomePageRemoteDataSource = HomePageRemoteDataSource(homePageServices, gson)

}