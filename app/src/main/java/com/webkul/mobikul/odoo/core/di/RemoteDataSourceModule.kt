package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.AuthServices
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
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
    fun loginRemoteDataSource(
        authServices: AuthServices,
        @ApplicationContext context: Context
    ): LoginRemoteDataSource = LoginRemoteDataSource(authServices, context)

}