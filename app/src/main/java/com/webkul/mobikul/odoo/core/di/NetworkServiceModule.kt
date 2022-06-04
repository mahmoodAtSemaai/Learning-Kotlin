package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    fun provideTermsConditionServices(retrofit: Retrofit): TermsConditionServices =
            retrofit.create(TermsConditionServices::class.java)

}