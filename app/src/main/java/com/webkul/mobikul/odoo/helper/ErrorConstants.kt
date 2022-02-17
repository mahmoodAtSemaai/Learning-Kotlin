package com.webkul.mobikul.odoo.helper

sealed class ErrorConstants(
    var errorCode: Int,
    var errorType: String = "Unknown Error",
    var errorMessage: String = "Unknown Error Occurred"
) {

    object LoginError : ErrorConstants(5001, "Login Error")

    object SignupError : ErrorConstants(5002, "Signup Error")

    object APIError : ErrorConstants(5020, "Api Error")

    object FingerPrintSetupError: ErrorConstants(5070, "Fingerprint Setup Error", "Fingerprint Setup Failed Unexpectedly")

    object AddressUpdateError : ErrorConstants(5050, "Address Update Error")

    object ClearSearchHistoryError :
        ErrorConstants(5010, "Search History Clear Error", "Failed to Clear Search History")

    object LanguageChangeError :
        ErrorConstants(5011, "Language Change Error", "Failed to change the language")

    object SignOutError : ErrorConstants(5012, "Sign Out Error", "Failed to SignOut the User")

    object NotificationToggleError :
        ErrorConstants(5013, "Notification Toggle Error", "Unable to Toggle the Notification")

    object RecentViewToggleError :
        ErrorConstants(5014, "Recent View Toggle Error", "Unable to Toggle Recent View")

    object ClearRecentViewError :
        ErrorConstants(5015, "Clear Recent View  Error", "Unable to Clear Recent View")

}
