package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.*
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.AuthenticationServices
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.HomePageServices
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.SplashServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    @Provides
    @Singleton
    fun provideAuthServices(retrofit: Retrofit): AuthServices =
            retrofit.create(AuthServices::class.java)

    @Provides
    @Singleton
    fun provideAddressServices(retrofit: Retrofit): AddressServices =
            retrofit.create(AddressServices::class.java)

    @Provides
    @Singleton
    fun provideCountryStateServices(retrofit: Retrofit): CountryStateServices =
            retrofit.create(CountryStateServices::class.java)

    @Provides
    @Singleton
    fun provideTermsConditionServices(retrofit: Retrofit): TermsConditionServices =
            retrofit.create(TermsConditionServices::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationServices(retrofit: Retrofit): AuthenticationServices =
            retrofit.create(AuthenticationServices::class.java)

    @Provides
    @Singleton
    fun provideSplashServices(retrofit: Retrofit): SplashServices =
            retrofit.create(SplashServices::class.java)

    @Provides
    @Singleton
    fun provideHomeServices(retrofit: Retrofit): HomePageServices =
            retrofit.create(HomePageServices::class.java)

    @Provides
    @Singleton
    fun provideHomeServicesV1(retrofit: Retrofit): HomeServicesV1 =
            retrofit.create(HomeServicesV1::class.java)

    @Provides
    @Singleton
    fun provideSplashServicesV1(retrofit: Retrofit): SplashServicesV1 =
            retrofit.create(SplashServicesV1::class.java)


    @Provides
    @Singleton
    fun provideChatServices(retrofit: Retrofit): ChatServices =
            retrofit.create(ChatServices::class.java)

    @Provides
    @Singleton
    fun provideFCMTokenServices(retrofit: Retrofit): FCMTokenServices =
            retrofit.create(FCMTokenServices::class.java)

}