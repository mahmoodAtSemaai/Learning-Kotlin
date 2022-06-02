package com.webkul.mobikul.odoo.core.logger

import timber.log.Timber

class TimberLogger : ILogger {

    override fun logWarning(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    override fun logDebug(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun logError(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    override fun logInfo(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    override fun logVerbose(tag: String, message: String) {
        Timber.tag(tag).v(message)
    }

    override fun logException(tag: String, exception: Throwable) {
        Timber.tag(tag).e(exception)
    }
}