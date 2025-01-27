package com.amirx.matule_app_sessions

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.amirx.matule_app_sessions.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MapKitFactory.setApiKey("AIzaSyBtdCkMGV38958aT-WRIulIu6dVf6RTu8o")
        MapKitFactory.initialize(this)

        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profileFragment, R.id.homeFragment, R.id.messageFragment, R.id.favoritesFragment -> {
                    bottomNav.visibility = View.VISIBLE
                    binding.mainBt.visibility = View.VISIBLE
                }

                else -> {
                    bottomNav.visibility = View.GONE
                    binding.mainBt.visibility = View.GONE
                }
            }
        }

        binding.mainBt.setOnClickListener {
            navController.navigate(R.id.cart)
        }
    }
}