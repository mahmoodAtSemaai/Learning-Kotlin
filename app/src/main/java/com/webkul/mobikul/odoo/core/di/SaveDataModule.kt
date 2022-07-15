package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.SaveData
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SaveDataModule {

    @Provides
    @Singleton
    fun provideSaveData(sqlLiteDbHelper: SqlLiteDbHelper): SaveData = SaveData(sqlLiteDbHelper)
}