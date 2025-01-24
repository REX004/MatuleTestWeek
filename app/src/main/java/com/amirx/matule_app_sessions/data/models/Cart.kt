package com.amirx.matule_app_sessions.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date
import kotlinx.serialization.SerialName
import kotlinx.serialization.Contextual

@Serializable
@Parcelize
data class Cart(
    val id: Int?= null,
    val user_id: String,
    @Contextual
    @SerialName("created_at")
    val created_at: Date?= null,
    val product: Product,
    var quantity: Int?= null
) : Parcelable