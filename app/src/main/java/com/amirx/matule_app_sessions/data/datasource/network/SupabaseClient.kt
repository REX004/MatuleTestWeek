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

    private const val KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1uZ2Npc3lqbXZibWVibmxhcGh0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzAxOTAyMDIsImV4cCI6MjA0NTc2NjIwMn0.MFGhx41ZSshllA0x677E_sy3nFOcjZG-HFuTNE8yBz4"
    private const val URL = "https://mngcisyjmvbmebnlapht.supabase.co"

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