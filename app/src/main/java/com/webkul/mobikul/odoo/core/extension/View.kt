package com.webkul.mobikul.odoo.core.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.webkul.mobikul.odoo.constant.ApplicationConstant

/**
 * Visibility modifiers and check functions
 */

fun View.isVisibile(): Boolean = this.visibility == View.VISIBLE

fun View.isGone(): Boolean = this.visibility == View.GONE

fun View.isInvisible(): Boolean = this.visibility == View.INVISIBLE

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.showKeyboard() {
    this.postDelayed({
        this.isFocusableInTouchMode = true
        this.isFocusable = true
        this.requestFocus()
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, (ApplicationConstant.MILLIS / 5).toLong())
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setBackground(drawableId : Int) {
    this.setBackgroundResource(drawableId)
}

fun TextView.setfontColor(colorId : Int) {
    this.setTextColor(resources.getColor(colorId))
}

fun ImageView.setColor(colorId : Int) {
    this.setColorFilter(resources.getColor(colorId))
}