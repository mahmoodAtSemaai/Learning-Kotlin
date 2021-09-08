

# Getting Started with Odoo Mobile App Builder


## Basic Information

Odoo Mobile App Builder can transform your Odoo website to a fully funtional Android app. It will provide the customers with lots of features, to relish their shopping experience in a more easy and mobile way in order to provide Ubiquity (Easier information access in real-time), Convenience (Devices that store data are always at hand), Accessibility (Choice to limit the accessibility to particular persons who can be contacted anywhere anytime), Personalization (Creating services that customize the end-user experience), and Localization (Matching services to the location of the customers), which is just some taps and swipes away. The working demo of the app can be found on Play Store.


## Supported Version

  Target Sdk Version: 27

  Minimum Sdk Version: 16


## Instructions To Configure The Application


First, Install the Mobikul API Module (If not installed yet) on your server.
Now, You need to create an Api Secret Key for the app to communicate with the server.

You need to update that in app code also.

Open the build.gradle file in your Android Studio.
There you will find three buildConfigField that you needs to changed.
path- /Mobikul_Odoo_Application/app/build.gradle

BASE_URL - This is your website base url ex- https://example.com
BASIC_AUTH_KEY = basic Authentication Key

### - How to generate Basic Authentication Key

in your website login with admin credantials open Mobikul .
-In mobikul App >> Credantial >> Api Secret Key - You can enter any String as example- "4fs5sdjaj7s".

this key will be your username and password to generate basic Authentication Key.
For generating basic Authentication Key you need to concatenate the string "username:password" for example- (4fs5sdjaj7s:4fs5sdjaj7s) and convert it to base64.

Now, You will be able to connect to the server with your app.

## Change the image resorces

To Change the image resorces splash screen, placeholder,luncher icon, status-bar icon you can find these images in below mentioned path and you can replace them with yours.

### Launcher icon-
 path- /app/src/main/res/mipmap-hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi.  
 name- ic_launcher

### Placeholder-
path- /app/src/main/res/drawable/ic_vector_odoo_placeholder_grey400_48dp.png  
name- ic_vector_odoo_placeholder_grey400_48dp

### Splash screen-
path- /app/src/main/res/drawable/img_splash_screen.png  
name- img_splash_screen.png

### Statusbar icon
path- /app/src/main/res/drawable-hdpi/ic_stat_ic_notification.png-hdpi,mdpi,xhdpi,xx,hdpi  
name- ic_stat_ic_notification

## Push notification

 For push notification you need to change google-services.jsonfile with yours.  
 Path - /app/src/main/google-services.json

 Change application id in build.gradle file with the package-name in google-services file.  
 path - /app/build.gradle

## Color Code ( file name location )

For changing the color theme of the application go to color.xml file and hange the colors.  
path- /app/src/main/res/values/colors.xml

## App name

changes in string.xml file
 - app_name
 - store_name
 - signup_text_on_sign_in_page

 path- /app/src/main/res/values/strings.xml
 
## For Add New Localization:

#### For App Builder

Goto app -> main -> res -> value-> find String file and traslate it as your localization.

#### For MarketPlace.

Goto  appmarketplaceapp ->  main -> res -> value -> find String file and traslate it according to your localization.

Sample
```
(en) <string name="new_version_available">There is a newer version of Mobikul application available, click OK to upgrade now?</string>
(ar) <string name="new_version_available">هل هناك إصدار أحدث من تطبيق موبيكول المتاحة، انقر فوق موافق للترقية الآن؟</string>
```
Note:  Traslate only Value part i.e.
```
<string name="Key_name"> Value name </string>
```






