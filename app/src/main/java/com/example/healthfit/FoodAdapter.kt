package com.example.healthfit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class FoodAdapter(context: Context, private val foods: MutableList<Food>) :
    ArrayAdapter<Food>(context, 0, foods) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.food_card, parent, false)

        val food = getItem(position)!!

        val icon = itemView.findViewById<ImageView>(R.id.foodIcon)
        val text = itemView.findViewById<TextView>(R.id.foodText)

        icon.setImageResource(getCategoryIconRes(food.category))

        text.text = "${food.name} (${food.weight} г) — " +
                "${food.calories} ккал, " +
                "Б:${String.format("%.1f", food.protein)} " +
                "Ж:${String.format("%.1f", food.fat)} " +
                "У:${String.format("%.1f", food.carbs)}"

        return itemView
    }

    private fun getCategoryIconRes(category: String): Int {
        return when (category) {
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
}
