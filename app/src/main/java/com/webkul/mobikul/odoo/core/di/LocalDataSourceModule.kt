package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.localSource.localDataSource.*
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Provides
    @Singleton
    fun splashLocalDataSource(
        sqlLiteDbHelper: SqlLiteDbHelper,
        appPreferences: AppPreferences,
        localeManager: LocaleManager,
        resourcesProvider: ResourcesProvider
    ): SplashLocalDataSource =
        SplashLocalDataSource(sqlLiteDbHelper, appPreferences, localeManager, resourcesProvider)

    @Provides
    @Singleton
    fun homeLocalDataSource(
        sqlLiteDbHelper: SqlLiteDbHelper
    ): HomeLocalDataSource = HomeLocalDataSource(sqlLiteDbHelper)

    @Provides
    @Singleton
    fun onboardingLocalDataSource(
        resourcesProvider: ResourcesProvider
    ): OnboardingLocalDataSource = OnboardingLocalDataSource(resourcesProvider)

    @Provides
    @Singleton
    fun appConfigLocalDataSource(
        appPreferences: AppPreferences
    ): AppConfigLocalDataSource = AppConfigLocalDataSource(appPreferences)

    @Provides
    @Singleton
    fun userLocalDataSource(
        appPreferences: AppPreferences
    ): UserLocalDataSource = UserLocalDataSource(appPreferences)

}