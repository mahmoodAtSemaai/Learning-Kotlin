package com.webkul.mobikul.odoo.core.di


import android.content.Context
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.core.utils.NetworkManager
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.PaymentTransferInstructionServices
import com.webkul.mobikul.odoo.domain.repository.*
import com.webkul.mobikul.odoo.domain.usecase.auth.*
import com.webkul.mobikul.odoo.domain.usecase.checkout.*
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.domain.repository.*
import com.webkul.mobikul.odoo.domain.usecase.account.GetAccountInfoDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.appConfig.GetAppConfigUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.*
import com.webkul.mobikul.odoo.domain.usecase.banner.BannersUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.AddToCartUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.BagItemsCountUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.CreateCartUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.GetCartIdUseCase
import com.webkul.mobikul.odoo.domain.usecase.chat.CreateChatChannelUseCase
import com.webkul.mobikul.odoo.domain.usecase.fcmToken.RegisterFCMTokenUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeUseCase
import com.webkul.mobikul.odoo.domain.usecase.network.IsNetworkUseCase
import com.webkul.mobikul.odoo.domain.usecase.onboarding.CountdownTimerUseCase
import com.webkul.mobikul.odoo.domain.usecase.onboarding.OnboardingUseCase
import com.webkul.mobikul.odoo.domain.usecase.product.*
import com.webkul.mobikul.odoo.domain.usecase.productCategories.CategoriesUseCase
import com.webkul.mobikul.odoo.domain.usecase.search.GetSearchUseCase
import com.webkul.mobikul.odoo.domain.usecase.seller.GetSellerProductUseCase
import com.webkul.mobikul.odoo.domain.usecase.seller.GetSellerUseCase
import com.webkul.mobikul.odoo.domain.usecase.session.IsValidSessionUseCase
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.*
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.UserDetailUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.IsWishListAllowedUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.GetUserUseCase
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
        sellerTermsConditionRepository: SellerTermsConditionRepository
    ): ViewMarketPlaceTnCUseCase = ViewMarketPlaceTnCUseCase(sellerTermsConditionRepository)

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
    fun provideContinuePhoneNumberUseCase(phoneNumberRepository: PhoneNumberRepository): ContinuePhoneNumberUseCase =
        ContinuePhoneNumberUseCase(phoneNumberRepository)

    @Provides
    @Singleton
    fun provideLoginPasswordUseCase(
        authenticationPasswordRepository: AuthenticationPasswordRepository,
        userRepository: UserRepository
    ): LoginPasswordUseCase =
        LoginPasswordUseCase(authenticationPasswordRepository, userRepository)


    @Provides
    @Singleton
    fun provideGenerateOtpUseCase(otpRepository: OtpRepository): GenerateOtpUseCase =
        GenerateOtpUseCase(otpRepository)

    @Provides
    @Singleton
    fun provideVerifyOtpUseCase(
        authenticationOtpRepository: AuthenticationOtpRepository,
        userRepository: UserRepository
    ): VerifyOtpUseCase =
        VerifyOtpUseCase(authenticationOtpRepository, userRepository)


    @Provides
    @Singleton
    fun provideCountdownTimerUseCase(): CountdownTimerUseCase = CountdownTimerUseCase()

    @Provides
    @Singleton
    fun provideOnboardingUseCase(onboardingRepository: OnboardingRepository): OnboardingUseCase =
        OnboardingUseCase(onboardingRepository)

    @Provides
    @Singleton
    fun provideAddAddressUseCase(addressRepository: AddressRepository): AddAddressUseCase =
        AddAddressUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideDeleteAddressUseCase(addressRepository: AddressRepository): DeleteAddressUseCase =
        DeleteAddressUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideEditAddressUseCase(addressRepository: AddressRepository): EditAddressUseCase =
        EditAddressUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideFetchAddressFormDataUseCase(addressRepository: AddressRepository): FetchAddressFormDataUseCase =
        FetchAddressFormDataUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideFetchAllAddressUseCase(addressRepository: AddressRepository): FetchAllAddressUseCase =
        FetchAllAddressUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideFetchDistrictsUseCase(addressRepository: AddressRepository): FetchDistrictsUseCase =
        FetchDistrictsUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideFetchStatesUseCase(addressRepository: AddressRepository): FetchStatesUseCase =
        FetchStatesUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideFetchSubDistrictsUseCase(addressRepository: AddressRepository): FetchSubDistrictsUseCase =
        FetchSubDistrictsUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideFetchVillagesUseCase(addressRepository: AddressRepository): FetchVillagesUseCase =
        FetchVillagesUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideUpdateAddressForOrderUseCase(addressRepository: AddressRepository): UpdateAddressForOrderUseCase =
        UpdateAddressForOrderUseCase(addressRepository)

    @Provides
    @Singleton
    fun provideValidateMandatoryAddressFieldsUseCase(): ValidateMandatoryAddressFieldsUseCase =
        ValidateMandatoryAddressFieldsUseCase()

    @Provides
    @Singleton
    fun providePlaceOrderUseCase(orderRepository: OrderRepository): PlaceOrderUseCase =
        PlaceOrderUseCase(orderRepository)

    @Provides
    @Singleton
    fun provideFetchOrderDetailsUseCase(orderRepository: OrderRepository): FetchOrderDetailsUseCase =
        FetchOrderDetailsUseCase(orderRepository)

    @Provides
    @Singleton
    fun provideFetchOrderDetailsAfterCheckoutUseCase(orderRepository: OrderRepository): FetchOrderDetailsAfterCheckoutUseCase =
        FetchOrderDetailsAfterCheckoutUseCase(orderRepository)

    @Provides
    @Singleton
    fun provideFetchOrderReviewUseCase(orderRepository: OrderRepository): FetchOrderReviewDataUseCase =
        FetchOrderReviewDataUseCase(orderRepository)

    @Provides
    @Singleton
    fun provideValidateOrderForCheckOutUseCase() : ValidateOrderForCheckOutUseCase =
        ValidateOrderForCheckOutUseCase()

    @Provides
    @Singleton
    fun provideFetchPaymentAcquirersUseCase(paymentAcquireRepository: PaymentAcquireRepository) : FetchPaymentAcquirersUseCase =
        FetchPaymentAcquirersUseCase(paymentAcquireRepository)

    @Provides
    @Singleton
    fun provideFetchPaymentAcquirerMethodsUseCase(paymentAcquireRepository: PaymentAcquireRepository) : FetchPaymentAcquirerMethodsUseCase =
        FetchPaymentAcquirerMethodsUseCase(paymentAcquireRepository)

    @Provides
    @Singleton
    fun provideFetchPaymentAcquirerMethodProvidersUseCase(paymentAcquireRepository: PaymentAcquireRepository) : FetchPaymentAcquirerMethodProvidersUseCase =
        FetchPaymentAcquirerMethodProvidersUseCase(paymentAcquireRepository)

    @Provides
    @Singleton
    fun provideUpdateShippingMethodForOrderUseCase(shippingMethodRepository: ShippingMethodRepository) : UpdateShippingMethodForOrderUseCase =
        UpdateShippingMethodForOrderUseCase(shippingMethodRepository)

    @Provides
    @Singleton
    fun provideFetchActiveShippingMethodsUseCase(shippingMethodRepository: ShippingMethodRepository) : FetchActiveShippingMethodsUseCase =
        FetchActiveShippingMethodsUseCase(shippingMethodRepository)

    @Provides
    @Singleton
    fun provideCreateChatChannelUseCase(
        chatChannelRepository: ChatChannelRepository
    ): CreateChatChannelUseCase =
        CreateChatChannelUseCase(chatChannelRepository)

    @Provides
    @Singleton
    fun provideIsFirstTimeUseCase(
        appConfigRepository: AppConfigRepository
    ): IsFirstTimeUseCase = IsFirstTimeUseCase(appConfigRepository)

    @Provides
    @Singleton
    fun provideHomeDataUseCase(
        homeDataRepository: HomeDataRepository,
        userRepository: UserRepository
    ): HomeUseCase = HomeUseCase(homeDataRepository, userRepository)

    @Provides
    @Singleton
    fun provideSplashDataUseCase(
        splashDataRepository: SplashDataRepository
    ): SplashUseCase = SplashUseCase(splashDataRepository)

    @Provides
    @Singleton
    fun provideIsNetworkUseCase(
        networkManager: NetworkManager
    ): IsNetworkUseCase = IsNetworkUseCase(networkManager)

    @Provides
    @Singleton
    fun provideIsValidSessionUseCase(): IsValidSessionUseCase = IsValidSessionUseCase()

    @Provides
    @Singleton
    fun provideSetIsFirstTimeUseCase(
        appConfigRepository: AppConfigRepository
    ): SetIsFirstTimeUseCase = SetIsFirstTimeUseCase(appConfigRepository)

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
    fun getAppConfigUseCase(
        appConfigRepository: AppConfigRepository
    ): GetAppConfigUseCase = GetAppConfigUseCase(appConfigRepository)

    @Provides
    @Singleton
    fun provideGenerateSignUpOtpUseCase(otpRepository: OtpRepository): GenerateSignUpOtpUseCase =
        GenerateSignUpOtpUseCase(otpRepository)

    @Provides
    @Singleton
    fun provideVerifySignUpOtpUseCase(
        signUpAuthRepository: SignUpAuthRepository,
        userRepository: UserRepository
    ): VerifySignUpOtpUseCase =
        VerifySignUpOtpUseCase(signUpAuthRepository, userRepository)

    @Provides
    @Singleton
    fun provideOnboardingStageUseCase(
        onboardingStageRepository: OnboardingStageRepository
    ): OnboardingStageUseCase =
        OnboardingStageUseCase(onboardingStageRepository)

    @Provides
    @Singleton
    fun provideUserOnboardingStageUseCase(
        userOnboardingStageRepository: UserOnboardingStageRepository
    ): UserOnboardingStageUseCase =
        UserOnboardingStageUseCase(userOnboardingStageRepository)

    @Provides
    @Singleton
    fun provideContinueCustomerGroupUseCase(
        userCustomerGroupRepository: UserCustomerGroupRepository,
        userRepository: UserRepository
    ): ContinueCustomerGroupUseCase =
        ContinueCustomerGroupUseCase(userCustomerGroupRepository, userRepository)

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
        userRepository: UserRepository
    ): ContinueUserDetailsUseCase =
        ContinueUserDetailsUseCase(userRepository)

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
        userLocationRepository: UserLocationRepository,
        userRepository: UserRepository
    ): ContinueUserLocationUseCase =
        ContinueUserLocationUseCase(userLocationRepository, userRepository)

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
        userRepository: UserRepository,
        resourcesProvider: ResourcesProvider
    ): VerifyUserDetailsUseCase =
        VerifyUserDetailsUseCase(userRepository, resourcesProvider)

    @Provides
    @Singleton
    fun provideGetOnboardingDataUseCase(
        userRepository: UserRepository,
        resourcesProvider: ResourcesProvider
    ): GetOnboardingDataUseCase =
        GetOnboardingDataUseCase(userRepository, resourcesProvider)

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
        userRepository: UserRepository,
        resourcesProvider: ResourcesProvider
    ): GetUserDetailsViewsUseCase =
        GetUserDetailsViewsUseCase(userRepository, resourcesProvider)

    @Provides
    @Singleton
    fun provideGetAccountInfoDataUseCase(userRepository: UserRepository): GetAccountInfoDataUseCase =
        GetAccountInfoDataUseCase(userRepository)

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
    fun provideRegisterFCMTokenUseCase(
        fcmTokenRepository: FCMTokenRepository,
        userRepository: UserRepository
    ): RegisterFCMTokenUseCase = RegisterFCMTokenUseCase(fcmTokenRepository,userRepository)

    @Provides
    @Singleton
    fun provideCreatePaymentUseCase(
        paymentProcessingRepository: PaymentProcessingRepository
    ): CreatePaymentUseCase = CreatePaymentUseCase(paymentProcessingRepository)

    @Provides
    @Singleton
    fun provideFetchTransferInstructionUseCase(
            paymentTransferInstructionRepository: PaymentTransferInstructionRepository
    ): FetchTransferInstructionUseCase = FetchTransferInstructionUseCase(paymentTransferInstructionRepository)

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

    @Provides
    @Singleton
    fun getUserUseCase(
        userRepository: UserRepository
    ): GetUserUseCase = GetUserUseCase(userRepository)

}