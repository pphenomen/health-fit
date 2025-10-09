package com.example.healthfit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    // устанавливает тему до создания активности
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    // светлая/темная темы
    private fun applyTheme() {
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        val isDark = pref.getBoolean("dark_theme", false)
        setTheme(if (isDark) R.style.Theme_HealthFit_Dark else R.style.Theme_HealthFit)
    }
}