object DepVersions {
    const val buildTools = "4.2.1"
    const val kotlin = "1.6.0"
    const val googleServices = "4.3.4"
    const val firebaseCrashlyticsGradle = "2.3.0"
    const val gradleVersion = "2.3.0"

    const val compileSdkVersion = 31
    const val buildToolsVersion = "28.0.3"
    const val renderScriptTargetApi = 16
    const val targetSdkVersion = 30
    const val minSdkVersion = 16

    const val java = 1.8

    const val stylableToast = "1.0.9"
    const val glide = "4.11.0"
    const val expandableRecyclerView = "3.0.0-RC1"
    const val support = "27.1.1"
    const val materialDesignSupport = "1.5.0"
    const val multidex = "1.0.1"
    const val multidex2 = "2.0.1"
    const val gson = "2.8.5"
    const val retrofit = "2.9.0"
    const val okHTTP = "3.12.12"
    const val espresso = "3.1.0"

    const val rxAndroid = "2.1.1"
    const val rxJava2 = "2.2.6"
    const val rxBinding = "2.0.0"

    const val sweetAlert = "1.3"
    const val circleImageView = "2.2.0"
    const val googlePlayService = "11.8.0"
    const val flexbox = "2.0.1"
    const val jsoup = "1.11.3"
    const val bottombar = "2.3.1"
    const val stripe = "6.1.1"
    const val stetho = "1.5.0"
    const val twitterSDK = "3.2.0"
    const val lottie = "3.0.7"
    const val imageCropper = "2.8.0"
    const val webKit = "1.4.0"
    const val daggerAndroid = "2.15"

    const val lifecycleExtension = "2.2.0"
    const val legacySupport = "1.0.0"
    const val appCompat = "1.2.0"
    const val espressoCore = "3.1.0"
    const val androidXAnnotation = "1.1.0"

    const val cardview = "1.0.0"
    const val viewpager = "1.0.0"

    const val firebaseCrash = "16.2.1"
    const val firebaseMessaging = "19.0.1"
    const val firebaseAuth = "18.1.0"
    const val firebaseDatabase = "18.0.0"
    const val firebaseCore = "17.0.1"
    const val firebaseMLVision = "19.0.3"
    const val firebaseMLVisionImageLabel = "17.0.2"
    const val firebaseMLModelInterpreter = "18.0.0"
    const val firebaseCrashlytics = "17.2.2"
    const val firebaseAnalytics = "18.0.0"

    const val facebookSDK = "5.15.3"

    const val playServicesLocation = "17.0.0"
    const val playServicesMaps = "17.0.0"
    const val playServicesAuth = "17.0.0"

    const val constraintLayout = "2.0.4"
    const val constraintLayoutModule = "1.1.3"
    const val screenGrab = "1.0.0"

    const val coreKTX = "1.3.2"
    const val mixPanel = "5.+"

    const val remoteConfig = "21.0.1"
    const val remoteConfigKTX = "21.0.1"

    //SweetAlertDialog Module
    const val materialishProgress = "1.0"
    const val navigation = "2.4.1"
    const val navigationfragment="1.4.0"
    const val nav_version = "2.4.1"


    //appMarketPlace Modules
    const val jUnit = "4.13.2"

    //SemaaiUIModule
    const val extJUnit = "1.1.3"

    const val greenRobot = "3.3.1"

    const val activty = "1.4.0"
    const val fragment = "1.4.0"

    //hilt
    const val hilt = "2.40.5"

    // Arch Components
    const val lifecycle = "2.4.1"

    // Kotlin Coroutines
    const val kotlinCoroutines = "1.6.1"

    const val fragmentKTX = "1.4.1"

    const val timberLogging="5.0.1"

    const val shimmerAndroid ="0.5.0"
}

object Modules {
    const val app = ":app"
    const val appMarketplace = ":appMarketplace"
    const val sweetAlertDialog = ":SweetAlertDialog"
    const val semaaiUI = ":SemaaiUI"
}

object Flavours {
    const val appType = "appType"
    const val environment = "environment"
}

object Version {
    const val code = 37
    const val name = "1.10.2"
}

object Url {
    const val maven = "https://maven.fabric.io/public"
}

object ProGuard {
    const val androidText = "proguard-android.txt"
    const val rules = "proguard-rules.pro"
}

object marketPlaceConstants {
    const val applicationId = "com.semaai.toko"
    const val archivesBaseName = "archivesBaseName"
}

object BuildTypes {
    const val debug = "debug"
    const val release = "release"
}

object Dependencies {
    //Classpaths
    const val buildTools = "com.android.tools.build:gradle:${DepVersions.buildTools}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${DepVersions.kotlin}"
    const val googleServices = "com.google.gms:google-services:${DepVersions.googleServices}"
    const val firebaseCrashlyticsGradle = "com.google.firebase:firebase-crashlytics-gradle:${DepVersions.firebaseCrashlyticsGradle}"
    const val hiltClassPath = "com.google.dagger:hilt-android-gradle-plugin:${DepVersions.hilt}"
    const val safeargs =  "androidx.navigation:navigation-safe-args-gradle-plugin:${DepVersions.nav_version}"

    //App Dependencies
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    // To enable darkmode in webview
    const val webkit = "androidx.webkit:webkit:${DepVersions.webKit}"

    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${DepVersions.daggerAndroid}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${DepVersions.daggerAndroid}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:${DepVersions.legacySupport}"
    const val appCompat = "androidx.appcompat:appcompat:${DepVersions.appCompat}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${DepVersions.espressoCore}"
    const val androidXAnnotation = "androidx.annotation:annotation:${DepVersions.androidXAnnotation}"

    //noinspection GradleCompatible
    const val cardview = "androidx.cardview:cardview:${DepVersions.cardview}"
    const val viewpager = "androidx.viewpager:viewpager:${DepVersions.viewpager}"

    const val glide = "com.github.bumptech.glide:glide:${DepVersions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${DepVersions.glide}"

    const val circleImageView = "de.hdodenhof:circleimageview:${DepVersions.circleImageView}"

    const val jsoup = "org.jsoup:jsoup:${DepVersions.jsoup}"

    const val firebaseCrash = "com.google.firebase:firebase-crash:${DepVersions.firebaseCrash}"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging:${DepVersions.firebaseMessaging}"
    const val firebaseAuth = "com.google.firebase:firebase-auth:${DepVersions.firebaseAuth}"
    const val firebaseDatabase = "com.google.firebase:firebase-database:${DepVersions.firebaseDatabase}"
    const val firebaseCore = "com.google.firebase:firebase-core:${DepVersions.firebaseCore}"
    const val firebaseMLVision = "com.google.firebase:firebase-ml-vision:${DepVersions.firebaseMLVision}"
    const val firebaseMLVisionImageLabel = "com.google.firebase:firebase-ml-vision-image-label-model:${DepVersions.firebaseMLVisionImageLabel}"
    const val firebaseMLModelInterpreter = "com.google.firebase:firebase-ml-model-interpreter:${DepVersions.firebaseMLModelInterpreter}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:${DepVersions.firebaseCrashlytics}"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics:${DepVersions.firebaseAnalytics}"

    const val playServicesLocation = "com.google.android.gms:play-services-location:${DepVersions.playServicesLocation}"
    const val playServicesMaps = "com.google.android.gms:play-services-maps:${DepVersions.playServicesMaps}"
    const val playServicesAuth = "com.google.android.gms:play-services-auth:${DepVersions.playServicesAuth}"

    const val facebookSDK = "com.facebook.android:facebook-android-sdk:${DepVersions.facebookSDK}"

    const val gson = "com.google.code.gson:gson:${DepVersions.gson}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${DepVersions.retrofit}"
    const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${DepVersions.retrofit}"
    const val okHTTPInterceptor = "com.squareup.okhttp3:logging-interceptor:${DepVersions.okHTTP}"
    const val okHTTPURLConnection = "com.squareup.okhttp3:okhttp-urlconnection:${DepVersions.okHTTP}"

    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${DepVersions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${DepVersions.rxJava2}"
    const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${DepVersions.retrofit}"
    const val retrofitConverterScalar = "com.squareup.retrofit2:converter-scalars:${DepVersions.retrofit}"
    const val rxBinding = "com.jakewharton.rxbinding2:rxbinding:${DepVersions.rxBinding}"

    const val bottombar = "com.roughike:bottom-bar:${DepVersions.bottombar}"

    const val expandableRecyclerView = "com.bignerdranch.android:expandablerecyclerview:${DepVersions.expandableRecyclerView}"
    const val stylableToast = "com.muddzdev:styleabletoast:${DepVersions.stylableToast}"
    const val flexbox = "com.google.android:flexbox:${DepVersions.flexbox}"

    const val stetho = "com.facebook.stetho:stetho:${DepVersions.stetho}"
    const val stethoOkHttp = "com.facebook.stetho:stetho-okhttp3:${DepVersions.stetho}"
    const val twitterCore = "com.twitter.sdk.android:twitter-core:${DepVersions.twitterSDK}"
    const val multidex = "androidx.multidex:multidex:${DepVersions.multidex}"
    const val multidex2 = "androidx.multidex:multidex:${DepVersions.multidex2}"
    const val lottie = "com.airbnb.android:lottie:${DepVersions.lottie}"
    const val imageCropper = "com.theartofdev.edmodo:android-image-cropper:${DepVersions.imageCropper}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${DepVersions.constraintLayout}"
    const val screenGrab = "tools.fastlane:screengrab:${DepVersions.screenGrab}"

    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${DepVersions.espresso}"
    const val extJUnit = "androidx.test.ext:junit:${DepVersions.extJUnit}"
    const val coreKTX = "androidx.core:core-ktx:${DepVersions.coreKTX}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${DepVersions.kotlin}"
    const val lifecycleExtension = "androidx.lifecycle:lifecycle-extensions:${DepVersions.lifecycleExtension}"
    const val mixPanel = "com.mixpanel.android:mixpanel-android:${DepVersions.mixPanel}"

    const val remoteConfig = "com.google.firebase:firebase-config:${DepVersions.remoteConfig}"
    const val remoteConfigKTX = "com.google.firebase:firebase-config-ktx:${DepVersions.remoteConfigKTX}"

    const val greenRobot= "org.greenrobot:eventbus:${DepVersions.greenRobot}"
    //SweetAlertDialog Module
    const val materialishProgress = "com.pnikosis:materialish-progress:${DepVersions.materialishProgress}"

    const val hilt = "com.google.dagger:hilt-android:${DepVersions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${DepVersions.hilt}"

    // Arch Components
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${DepVersions.lifecycle}"
    const val lifeData = "androidx.lifecycle:lifecycle-livedata-ktx:${DepVersions.lifecycle}"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${DepVersions.lifecycle}"
    const val viewModelState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${DepVersions.lifecycle}"

    // Kotlin Coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DepVersions.kotlinCoroutines}"
    const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${DepVersions.kotlinCoroutines}"

    //Fragment Ktx
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${DepVersions.fragmentKTX}"


    //appMarketPlace Module
    const val materialDesignSupport =
        "com.google.android.material:material:${DepVersions.materialDesignSupport}"
    const val constraintLayoutModule = "androidx.constraintlayout:constraintlayout:${DepVersions.constraintLayoutModule}"
    const val jUnit = "junit:junit:${DepVersions.jUnit}"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:${DepVersions.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${DepVersions.navigation}"
    const val navigationfragment = "androidx.fragment:fragment-ktx:${DepVersions.navigationfragment}"

    const val activity = "androidx.activity:activity-ktx:${DepVersions.activty}"
    const val fragment = "androidx.fragment:fragment-ktx:${DepVersions.fragment}"

    const val timberLogging="com.jakewharton.timber:timber:${DepVersions.timberLogging}"

    //Shimmer Library
    const val shimmerAndroid = "com.facebook.shimmer:shimmer:${DepVersions.shimmerAndroid}"
}