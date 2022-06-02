package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.logger.ILogger
import com.webkul.mobikul.odoo.core.logger.TimberLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideLogger(): ILogger = TimberLogger()
}