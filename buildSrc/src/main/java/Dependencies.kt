

object DepVersions {

    const val buildTools = "4.0.2"
    const val kotlin = "1.3.41"
    const val googleServices = "4.3.4"
    const val firebaseCrashlyticsGradle = "2.3.0"

    const val compileSdkVersion = 30
    const val buildToolsVersion = "28.0.3"
    const val renderScriptTargetApi = 16
    const val targetSdkVersion = 30
    const val minSdkVersion = 16

    const val java = 1.8


}

object Modules{
    const val app = ":app"
    const val appMarketplace = ":appMarketplace"
    const val SweetAlertDialog = ":SweetAlertDialog"
}

object Dependencies {

    //Classpaths
    const val buildTools = "com.android.tools.build:gradle:${DepVersions.buildTools}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${DepVersions.kotlin}"
    const val googleServices = "com.google.gms:google-services:${DepVersions.googleServices}"
    const val firebaseCrashlyticsGradle = "com.google.firebase:firebase-crashlytics-gradle:${DepVersions.firebaseCrashlyticsGradle}"

    //App Dependencies
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    //App Dependencies


}