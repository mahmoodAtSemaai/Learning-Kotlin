package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.local.SaveData
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.*
import com.webkul.mobikul.odoo.data.repository.*
import com.webkul.mobikul.odoo.domain.repository.*
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.AuthenticationRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.HomePageRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.SplashRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.repo.AuthenticationRepositoryImpl
import com.webkul.mobikul.odoo.features.authentication.data.repo.HomePageRepositoryImpl
import com.webkul.mobikul.odoo.features.authentication.data.repo.SplashPageRepositoryImpl
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.SplashPageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesAuthRepository(
        remoteDataSource: AuthRemoteDataSource,
        appPreferences: AppPreferences
    ): AuthRepository = AuthRepositoryImpl(remoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun providesAddressRepository(
        remoteDataSource: AddressRemoteDataSource
    ): AddressRepository = AddressRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesCountryStateRepository(
        remoteDataSource: CountryStateRemoteDataSource
    ): CountryStateRepository = CountryStateRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesTermsConditionRepository(
        remoteDataSource: TermsConditionRemoteDataSource
    ): TermsConditionRepository = TermsConditionRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        remoteDataSource: AuthenticationRemoteDataSource
    ): AuthenticationRepository = AuthenticationRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideSplashPageRepository(
        remoteDataSource: SplashRemoteDataSource
    ): SplashPageRepository = SplashPageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideHomePageRepository(
        remoteDataSource: HomePageRemoteDataSource,
        appPreferences: AppPreferences
    ): HomePageRepository = HomePageRepositoryImpl(remoteDataSource, appPreferences)


    @Provides
    @Singleton
    fun providesHomeDataRepository(
        remoteDataSource: HomeRemoteDataSource,
        saveData: SaveData,
        appPreferences: AppPreferences
    ): HomeDataRepository = HomeDataRepositoryImpl(remoteDataSource, saveData, appPreferences)

    @Provides
    @Singleton
    fun providesSplashDataRepository(
        remoteDataSource: SplashPageRemoteDataSource,
        saveData: SaveData,
        appPreferences: AppPreferences,
        localeManager: LocaleManager,
        resourcesProvider: ResourcesProvider
    ): SplashDataRepository =
        SplashDataRepositoryImpl(
            remoteDataSource,
            saveData,
            appPreferences,
            localeManager,
            resourcesProvider
        )

    @Provides
    @Singleton
    fun providesUserAnalyticsDataRepository(
        remoteDataSource: UserAnalyticsRemoteDataSource
    ): UserAnalyticsRepository = UserAnalyticsRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesChatChannelRepository(
        remoteDataSource: ChatChannelRemoteDataSource
    ): ChatChannelRepository = ChatChannelRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesSignUpAuthRepository(
        appPreferences: AppPreferences,
        remoteDataSource: SignUpAuthRemoteDataSource
    ): SignUpAuthRepository =
        SignUpAuthRepositoryImpl(appPreferences, remoteDataSource)

    @Provides
    @Singleton
    fun providesOnboardingStageRepository(remoteDataSource: OnboardingStageRemoteDataSource): OnboardingStageRepository =
        OnboardingStageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesCustomerGroupRepository(
        appPreferences: AppPreferences,
        remoteDataSource: CustomerGroupRemoteDataSource
    ): CustomerGroupRepository =
        CustomerGroupRepositoryImpl(remoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun providesUserDetailsRepository(
        appPreferences: AppPreferences,
        remoteDataSource: UserDetailsRemoteDataSource,
        resourcesProvider: ResourcesProvider
    ): UserDetailsRepository =
        UserDetailsRepositoryImpl(remoteDataSource, appPreferences, resourcesProvider)

    @Provides
    @Singleton
    fun providesReferralCodeRepository(remoteDataSource: ReferralCodeRemoteDataSource): ReferralCodeRepository =
        ReferralCodeRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesUserAddressRepository(
        remoteDataSource: UserAddressRemoteDataSource
    ): UserAddressRepository = UserAddressRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesUserLocationRepository(
        remoteDataSource: UserLocationRemoteDataSource,
        appPreferences: AppPreferences
    ): UserLocationRepository =
        UserLocationRepositoryImpl(remoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun providesProvinceRepository(remoteDataSource: ProvinceRemoteDataSource): ProvinceRepository =
        ProvinceRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesDistrictRepository(remoteDataSource: DistrictRemoteDataSource): DistrictRepository =
        DistrictRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesSubDistrictRepository(remoteDataSource: SubDistrictRemoteDataSource): SubDistrictRepository =
        SubDistrictRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesVillageRepository(remoteDataSource: VillageRemoteDataSource): VillageRepository =
        VillageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesFCMTokenRepository(
        remoteDataSource: FCMTokenRemoteDataSource,
        appPreferences: AppPreferences
    ): FCMTokenRepository = FCMTokenRepositoryImpl(remoteDataSource, appPreferences)


    @Provides
    @Singleton
    fun providesUpdateCartRepository(
        remoteDataSource: CartRemoteDataSource,
        appPreferences: AppPreferences
    ): CartRepository = CartRepositoryImpl(remoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun providesWishListRepository(
        remoteDataSource: WishListRemoteDataSource,
        appPreferences : AppPreferences
    ): WishListRepository = WishListRepositoryImpl(remoteDataSource,appPreferences)

    @Provides
    @Singleton
    fun provideUserDetailRepository(
        userDetailRemoteDataSource: UserDetailRemoteDataSource,
        appPreferences: AppPreferences
    ): UserDetailRepository = UserDetailRepositoryImpl(userDetailRemoteDataSource, appPreferences)

    @Provides
    @Singleton
    fun provideProductCategoriesRepository(
        categoriesRemoteDataSource: CategoriesRemoteDataSource
    ): CategoriesRepository = CategoriesRepositoryImpl(categoriesRemoteDataSource)

    @Provides
    @Singleton
    fun provideBannerRepository(
        bannerRemoteDataSource: BannerRemoteDataSource
    ): BannerRepository = BannerRepositoryImpl(bannerRemoteDataSource)

    @Provides
    @Singleton
    fun provideSellerRepository(
        sellerRemoteDataSource: SellerRemoteDataSource
    ): SellerRepository = SellerRepositoryImpl(sellerRemoteDataSource)

    @Provides
    @Singleton
    fun provideSellerProductRepository(
        sellerProductsRemoteDataSource: SellerProductsRemoteDataSource
    ): SellerProductRepository = SellerProductRepositoryImpl(sellerProductsRemoteDataSource)

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchRemoteDataSource: SearchRemoteDataSource
    ): SearchRepository = SearchRepositoryImpl(searchRemoteDataSource)

    @Provides
    @Singleton
    fun provideProductDetailsRepository(
        productDetailsRemoteDataSource: ProductDetailsRemoteDataSource
    ): ProductDetailsRepository = ProductDetailsRepositoryImpl(productDetailsRemoteDataSource)

    @Provides
    @Singleton
    fun provideProductSellersRepository(
        productSellersRemoteDataSource: ProductSellersRemoteDataSource
    ): ProductSellersRepository = ProductSellersRepositoryImpl(productSellersRemoteDataSource)

    @Provides
    @Singleton
    fun provideCategoryProductRepository(
        productSellersRemoteDataSource: CategoryProductRemoteDataSource
    ): CategoryProductRepository = CategoryProductRepositoryImpl(productSellersRemoteDataSource)

    @Provides
    @Singleton
    fun provideBagItemsCountRepository(
        appPreferences: AppPreferences
    ): BagItemsCountRepository = BagItemsCountRepositoryImpl(appPreferences)

   
}