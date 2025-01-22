package com.amirx.matule_app_sessions.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Notification(
    val title: String,
    val description: String,
    val created_at: String
) : Parcelable