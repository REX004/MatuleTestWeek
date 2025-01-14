package com.amirx.matule_app_sessions.data.datasource.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

object SupabaseClient {

    private val URL = ""
    private val KEY = ""

    @OptIn(SupabaseInternal::class)
    val supabase: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseKey = KEY,
            supabaseUrl = URL
        ) {
            install(Postgrest)
            install(Auth)
            install(Realtime)
            install(Storage)
            httpConfig {
                Logging { level = LogLevel.BODY }
            }
        }
    }
}