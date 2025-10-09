package com.example.healthfit

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import java.util.*
import androidx.core.content.edit

class DiaryActivity : AppCompatActivity() {

    private val morningList = mutableListOf<Food>()
    private val lunchList = mutableListOf<Food>()
    private val dinnerList = mutableListOf<Food>()
    private val snackList = mutableListOf<Food>()

    private lateinit var totalMorning: TextView
    private lateinit var totalLunch: TextView
    private lateinit var totalDinner: TextView
    private lateinit var totalSnack: TextView
    private lateinit var totalDay: TextView

    private lateinit var morningKBJU: TextView
    private lateinit var lunchKBJU: TextView
    private lateinit var dinnerKBJU: TextView
    private lateinit var snackKBJU: TextView
    private lateinit var totalKBJU: TextView

    private lateinit var containerMorning: LinearLayout
    private lateinit var containerLunch: LinearLayout
    private lateinit var containerDinner: LinearLayout
    private lateinit var containerSnack: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        val isDark = pref.getBoolean("dark_theme", false)
        setTheme(if (isDark) R.style.Theme_HealthFit_Dark else R.style.Theme_HealthFit)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        containerMorning = findViewById(R.id.containerMorning)
        containerLunch = findViewById(R.id.containerLunch)
        containerDinner = findViewById(R.id.containerDinner)
        containerSnack = findViewById(R.id.containerSnack)

        totalMorning = findViewById(R.id.morningCalories)
        totalLunch = findViewById(R.id.lunchCalories)
        totalDinner = findViewById(R.id.dinnerCalories)
        totalSnack = findViewById(R.id.snackCalories)
        totalDay = findViewById(R.id.dayCalories)

        morningKBJU = findViewById(R.id.morningKBJU)
        lunchKBJU = findViewById(R.id.lunchKBJU)
        dinnerKBJU = findViewById(R.id.dinnerKBJU)
        snackKBJU = findViewById(R.id.snackKBJU)
        totalKBJU = findViewById(R.id.dayKBJU)

        findViewById<ImageButton>(R.id.btnAddMorning).setOnClickListener {
            showFoodDialog(morningList, containerMorning, totalMorning, morningKBJU)
        }
        findViewById<ImageButton>(R.id.btnAddLunch).setOnClickListener {
            showFoodDialog(lunchList, containerLunch, totalLunch, lunchKBJU)
        }
        findViewById<ImageButton>(R.id.btnAddDinner).setOnClickListener {
            showFoodDialog(dinnerList, containerDinner, totalDinner, dinnerKBJU)
        }
        findViewById<ImageButton>(R.id.btnAddSnack).setOnClickListener {
            showFoodDialog(snackList, containerSnack, totalSnack, snackKBJU)
        }

        setupToggle(containerMorning, findViewById(R.id.btnToggleMorning))
        setupToggle(containerLunch, findViewById(R.id.btnToggleLunch))
        setupToggle(containerDinner, findViewById(R.id.btnToggleDinner))
        setupToggle(containerSnack, findViewById(R.id.btnToggleSnack))

        resetTotals()
    }

    private fun resetTotals() {
        morningKBJU.text = getString(R.string.kbju_format, 0.0f, 0.0f, 0.0f)
        lunchKBJU.text = getString(R.string.kbju_format, 0.0f, 0.0f, 0.0f)
        dinnerKBJU.text = getString(R.string.kbju_format, 0.0f, 0.0f, 0.0f)
        snackKBJU.text = getString(R.string.kbju_format, 0.0f, 0.0f, 0.0f)
        totalKBJU.text = getString(R.string.kbju_format, 0.0f, 0.0f, 0.0f)
        totalMorning.text = getString(R.string.calories_value, 0)
        totalLunch.text = getString(R.string.calories_value, 0)
        totalDinner.text = getString(R.string.calories_value, 0)
        totalSnack.text = getString(R.string.calories_value, 0)
        totalDay.text = getString(R.string.calories_value, 0)
    }

    private fun setupToggle(container: LinearLayout, toggleButton: ImageButton) {
        toggleButton.setOnClickListener {
            if (container.isGone) {
                container.visibility = View.VISIBLE
                toggleButton.setImageResource(R.drawable.arrow_up)
                for (i in 0 until container.childCount) {
                    val card = container.getChildAt(i)
                    val animation = AnimationUtils.loadAnimation(this, R.anim.food_card_appear)
                    animation.startOffset = (i * 50L)
                    card.startAnimation(animation)
                }
            } else {
                container.visibility = View.GONE
                toggleButton.setImageResource(R.drawable.arrow_down)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                val pref = getSharedPreferences("prefs", MODE_PRIVATE)
                val isDark = !pref.getBoolean("dark_theme", false)
                pref.edit { putBoolean("dark_theme", isDark) }
                recreate()
                true
            }
            R.id.menu_change_language -> {
                if (resources.configuration.locale.language == "ru") {
                    setLocale("en")
                } else {
                    setLocale("ru")
                }
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun showFoodDialog(
        targetList: MutableList<Food>,
        container: LinearLayout,
        sectionTotal: TextView,
        sectionKBJU: TextView
    ) {
        val foodNames = FoodDatabase.availableFoods.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_food_title)
            .setItems(foodNames) { _, which ->
                val selectedFood = FoodDatabase.availableFoods[which]

                val input = EditText(this)
                input.hint = getString(R.string.dialog_weight_hint)
                input.inputType = InputType.TYPE_CLASS_NUMBER

                AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_weight_title)
                    .setView(input)
                    .setPositiveButton(R.string.dialog_ok) { _, _ ->
                        val grams = input.text.toString().toIntOrNull() ?: 100
                        val existing = targetList.find { it.name == selectedFood.name }
                        if (existing != null) existing.weight += grams
                        else targetList.add(selectedFood.copy(weight = grams))

                        refreshSection(container, targetList)
                        updateSectionTotal(targetList, sectionTotal, sectionKBJU)
                        updateDayTotal()

                        Toast.makeText(this, R.string.food_added_toast, Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .show()
            }
            .show()
    }

    private fun refreshSection(container: LinearLayout, foods: List<Food>) {
        container.removeAllViews()
        for (food in foods) addFoodCard(container, food)
    }

    private fun addFoodCard(container: LinearLayout, food: Food) {
        val card = layoutInflater.inflate(R.layout.food_card, container, false)
        val icon = card.findViewById<ImageView>(R.id.foodIcon)
        val text = card.findViewById<TextView>(R.id.foodText)
        val btnDetails = card.findViewById<ImageButton>(R.id.btnDetails)

        icon.setImageResource(getCategoryIconRes(food.category))
        text.text = getString(R.string.food_card_format, food.name, food.weight, food.calories)

        val animation = AnimationUtils.loadAnimation(this, R.anim.food_card_appear)
        card.startAnimation(animation)

        btnDetails.setOnClickListener { showFoodDetailsDialog(food) }

        container.addView(card)
    }

    private fun showFoodDetailsDialog(food: Food) {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_food, null)

        val icon = view.findViewById<ImageView>(R.id.foodIcon)
        val name = view.findViewById<TextView>(R.id.foodName)
        val grams = view.findViewById<TextView>(R.id.foodWeight)
        val calories = view.findViewById<TextView>(R.id.foodCalories)
        val kbju = view.findViewById<TextView>(R.id.foodKBJU)
        val category = view.findViewById<TextView>(R.id.foodCategory)

        icon.setImageResource(getCategoryIconRes(food.category))
        name.text = food.name
        grams.text = getString(R.string.food_weight, food.weight)
        calories.text = getString(R.string.calories_label, food.calories)
        kbju.text = getString(R.string.kbju_format, food.protein, food.fat, food.carbs)
        category.text = getString(R.string.category_label, food.category)

        builder.setView(view)
        builder.setPositiveButton(R.string.dialog_close, null)
        builder.show()
    }

    private fun updateSectionTotal(list: List<Food>, textView: TextView, kbjuView: TextView) {
        val calories = list.sumOf { it.calories }
        val protein = list.sumOf { it.protein.toDouble() }.toFloat()
        val fat = list.sumOf { it.fat.toDouble() }.toFloat()
        val carbs = list.sumOf { it.carbs.toDouble() }.toFloat()

        textView.text = getString(R.string.calories_value, calories)
        kbjuView.text = getString(R.string.kbju_format, protein, fat, carbs)
    }

    private fun updateDayTotal() {
        val allFoods = morningList + lunchList + dinnerList + snackList
        val calories = allFoods.sumOf { it.calories }
        val protein = allFoods.sumOf { it.protein.toDouble() }.toFloat()
        val fat = allFoods.sumOf { it.fat.toDouble() }.toFloat()
        val carbs = allFoods.sumOf { it.carbs.toDouble() }.toFloat()

        totalDay.text = getString(R.string.calories_value, calories)
        totalKBJU.text = getString(R.string.kbju_format, protein, fat, carbs)
    }

    private fun getCategoryIconRes(category: String): Int = when (category) {
        "Фрукты/ягоды" -> R.drawable.fruits
        "Овощи" -> R.drawable.vegetables
        "Мясо/птица" -> R.drawable.meat
        "Рыба/морепродукты" -> R.drawable.seafood
        "Молочные/яйца" -> R.drawable.eggmilk
        "Крупы/хлеб" -> R.drawable.bread
        "Напитки" -> R.drawable.drinks
        "Сладости/выпечка" -> R.drawable.sweets
        else -> R.drawable.other
    }
}