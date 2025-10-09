package com.example.healthfit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        val isDark = pref.getBoolean("dark_theme", false)
        setTheme(if (isDark) R.style.Theme_HealthFit_Dark else R.style.Theme_HealthFit)

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}