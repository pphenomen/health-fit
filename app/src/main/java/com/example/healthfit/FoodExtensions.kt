package com.example.healthfit

// считает суммарные калории для списка продуктов
fun List<Food>.totalCalories(): Int {
    return this.sumOf { it.calories }
}

// считает суммарные белки для списка продуктов
fun List<Food>.totalProtein(): Float {
    return this.sumOf { it.protein }.toFloat()
}

// считает суммарные жиры для списка продуктов
fun List<Food>.totalFat(): Float {
    return this.sumOf { it.fat }.toFloat()
}

// считает суммарные углеводы для списка продуктов
fun List<Food>.totalCarbs(): Float {
    return this.sumOf { it.carbs }.toFloat()
}