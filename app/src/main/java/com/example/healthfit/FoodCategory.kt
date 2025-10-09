package com.example.healthfit

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class FoodCategory(
    @StringRes val stringRes: Int,
    @DrawableRes val iconRes: Int
) {
    FRUITS(R.string.category_fruits, R.drawable.fruits),
    VEGETABLES(R.string.category_vegetables, R.drawable.vegetables),
    MEAT(R.string.category_meat, R.drawable.meat),
    SEAFOOD(R.string.category_seafood, R.drawable.seafood),
    DAIRY_EGGS(R.string.category_dairy_eggs, R.drawable.eggmilk),
    GRAINS(R.string.category_grains, R.drawable.bread),
    DRINKS(R.string.category_drinks, R.drawable.drinks),
    SWEETS(R.string.category_sweets, R.drawable.sweets),
    OTHER(R.string.category_other, R.drawable.other);

    companion object {
        // получает enum по строковому названию категории
        fun fromString(context: Context, categoryName: String): FoodCategory {
            return values().find { context.getString(it.stringRes) == categoryName } ?: OTHER
        }
    }
}