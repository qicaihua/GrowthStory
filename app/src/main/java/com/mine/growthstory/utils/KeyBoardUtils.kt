package com.mine.growthstory.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBoardUtils {
    @JvmStatic
    fun showKeyboard(context: Context, view: View?) {
        // Show the keyboard.
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }
    fun hideKeyboard(context: Context, view: View?) {
        // Hide the keyboard.
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}