package com.fmk.monitoring.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService

object CommonUtil {
    lateinit var inputMethodManager: InputMethodManager

    /**
     * 키보드 숨기기
     * */
    fun hideKeyboard(context: Context, view: View) {
        inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}