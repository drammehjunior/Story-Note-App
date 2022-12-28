package com.example.submissionintermediate.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

var emailPattern: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()


fun verifyEmail(variable: EditText){
    variable.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(str: Editable) {
            if (str.toString().isNotEmpty()) {
                var value = str.toString()
                if(value.trim().matches(emailPattern)){
                }else variable.error = "Email Not Valid"
            }

        }
    })
}