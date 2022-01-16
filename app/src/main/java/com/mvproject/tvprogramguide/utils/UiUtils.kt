package com.mvproject.tvprogramguide.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Extension function to simplify setting an onTextChanged action to EditText components.
 */
fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s.toString())
        }
    })
}