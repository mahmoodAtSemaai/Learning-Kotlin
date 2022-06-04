package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AddressRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AuthRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.TermsConditionRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
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
            @ApplicationContext context: Context
    ): AuthRemoteDataSource = AuthRemoteDataSource(authServices, context)

    @Provides
    @Singleton
    fun addressRemoteDataSource(
            addressServices: AddressServices
    ): AddressRemoteDataSource = AddressRemoteDataSource(addressServices)

    @Provides
    @Singleton
    fun termsConditionRemoteDataSource(
            termsConditionServices: TermsConditionServices
    ): TermsConditionRemoteDataSource = TermsConditionRemoteDataSource(termsConditionServices)

}