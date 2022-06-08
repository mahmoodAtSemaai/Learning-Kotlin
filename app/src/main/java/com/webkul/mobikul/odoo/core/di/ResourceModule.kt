package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {

    @Provides
    @Singleton
    fun provideResourceModule(@ApplicationContext context: Context) = ResourcesProvider(context)

}