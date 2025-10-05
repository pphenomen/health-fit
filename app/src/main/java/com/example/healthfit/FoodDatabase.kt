package com.example.healthfit

class FoodDatabase {
    companion object {
        val availableFoods = mutableListOf(
            Food("Яблоко", 59, 0.3, 0.2, 14.0, "Фрукты/ягоды"),
            Food("Курица", 98, 20.0, 2.0, 0.0, "Мясо/птица"),
            Food("Рис", 334, 6.0, 0.3, 77.0, "Крупы/хлеб"),
            Food("Яйцо", 146, 13.0, 10.0, 1.1, "Молочные/яйца")
        )
    }
}