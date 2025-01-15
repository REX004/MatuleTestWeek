package com.amirx.matule_app_sessions.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Product(
    val id: Int,
    val name: String? = null,
    val image: String? = null,
    val price: Int? = null,
    val description: String? = null,
    val gender: String? = null,
    val category: String? = null,
    val popular: Boolean,
) : Parcelable
