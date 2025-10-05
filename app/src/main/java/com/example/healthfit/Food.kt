package com.example.healthfit

data class Food(
    val name: String,
    val caloriesPer100g: Int,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
    val category: String,
    var weight: Int = 100
) {
    val calories: Int
        get() = (caloriesPer100g * weight / 100.0).toInt()
    val protein: Double
        get() = proteinPer100g * weight / 100.0
    val fat: Double
        get() = fatPer100g * weight / 100.0
    val carbs: Double
        get() = carbsPer100g * weight / 100.0
}
