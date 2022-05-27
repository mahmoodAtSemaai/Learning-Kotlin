package com.webkul.mobikul.odoo.core.logger

interface ILogger {

    fun logWarning(tag: String, message:String)

    fun logDebug(tag: String, message:String)

    fun logError(tag: String, message:String)

    fun logInfo(tag: String, message:String)

    fun logVerbose(tag: String, message:String)

    fun logException(tag: String, exception: Throwable)
}