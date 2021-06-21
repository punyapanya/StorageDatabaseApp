package com.example.storagedatabaseapp.util

import android.widget.Button

fun Button.makeClickable(state: Boolean) {
    isSelected = !state
    isClickable = state
}