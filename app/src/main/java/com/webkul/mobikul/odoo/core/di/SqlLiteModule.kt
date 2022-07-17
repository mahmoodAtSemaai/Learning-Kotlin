package com.webkul.mobikul.odoo.core.di

import android.content.Context
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SqlLiteModule {

    @Provides
    @Singleton
    fun provideSqlLiteDbHelper(@ApplicationContext context: Context) = SqlLiteDbHelper(context)

}