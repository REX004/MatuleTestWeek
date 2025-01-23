package com.amirx.matule_app_sessions.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Cart(
    val id: Int?= null,
    val user_id: String,
    val product: Product,
    var quantity: Int?= null
) : Parcelable
