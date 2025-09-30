package com.example.healthfit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DiaryActivity : AppCompatActivity() {

    private val morningList = mutableListOf<Food>()
    private val lunchList = mutableListOf<Food>()
    private val dinnerList = mutableListOf<Food>()
    private val snackList = mutableListOf<Food>()

    private lateinit var morningAdapter: ArrayAdapter<String>
    private lateinit var lunchAdapter: ArrayAdapter<String>
    private lateinit var dinnerAdapter: ArrayAdapter<String>
    private lateinit var snackAdapter: ArrayAdapter<String>

    private lateinit var totalMorning: TextView
    private lateinit var totalLunch: TextView
    private lateinit var totalDinner: TextView
    private lateinit var totalSnack: TextView
    private lateinit var totalDay: TextView

    private val availableFoods = listOf(
        Food("Яблоко", 59, 0.3, 0.2, 14.0),
        Food("Курица", 98, 20.0, 2.0, 0.0),
        Food("Рис", 334, 6.0, 0.3, 77.0),
        Food("Яйцо", 146, 13.0, 10.0, 1.1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val listMorning = findViewById<ListView>(R.id.listMorning)
        val listLunch = findViewById<ListView>(R.id.listLunch)
        val listDinner = findViewById<ListView>(R.id.listDinner)
        val listSnack = findViewById<ListView>(R.id.listSnack)

        totalMorning = findViewById(R.id.totalMorning)
        totalLunch = findViewById(R.id.totalLunch)
        totalDinner = findViewById(R.id.totalDinner)
        totalSnack = findViewById(R.id.totalSnack)
        totalDay = findViewById(R.id.totalDay)

        morningAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lunchAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        dinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        snackAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())

        listMorning.adapter = morningAdapter
        listLunch.adapter = lunchAdapter
        listDinner.adapter = dinnerAdapter
        listSnack.adapter = snackAdapter

        findViewById<Button>(R.id.btnAddMorning).setOnClickListener { showFoodDialog(morningList, morningAdapter, totalMorning) }
        findViewById<Button>(R.id.btnAddLunch).setOnClickListener { showFoodDialog(lunchList, lunchAdapter, totalLunch) }
        findViewById<Button>(R.id.btnAddDinner).setOnClickListener { showFoodDialog(dinnerList, dinnerAdapter, totalDinner) }
        findViewById<Button>(R.id.btnAddSnack).setOnClickListener { showFoodDialog(snackList, snackAdapter, totalSnack) }
    }

    private fun showFoodDialog(
        targetList: MutableList<Food>,
        adapter: ArrayAdapter<String>,
        sectionTotal: TextView
    ) {
        val foodNames = availableFoods.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Выберите продукт")
            .setItems(foodNames) { _, which ->
                val selectedFood = availableFoods[which]

                val input = EditText(this)
                input.hint = "Введите вес (граммы)"
                input.inputType = android.text.InputType.TYPE_CLASS_NUMBER

                AlertDialog.Builder(this)
                    .setTitle("Укажите вес")
                    .setView(input)
                    .setPositiveButton("OK") { _, _ ->
                        val grams = input.text.toString().toIntOrNull() ?: 100

                        val existing = targetList.find { it.name == selectedFood.name }
                        if (existing != null) {
                            existing.weight += grams
                        } else {
                            targetList.add(
                                Food(
                                    selectedFood.name,
                                    selectedFood.caloriesPer100g,
                                    selectedFood.proteinPer100g,
                                    selectedFood.fatPer100g,
                                    selectedFood.carbsPer100g,
                                    grams
                                )
                            )
                        }

                        adapter.clear()
                        adapter.addAll(targetList.map { food ->
                            "${food.name} (${food.weight} г) — " +
                                    "${food.calories} ккал, " +
                                    "Б:${String.format("%.1f", food.protein)} " +
                                    "Ж:${String.format("%.1f", food.fat)} " +
                                    "У:${String.format("%.1f", food.carbs)}"
                        })
                        adapter.notifyDataSetChanged()

                        updateSectionTotal(targetList, sectionTotal)
                        updateDayTotal()

                        Toast.makeText(this, "Продукт добавлен", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
            .show()
    }

    private fun updateSectionTotal(list: List<Food>, textView: TextView) {
        val calories = list.sumOf { it.calories }
        val protein = list.sumOf { it.protein }
        val fat = list.sumOf { it.fat }
        val carbs = list.sumOf { it.carbs }

        textView.text = "Итого: $calories ккал, " +
                "Б:${String.format("%.1f", protein)} " +
                "Ж:${String.format("%.1f", fat)} " +
                "У:${String.format("%.1f", carbs)}"
    }

    private fun updateDayTotal() {
        val allFoods = morningList + lunchList + dinnerList + snackList
        val calories = allFoods.sumOf { it.calories }
        val protein = allFoods.sumOf { it.protein }
        val fat = allFoods.sumOf { it.fat }
        val carbs = allFoods.sumOf { it.carbs }

        totalDay.text = "За день: $calories ккал, " +
                "Б:${String.format("%.1f", protein)} " +
                "Ж:${String.format("%.1f", fat)} " +
                "У:${String.format("%.1f", carbs)}"
    }
}
