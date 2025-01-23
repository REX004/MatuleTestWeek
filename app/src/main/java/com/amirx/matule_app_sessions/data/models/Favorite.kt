package com.amirx.matule_app_sessions.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Favorite(
    val product: Product,
    val user_id: String
) : Parcelable