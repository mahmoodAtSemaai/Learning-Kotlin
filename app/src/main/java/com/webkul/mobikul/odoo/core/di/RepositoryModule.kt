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
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.AuthenticationRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.HomePageRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.SplashRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.repo.AuthenticationRepositoryImpl
import com.webkul.mobikul.odoo.features.authentication.data.repo.HomePageRepositoryImpl
import com.webkul.mobikul.odoo.features.authentication.data.repo.SplashPageRepositoryImpl
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.SplashPageRepository
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

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        remoteDataSource: AuthenticationRemoteDataSource
    ): AuthenticationRepository = AuthenticationRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideSplashPageRepository(
        remoteDataSource: SplashRemoteDataSource
    ): SplashPageRepository = SplashPageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideHomePageRepository(
        remoteDataSource: HomePageRemoteDataSource
    ): HomePageRepository = HomePageRepositoryImpl(remoteDataSource)

}