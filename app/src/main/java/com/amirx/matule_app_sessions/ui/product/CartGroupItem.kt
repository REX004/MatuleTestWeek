package com.amirx.matule_app_sessions.ui.product

import com.amirx.matule_app_sessions.data.models.Cart
import java.util.Calendar
import java.util.Date

sealed class CartGroupItem {
    data class Header(val title: String) : CartGroupItem()
    data class Item(val cart: Cart) : CartGroupItem()
}

fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val date = Calendar.getInstance().apply { time = this@isToday }
    return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
}

fun Date.isYesterday(): Boolean {
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val date = Calendar.getInstance().apply { time = this@isYesterday }
    return yesterday.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
}