package com.webkul.mobikul.odoo.core.di


import android.content.Context
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.core.utils.NetworkManager
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.domain.repository.*
import com.webkul.mobikul.odoo.domain.usecase.account.GetAccountInfoDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.*
import com.webkul.mobikul.odoo.domain.usecase.banner.BannersUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.AddToCartUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.BagItemsCountUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.CreateCartUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.GetCartIdUseCase
import com.webkul.mobikul.odoo.domain.usecase.chat.CreateChatChannelUseCase
import com.webkul.mobikul.odoo.domain.usecase.fcmToken.RegisterFCMTokenUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeLocalDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeUseCase
import com.webkul.mobikul.odoo.domain.usecase.network.IsNetworkUseCase
import com.webkul.mobikul.odoo.domain.usecase.product.*
import com.webkul.mobikul.odoo.domain.usecase.productCategories.CategoriesUseCase
import com.webkul.mobikul.odoo.domain.usecase.search.GetSearchUseCase
import com.webkul.mobikul.odoo.domain.usecase.seller.GetSellerProductUseCase
import com.webkul.mobikul.odoo.domain.usecase.seller.GetSellerUseCase
import com.webkul.mobikul.odoo.domain.usecase.session.IsValidSessionUseCase
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.*
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashLocalDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.UserDetailUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.IsWishListAllowedUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.features.authentication.domain.repo.SplashPageRepository
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.*
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.CountdownTimerUseCase
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.OnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideLogInUseCase(
        authRepository: AuthRepository
    ): LogInUseCase = LogInUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(
        authRepository: AuthRepository,
        @ApplicationContext context: Context
    ): SignUpUseCase = SignUpUseCase(authRepository, context)

    @Provides
    @Singleton
    fun provideViewPrivacyPolicyUseCase(@ApplicationContext context: Context): ViewPrivacyPolicyUseCase =
        ViewPrivacyPolicyUseCase(context)

    @Provides
    @Singleton
    fun provideBillingAddressUseCase(
        addressRepository: AddressRepository
    ): BillingAddressUseCase = BillingAddressUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideCountryStateUseCase(
        countryStateRepository: CountryStateRepository
    ): CountryStateUseCase = CountryStateUseCase(countryStateRepository)

    @Provides
    @Singleton
    fun provideViewMarketPlaceTnCUseCase(
        termsConditionRepository: TermsConditionRepository
    ): ViewMarketPlaceTnCUseCase = ViewMarketPlaceTnCUseCase(termsConditionRepository)

    @Provides
    @Singleton
    fun provideViewTnCUseCase(
        termsConditionRepository: TermsConditionRepository
    ): ViewTnCUseCase = ViewTnCUseCase(termsConditionRepository)

    @Provides
    @Singleton
    fun provideVerifyPhoneNumberUseCase(): VerifyPhoneNumberUseCase =
        VerifyPhoneNumberUseCase()

    @Provides
    @Singleton
    fun provideContinuePhoneNumberUseCase(authenticationRepository: AuthenticationRepository): ContinuePhoneNumberUseCase =
        ContinuePhoneNumberUseCase(authenticationRepository)

    @Provides
    @Singleton
    fun provideLoginPasswordUseCase(
        authenticationRepository: AuthenticationRepository,
        appPreferences: AppPreferences
    ): LoginPasswordUseCase =
        LoginPasswordUseCase(authenticationRepository, appPreferences)


    @Provides
    @Singleton
    fun provideGenerateOtpUseCase(authenticationRepository: AuthenticationRepository): GenerateOtpUseCase =
        GenerateOtpUseCase(authenticationRepository)

    @Provides
    @Singleton
    fun provideVerifyOtpUseCase(
        authenticationRepository: AuthenticationRepository,
        appPreferences: AppPreferences
    ): VerifyOtpUseCase =
        VerifyOtpUseCase(authenticationRepository, appPreferences)

    @Provides
    @Singleton
    fun provideSplashPageUseCase(splashPageRepository: SplashPageRepository): SplashPageUseCase =
        SplashPageUseCase(splashPageRepository)

    @Provides
    @Singleton
    fun provideHomePageUseCase(homePageRepository: HomePageRepository): HomePageDataUseCase =
        HomePageDataUseCase(homePageRepository)

    @Provides
    @Singleton
    fun provideCountdownTimerUseCase(): CountdownTimerUseCase = CountdownTimerUseCase()

    @Provides
    @Singleton
    fun provideOnboardingUseCase(resourcesProvider: ResourcesProvider): OnboardingUseCase =
        OnboardingUseCase(resourcesProvider)

    @Provides
    @Singleton
    fun provideCreateChatChannelUseCase(
        chatChannelRepository: ChatChannelRepository,
        appPreferences: AppPreferences
    ): CreateChatChannelUseCase =
        CreateChatChannelUseCase(chatChannelRepository, appPreferences)

    @Provides
    @Singleton
    fun provideIsFirstTimeUseCase(
        appPreferences: AppPreferences
    ): IsFirstTimeUseCase = IsFirstTimeUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideHomeDataUseCase(
        homeDataRepository: HomeDataRepository,
        homeLocalDataUseCase: HomeLocalDataUseCase
    ): HomeUseCase = HomeUseCase(homeDataRepository, homeLocalDataUseCase)

    @Provides
    @Singleton
    fun provideSplashDataUseCase(
        splashDataRepository: SplashDataRepository,
        splashLocalDataUseCase: SplashLocalDataUseCase
    ): SplashUseCase = SplashUseCase(splashDataRepository, splashLocalDataUseCase)

    @Provides
    @Singleton
    fun provideHomeLocalDataUseCase(
        sqlLiteDbHelper: SqlLiteDbHelper
    ): HomeLocalDataUseCase = HomeLocalDataUseCase(sqlLiteDbHelper)

    @Provides
    @Singleton
    fun provideSplashLocalDataUseCase(
        sqlLiteDbHelper: SqlLiteDbHelper
    ): SplashLocalDataUseCase = SplashLocalDataUseCase(sqlLiteDbHelper)

    @Provides
    @Singleton
    fun provideIsNetworkUseCase(
        networkManager: NetworkManager
    ): IsNetworkUseCase = IsNetworkUseCase(networkManager)


    @Provides
    @Singleton
    fun provideIsUserAuthorisedUseCase(
        appPreferences: AppPreferences
    ): IsUserAuthorisedUseCase = IsUserAuthorisedUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideIsUserApprovedUseCase(
        appPreferences: AppPreferences
    ): IsUserApprovedUseCase = IsUserApprovedUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideCheckChatFeatureEnabledUseCase(
        appPreferences: AppPreferences
    ): CheckChatFeatureEnabledUseCase = CheckChatFeatureEnabledUseCase(appPreferences)


    @Provides
    @Singleton
    fun provideIsWishListAllowedUseCase(
        wishListRepository: WishListRepository,
    ): IsWishListAllowedUseCase = IsWishListAllowedUseCase(wishListRepository)


    @Provides
    @Singleton
    fun provideBagItemCountUseCase(
        bagItemsCountRepository: BagItemsCountRepository,
        resourcesProvider: ResourcesProvider
    ): BagItemsCountUseCase = BagItemsCountUseCase(bagItemsCountRepository, resourcesProvider)

    @Provides
    @Singleton
    fun provideIsValidSessionUseCase(
        isUserAuthorisedUseCase: IsUserAuthorisedUseCase
    ): IsValidSessionUseCase = IsValidSessionUseCase(isUserAuthorisedUseCase)

    @Provides
    @Singleton
    fun provideGenerateSignUpOtpUseCase(signUpAuthRepository: SignUpAuthRepository): GenerateSignUpOtpUseCase =
        GenerateSignUpOtpUseCase(signUpAuthRepository)

    @Provides
    @Singleton
    fun provideVerifySignUpOtpUseCase(
        signUpAuthRepository: SignUpAuthRepository
    ): VerifySignUpOtpUseCase =
        VerifySignUpOtpUseCase(signUpAuthRepository)

    @Provides
    @Singleton
    fun provideOnboardingStageUseCase(
        onboardingStageRepository: OnboardingStageRepository
    ): OnboardingStageUseCase =
        OnboardingStageUseCase(onboardingStageRepository)

    @Provides
    @Singleton
    fun provideUserOnboardingStageUseCase(
        onboardingStageRepository: OnboardingStageRepository
    ): UserOnboardingStageUseCase =
        UserOnboardingStageUseCase(onboardingStageRepository)

    @Provides
    @Singleton
    fun provideContinueCustomerGroupUseCase(
        customerGroupRepository: CustomerGroupRepository
    ): ContinueCustomerGroupUseCase =
        ContinueCustomerGroupUseCase(customerGroupRepository)

    @Provides
    @Singleton
    fun provideCustomerGroupUseCase(
        customerGroupRepository: CustomerGroupRepository,
        resourcesProvider: ResourcesProvider,
        appPreferences: AppPreferences
    ): CustomerGroupUseCase =
        CustomerGroupUseCase(customerGroupRepository, resourcesProvider, appPreferences)

    @Provides
    @Singleton
    fun provideContinueUserDetailsUseCase(
        userDetailsRepository: UserDetailsRepository
    ): ContinueUserDetailsUseCase =
        ContinueUserDetailsUseCase(userDetailsRepository)

    @Provides
    @Singleton
    fun provideValidateReferralCodeUseCase(
        referralCodeRepository: ReferralCodeRepository
    ): ValidateReferralCodeUseCase =
        ValidateReferralCodeUseCase(referralCodeRepository)

    @Provides
    @Singleton
    fun provideContinueUserAddressUseCase(
        userAddressRepository: UserAddressRepository
    ): ContinueUserAddressUseCase =
        ContinueUserAddressUseCase(userAddressRepository)

    @Provides
    @Singleton
    fun provideContinueUserLocationUseCase(
        userLocationRepository: UserLocationRepository
    ): ContinueUserLocationUseCase =
        ContinueUserLocationUseCase(userLocationRepository)

    @Provides
    @Singleton
    fun provideProvinceDataUseCase(
        provinceRepository: ProvinceRepository
    ): ProvinceDataUseCase =
        ProvinceDataUseCase(provinceRepository)

    @Provides
    @Singleton
    fun provideDistrictDataUseCase(
        districtRepository: DistrictRepository
    ): DistrictDataUseCase =
        DistrictDataUseCase(districtRepository)

    @Provides
    @Singleton
    fun provideSubDistrictDataUseCase(
        subDistrictRepository: SubDistrictRepository
    ): SubDistrictDataUseCase =
        SubDistrictDataUseCase(subDistrictRepository)

    @Provides
    @Singleton
    fun provideVillageDataUseCase(
        villageRepository: VillageRepository
    ): VillageDataUseCase =
        VillageDataUseCase(villageRepository)

    @Provides
    @Singleton
    fun provideVerifyUserDetailsUseCase(
        appPreferences: AppPreferences,
        resourcesProvider: ResourcesProvider
    ): VerifyUserDetailsUseCase =
        VerifyUserDetailsUseCase(appPreferences, resourcesProvider)

    @Provides
    @Singleton
    fun provideGetOnboardingDataUseCase(
        appPreferences: AppPreferences,
        resourcesProvider: ResourcesProvider
    ): GetOnboardingDataUseCase =
        GetOnboardingDataUseCase(appPreferences, resourcesProvider)

    @Provides
    @Singleton
    fun provideIncompleteStageUseCase(): IncompleteStagesUseCase = IncompleteStagesUseCase()

    @Provides
    @Singleton
    fun provideNextOnboardingStageUseCase(resourcesProvider: ResourcesProvider): NextOnboardingStageUseCase =
        NextOnboardingStageUseCase(resourcesProvider)

    @Provides
    @Singleton
    fun provideGetUserDetailsViewsUseCase(
        appPreferences: AppPreferences,
        resourcesProvider: ResourcesProvider
    ): GetUserDetailsViewsUseCase =
        GetUserDetailsViewsUseCase(appPreferences, resourcesProvider)

    @Provides
    @Singleton
    fun provideGetAccountInfoDataUseCase(appPreferences: AppPreferences): GetAccountInfoDataUseCase =
        GetAccountInfoDataUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideIsProvinceAvailableUseCase(): IsProvinceAvailableUseCase =
        IsProvinceAvailableUseCase()

    @Provides
    @Singleton
    fun provideProvinceSearchUseCase(): ProvinceSearchUseCase =
        ProvinceSearchUseCase()

    @Provides
    @Singleton
    fun provideDistrictSearchUseCase(): DistrictSearchUseCase =
        DistrictSearchUseCase()

    @Provides
    @Singleton
    fun provideSubDistrictSearchUseCase(): SubDistrictSearchUseCase =
        SubDistrictSearchUseCase()

    @Provides
    @Singleton
    fun provideVillageSearchUseCase(): VillageSearchUseCase =
        VillageSearchUseCase()

    @Provides
    @Singleton
    fun provideIsUserOnboardedUseCase(
        appPreferences: AppPreferences
    ): IsUserOnboardedUseCase = IsUserOnboardedUseCase(appPreferences)

    @Provides
    @Singleton
    fun provideRegisterFCMTokenUseCase(
        fcmTokenRepository: FCMTokenRepository
    ): RegisterFCMTokenUseCase = RegisterFCMTokenUseCase(fcmTokenRepository)

    @Provides
    @Singleton
    fun provideUserDetailUseCase(
        userDetailRepository: UserDetailRepository,
        appPreferences: AppPreferences
    ): UserDetailUseCase = UserDetailUseCase(userDetailRepository, appPreferences)

    @Provides
    @Singleton
    fun provideProductCategoriesUseCase(
        categoriesRepository: CategoriesRepository
    ): CategoriesUseCase = CategoriesUseCase(categoriesRepository)

    @Provides
    @Singleton
    fun provideBannersUseCase(
        bannerRepository: BannerRepository
    ): BannersUseCase = BannersUseCase(bannerRepository)

    @Provides
    @Singleton
    fun provideGetSellerUseCase(
        sellerRepository: SellerRepository
    ): GetSellerUseCase = GetSellerUseCase(sellerRepository)

    @Provides
    @Singleton
    fun provideGetSellerProductUseCase(
        sellerProductRepository: SellerProductRepository
    ): GetSellerProductUseCase = GetSellerProductUseCase(sellerProductRepository)

    @Provides
    @Singleton
    fun provideGetSearchUseCase(
        searchRepository: SearchRepository
    ): GetSearchUseCase = GetSearchUseCase(searchRepository)

    @Provides
    @Singleton
    fun provideGetProductDetailsUseCase(
        productDetailsRepository: ProductDetailsRepository
    ): GetProductDetailsUseCase = GetProductDetailsUseCase(productDetailsRepository)

    @Provides
    @Singleton
    fun provideGetProductSellersUseCase(
        productSellersRepository: ProductSellersRepository
    ): GetProductSellersUseCase = GetProductSellersUseCase(productSellersRepository)

    @Provides
    @Singleton
    fun provideAddToCartUseCase(
        cartRepository: CartRepository,
        firebaseAnalyticsImpl: FirebaseAnalyticsImpl,
        resourcesProvider: ResourcesProvider
    ): AddToCartUseCase = AddToCartUseCase(cartRepository, firebaseAnalyticsImpl, resourcesProvider)

    @Provides
    @Singleton
    fun provideCreateCartUseCase(
        userDetailRepository: UserDetailRepository,
        cartRepository: CartRepository
    ): CreateCartUseCase = CreateCartUseCase(userDetailRepository, cartRepository)

    @Provides
    @Singleton
    fun provideGetCartIdUseCase(
        userDetailRepository: UserDetailRepository,
        cartRepository: CartRepository
    ): GetCartIdUseCase = GetCartIdUseCase(userDetailRepository, cartRepository)
}