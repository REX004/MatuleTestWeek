package com.amirx.matule_app_sessions.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val image: String,
    val price: Int,
    val description: String,
    val gender: String,
    val category: String,
    val popular: Boolean,
)
