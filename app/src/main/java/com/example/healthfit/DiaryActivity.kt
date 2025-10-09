package com.example.healthfit

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.healthfit.databinding.ActivityDiaryBinding
import java.util.*

class DiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryBinding
    private val mealManagers = mutableListOf<MealManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupMealManagers()
        resetTotals()
    }

    // применяет сохраненную тему (светлую/темную)
    private fun applyTheme() {
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        val isDark = pref.getBoolean("dark_theme", false)
        setTheme(if (isDark) R.style.Theme_HealthFit_Dark else R.style.Theme_HealthFit)
    }

    // настраивает тулбар
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    // инициализирует менеджеры для каждого приема пищи
    private fun setupMealManagers() {
        val dialogManager = DialogManager(this)

        mealManagers.add(
            MealManager(this, binding.morningSection, dialogManager, getString(R.string.morning_title), R.drawable.breakfast) { updateDayTotal() }
        )
        mealManagers.add(
            MealManager(this, binding.lunchSection, dialogManager, getString(R.string.lunch_title), R.drawable.lunch) { updateDayTotal() }
        )
        mealManagers.add(
            MealManager(this, binding.dinnerSection, dialogManager, getString(R.string.dinner_title), R.drawable.dinner) { updateDayTotal() }
        )
        mealManagers.add(
            MealManager(this, binding.snackSection, dialogManager, getString(R.string.snack_title), R.drawable.snack) { updateDayTotal() }
        )
    }

    // обновляет итоговые значения калорий и бжу за весь день
    private fun updateDayTotal() {
        val allFoods = mealManagers.flatMap { it.getFoods() }
        binding.dayCalories.text = getString(R.string.calories_value, allFoods.totalCalories())
        binding.dayKBJU.text = getString(
            R.string.kbju_format,
            allFoods.totalProtein(),
            allFoods.totalFat(),
            allFoods.totalCarbs()
        )
    }

    // сбрасывает все счетчики
    private fun resetTotals() {
        mealManagers.forEach { it.reset() }
        updateDayTotal()
    }

    // создает меню в тулбаре
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // обрабатывает нажатия на элементы меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.action_theme -> {
                val pref = getSharedPreferences("prefs", MODE_PRIVATE)
                val isDark = !pref.getBoolean("dark_theme", false)
                pref.edit { putBoolean("dark_theme", isDark) }
                recreate()
                true
            }
            R.id.menu_change_language -> {
                setLocale(if (resources.configuration.locale.language == "ru") "en" else "ru")
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // устанавливает новую локаль для приложения
    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}