package com.webkul.mobikul.odoo.helper

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText

class GenericTextWatcher(etNext: EditText, etPrev: EditText) : TextWatcher {
    private val etPrev: EditText
    private val etNext: EditText

    override fun afterTextChanged(editable: Editable) {
        val text: String = editable.toString()
        if (text.length == 1) etNext.requestFocus() else if (text.length == 0) etPrev.requestFocus()
    }

    override fun beforeTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
    override fun onTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}


    init {
        this.etPrev = etPrev
        this.etNext = etNext
    }
}

class GenericKeyEvent(
    private val currentView: EditText,
    private val previousView: EditText
) : View.OnKeyListener {

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.text.toString()
                .isEmpty()
        ) {
            previousView.requestFocus()
            return true
        }
        return false
    }
}
