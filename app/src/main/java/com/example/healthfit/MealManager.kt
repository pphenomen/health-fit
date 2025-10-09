package com.example.healthfit

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import com.example.healthfit.databinding.LayoutMealSectionBinding

class MealManager(
    private val context: Context,
    private val binding: LayoutMealSectionBinding,
    private val dialogManager: DialogManager,
    title: String,
    iconRes: Int,
    private val onUpdate: () -> Unit
) {
    private val foodList = mutableListOf<Food>()

    init {
        binding.mealTitle.text = title
        binding.mealIcon.setImageResource(iconRes)
        binding.btnAdd.setOnClickListener {
            showAddFoodDialog()
        }
        setupToggle()
    }

    // возвращает список добавленных продуктов
    fun getFoods(): List<Food> = foodList.toList()

    // сбрасывает секцию до начального состояния
    fun reset() {
        foodList.clear()
        refreshFoodCards()
        updateSectionTotal()
    }

    // добавляет продукт в список
    private fun addFood(food: Food) {
        val existing = foodList.find { it.name == food.name }
        if (existing != null) {
            existing.weight += food.weight
        } else {
            foodList.add(food)
        }
        refreshFoodCards()
        updateSectionTotal()
        onUpdate() // Сообщаем главной активности об обновлении
    }

    // обновляет отображение карточек продуктов в контейнере
    private fun refreshFoodCards() {
        binding.container.removeAllViews()
        for (food in foodList) {
            val card = View.inflate(context, R.layout.food_card, null)
            val icon = card.findViewById<ImageView>(R.id.foodIcon)
            val text = card.findViewById<TextView>(R.id.foodText)
            val btnDetails = card.findViewById<ImageButton>(R.id.btnDetails)

            icon.setImageResource(FoodCategory.fromString(context, food.category).iconRes)
            text.text = context.getString(R.string.food_card_format, food.name, food.weight, food.calories)

            btnDetails.setOnClickListener { dialogManager.showFoodDetailsDialog(food) }

            val animation = AnimationUtils.loadAnimation(context, R.anim.food_card_appear)
            card.startAnimation(animation)
            binding.container.addView(card)
        }
    }

    // обновляет счетчики калорий и бжу для данной секции
    private fun updateSectionTotal() {
        binding.mealCalories.text = context.getString(R.string.calories_value, foodList.totalCalories())
        binding.mealKBJU.text = context.getString(
            R.string.kbju_format,
            foodList.totalProtein(),
            foodList.totalFat(),
            foodList.totalCarbs()
        )
    }

    // показывает диалог выбора продукта
    private fun showAddFoodDialog() {
        dialogManager.showFoodSelectionDialog { selectedFood ->
            dialogManager.showWeightDialog { weight ->
                addFood(selectedFood.copy(weight = weight))
            }
        }
    }

    // настраивает кнопку для скрытия/раскрытия списка продуктов
    private fun setupToggle() {
        binding.btnToggle.setOnClickListener {
            if (binding.container.isGone) {
                binding.container.visibility = View.VISIBLE
                binding.btnToggle.setImageResource(R.drawable.arrow_up)
            } else {
                binding.container.visibility = View.GONE
                binding.btnToggle.setImageResource(R.drawable.arrow_down)
            }
        }
    }
}