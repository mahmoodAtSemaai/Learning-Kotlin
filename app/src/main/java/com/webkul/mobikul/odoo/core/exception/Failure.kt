package com.webkul.mobikul.odoo.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific com.webkul.mobikul.odoo.core.extension.failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}
