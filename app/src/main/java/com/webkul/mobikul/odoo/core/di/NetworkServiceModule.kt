package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.features.auth.data.remoteSource.AuthServices
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.SignUpServices
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
    fun provideSignUpServices(retrofit: Retrofit): SignUpServices =
        retrofit.create(SignUpServices::class.java)

}