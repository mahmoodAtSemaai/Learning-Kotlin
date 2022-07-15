package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.utils.DeeplinkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeeplinkModule {

    @Provides
    @Singleton
    fun provideDeeplinkManager() = DeeplinkManager()
}