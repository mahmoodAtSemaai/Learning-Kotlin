package com.webkul.mobikul.odoo.core.di

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.data.localSource.localDataSource.*
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.*
import com.webkul.mobikul.odoo.data.repository.*
import com.webkul.mobikul.odoo.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    //TODO-> Remove this
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
    fun providesOrderRepository(
        remoteDataSource: OrderRemoteDataSource
    ): OrderRepository = OrderRepositoryImpl(remoteDataSource)

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
    fun providesSellerTermsConditionRepository(
        remoteDataSource: SellerTermsConditionRemoteDataSource
    ): SellerTermsConditionRepository = SellerTermsConditionRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesAuthenticationPasswordRepository(
        remoteDataSource: AuthenticationPasswordRemoteDataSource
    ): AuthenticationPasswordRepository =
        AuthenticationPasswordRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesAuthenticationOtpRepository(
        remoteDataSource: AuthenticationOtpRemoteDataSource
    ): AuthenticationOtpRepository = AuthenticationOtpRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesOtpRepository(
        remoteDataSource: OtpRemoteDataSource
    ): OtpRepository = OtpRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesPhoneNumberRepository(
        remoteDataSource: PhoneNumberRemoteDataSource
    ): PhoneNumberRepository = PhoneNumberRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideShippingMethodRepository(
        remoteDataSource: ShippingMethodRemoteDataSource
    ): ShippingMethodRepository = ShippingMethodRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providePaymentAcquireRepository(
        remoteDataSource: PaymentAcquireRemoteDataSource
    ): PaymentAcquireRepository = PaymentAcquireRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providePaymentStatusRepository(
            remoteDataSource: PaymentStatusRemoteDataSource
    ): PaymentStatusRepository = PaymentStatusRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesHomeDataRepository(
        remoteDataSource: HomeRemoteDataSource,
        localDataSource: HomeLocalDataSource
    ): HomeDataRepository = HomeDataRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun providesSplashDataRepository(
        remoteDataSource: SplashRemoteDataSource,
        localDataSource: SplashLocalDataSource
    ): SplashDataRepository =
        SplashDataRepositoryImpl(remoteDataSource, localDataSource)

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
    fun providePaymentProcessingRepository(
        remoteDataSource: PaymentProcessingRemoteDataSource
    ) : PaymentProcessingRepository = PaymentProcessingRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providePaymentTransferInstructionRepository(
            remoteDataSource: PaymentTransferInstructionRemoteDataSource
    ) : PaymentTransferInstructionRepository = PaymentTransferInstructionRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesSignUpAuthRepository(
        remoteDataSource: SignUpAuthRemoteDataSource
    ): SignUpAuthRepository =
        SignUpAuthRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesOnboardingStageRepository(remoteDataSource: OnboardingStageRemoteDataSource): OnboardingStageRepository =
        OnboardingStageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesUserOnboardingStageRepository(remoteDataSource: UserOnboardingStageRemoteDataSource): UserOnboardingStageRepository =
        UserOnboardingStageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesCustomerGroupRepository(
        remoteDataSource: CustomerGroupRemoteDataSource
    ): CustomerGroupRepository =
        CustomerGroupRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providesUserCustomerGroupRepository(
        remoteDataSource: UserCustomerGroupRemoteDataSource
    ): UserCustomerGroupRepository =
        UserCustomerGroupRepositoryImpl(remoteDataSource)

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
        remoteDataSource: UserLocationRemoteDataSource
    ): UserLocationRepository =
        UserLocationRepositoryImpl(remoteDataSource)

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
        remoteDataSource: FCMTokenRemoteDataSource
    ): FCMTokenRepository = FCMTokenRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        localDataSource: OnboardingLocalDataSource
    ): OnboardingRepository = OnboardingRepositoryImpl(localDataSource)

    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        userLocalDataSource: UserLocalDataSource
    ): UserRepository = UserRepositoryImpl(userRemoteDataSource, userLocalDataSource)

    @Provides
    @Singleton
    fun provideAppConfigRepository(
        appConfigLocalDataSource: AppConfigLocalDataSource
    ): AppConfigRepository = AppConfigRepositoryImpl(appConfigLocalDataSource)



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