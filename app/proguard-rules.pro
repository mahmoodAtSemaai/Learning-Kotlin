# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/users/shubham.agarwal/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-------------------------------------------------------------------------------------------------------------------------------------------

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-retrofit2.pro

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-keep class okhttp3.** {*;}


#-------------------------------------------------------------------------------------------------------------------------------------------


# PICAASSO https://github.com/square/picasso/blob/master/picasso/consumer-proguard-rules.txt
### OKHTTP

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote okhttp3.internal.Platform

### OKIO

# java.nio.file.* usage which cannot be used at runtime. Animal sniffer annotation.
-dontwarn okio.Okio
# JDK 7-only method which is @hide on Android. Animal sniffer annotation.
-dontwarn okio.DeflaterSink

-dontwarn com.squareup.picasso.**

#-------------------------------------------------------------------------------------------------------------------------------------------

-keepattributes InnerClasses
-keepattributes EnclosingMethod

#-------------------------------------------------------------------------------------------------------------------------------------------

# remove logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

#-------------------------------------------------------------------------------------------------------------------------------------------


-keep class android.support.v7.widget.SearchView { *; }
 -keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    public <init>(...);
 }

-keepclassmembers class com.webkul.mobikul.** { *; }

#-------------------------------------------------------------------------------------------------------------------------------------------

# JSOUP
-keeppackagenames org.jsoup.nodes



##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class store.model.** { *; }
-keep class com.webkul.mobikul.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


##---------------Begin: proguard configuration for roughike/BottomBar  ----------
# https://github.com/roughike/BottomBar/issues/456

-dontwarn com.roughike.bottombar.**

##---------------Begin: proguard configuration for stripe/stripe-android  ----------
-keep class com.stripe.** { *; }
##---------------Begin: proguard configuration for rxjava  ----------

-keepclassmembers enum * { *; }

#To preserve the info Crashlytics needs for readable crash reports
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**