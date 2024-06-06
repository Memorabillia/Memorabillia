package com.example.memorabilia.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$"
                if (!s.matches(emailPattern.toRegex())) {
                    error = "Email is not valid"
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }
}
