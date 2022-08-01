package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.local.SaveData
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.*
import com.webkul.mobikul.odoo.data.repository.*
import com.webkul.mobikul.odoo.domain.repository.*
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
    fun providesCountryStateRepository(
            remoteDataSource: CountryStateRemoteDataSource
    ): CountryStateRepository = CountryStateRepositoryImpl(remoteDataSource)

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


    @Provides
    @Singleton
    fun providesHomeDataRepository(
            remoteDataSource: HomeRemoteDataSource,
            saveData: SaveData
    ): HomeDataRepository = HomeDataRepositoryImpl(remoteDataSource, saveData)

    @Provides
    @Singleton
    fun providesSplashDataRepository(
            remoteDataSource: SplashPageRemoteDataSource,
            saveData: SaveData,
            appPreferences: AppPreferences,
            localeManager: LocaleManager
    ): SplashDataRepository =
            SplashDataRepositoryImpl(remoteDataSource, saveData, appPreferences, localeManager)

    @Provides
    @Singleton
    fun providesUserAnalyticsDataRepository(
            remoteDataSource: UserAnalyticsRemoteDataSource
    ): UserAnalyticsRepository = UserAnalyticsRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesChatChannelRepository(
            remoteDataSource: ChatChannelRemoteDataSource
    ): ChatChannelRepository = ChatChannelRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesFCMTokenRepository(
            remoteDataSource: FCMTokenRemoteDataSource,
            appPreferences: AppPreferences
    ): FCMTokenRepository = FCMTokenRepositoryImpl(remoteDataSource, appPreferences)

}