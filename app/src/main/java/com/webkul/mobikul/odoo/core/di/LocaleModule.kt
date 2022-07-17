package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocaleModule {

    @Provides
    @Singleton
    fun provideLocaleManager(@ApplicationContext context: Context) = LocaleManager(context)

}