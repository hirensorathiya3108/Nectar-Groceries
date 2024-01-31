package com.nectar.groceries.nectargroceries.extensions

import android.view.MenuItem

fun MenuItem.isEnable() {
    isEnabled = true
}
fun MenuItem.isNotEnable() {
    isEnabled = false
}