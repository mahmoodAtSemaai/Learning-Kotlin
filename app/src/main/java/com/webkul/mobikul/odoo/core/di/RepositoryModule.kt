package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.SignUpRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.data.repo.LoginRepositoryImpl
import com.webkul.mobikul.odoo.features.auth.data.repo.SignUpRepositoryImpl
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        remoteDataSource: LoginRemoteDataSource,
        appPreferences: AppPreferences
    ): LoginRepository = LoginRepositoryImpl(remoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun provideSignUpRepository(
        remoteDataSource: SignUpRemoteDataSource,
        appPreferences: AppPreferences
    ): SignUpRepository = SignUpRepositoryImpl(remoteDataSource,appPreferences)
}