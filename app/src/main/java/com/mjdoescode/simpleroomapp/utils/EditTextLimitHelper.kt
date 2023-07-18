package com.mjdoescode.simpleroomapp.utils

import android.text.InputFilter
import android.text.Spanned
import kotlin.math.max

class EditTextLimitHelper(private val maxNewLines: Int): InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val newLineCount = dest?.subSequence(0, dstart)?.count { it == '\n' } ?: 0
        if (newLineCount >= maxNewLines) {
            // Reject input if the maximum number of new lines is reached
            return ""
        }
        return source ?: "" // Accept the input
    }
}
