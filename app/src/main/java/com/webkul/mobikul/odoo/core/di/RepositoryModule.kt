package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AddressRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AuthRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.TermsConditionRemoteDataSource
import com.webkul.mobikul.odoo.data.repository.AddressRepositoryImpl
import com.webkul.mobikul.odoo.data.repository.AuthRepositoryImpl
import com.webkul.mobikul.odoo.data.repository.TermsConditionRepositoryImpl
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.domain.repository.AuthRepository
import com.webkul.mobikul.odoo.domain.repository.TermsConditionRepository
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
    fun providesAuthRepository(
            remoteDataSource: AuthRemoteDataSource,
            appPreferences: AppPreferences
    ): AuthRepository = AuthRepositoryImpl(remoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun providesAddressRepository(
            remoteDataSource: AddressRemoteDataSource
    ): AddressRepository = AddressRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesTermsConditionRepository(
            remoteDataSource: TermsConditionRemoteDataSource
    ): TermsConditionRepository = TermsConditionRepositoryImpl(remoteDataSource)
}