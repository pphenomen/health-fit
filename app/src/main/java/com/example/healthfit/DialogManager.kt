package com.example.healthfit

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.healthfit.databinding.AddCustomFoodBinding
import com.example.healthfit.databinding.DialogFoodBinding
import com.example.healthfit.databinding.FoodListBinding

class DialogManager(private val context: Context) {

    // показывает диалог для выбора продукта из списка
    fun showFoodSelectionDialog(onFoodSelected: (Food) -> Unit) {
        val binding = FoodListBinding.inflate(LayoutInflater.from(context))
        val foodNames = FoodDatabase.availableFoods.map { it.name }
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, foodNames)
        binding.foodListView.adapter = adapter

        val dialog = AlertDialog.Builder(context)
            .setTitle(R.string.dialog_food_title)
            .setView(binding.root)
            .setNegativeButton(R.string.dialog_cancel, null)
            .create()

        binding.foodSearchInput.addTextChangedListener { text ->
            val query = text.toString().trim().lowercase()
            val filteredFoods = FoodDatabase.availableFoods.filter { it.name.lowercase().contains(query) }
            adapter.clear()
            adapter.addAll(filteredFoods.map { it.name })
            adapter.notifyDataSetChanged()
        }

        binding.foodListView.setOnItemClickListener { _, _, position, _ ->
            dialog.dismiss()
            val foodName = adapter.getItem(position)!!
            val food = FoodDatabase.availableFoods.find { it.name == foodName }!!
            onFoodSelected(food)
        }

        binding.btnAddCustom.setOnClickListener {
            dialog.dismiss()
            showCustomFoodDialog()
        }

        dialog.show()
    }

    // показывает диалог для ввода веса продукта
    fun showWeightDialog(onWeightConfirmed: (Int) -> Unit) {
        val input = EditText(context).apply {
            hint = context.getString(R.string.dialog_weight_hint)
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        AlertDialog.Builder(context)
            .setTitle(R.string.dialog_weight_title)
            .setView(input)
            .setPositiveButton(R.string.dialog_ok) { _, _ ->
                val grams = input.text.toString().toIntOrNull() ?: 100
                onWeightConfirmed(grams)
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }

    // показывает диалог для создания нового продукта
    private fun showCustomFoodDialog() {
        val binding = AddCustomFoodBinding.inflate(LayoutInflater.from(context))
        val categories = context.resources.getStringArray(R.array.food_categories)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = adapter

        AlertDialog.Builder(context)
            .setTitle(R.string.dialog_custom_food_title)
            .setView(binding.root)
            .setPositiveButton(R.string.dialog_add) { _, _ ->
                createFoodFromInput(binding)
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }

    // создает объект food из данных, введенных пользователем
    private fun createFoodFromInput(binding: AddCustomFoodBinding) {
        val name = binding.inputFoodName.text.toString().trim()
        val calories = binding.inputCalories.text.toString().toIntOrNull()
        val protein = binding.inputProtein.text.toString().toDoubleOrNull()
        val fat = binding.inputFat.text.toString().toDoubleOrNull()
        val carbs = binding.inputCarbs.text.toString().toDoubleOrNull()
        val category = binding.spinnerCategory.selectedItem.toString()

        if (name.isEmpty() || calories == null || protein == null || fat == null || carbs == null) {
            Toast.makeText(context, R.string.fields_warning, Toast.LENGTH_LONG).show()
            return
        }

        val newFood = Food(name, calories, protein, fat, carbs, category)
        FoodDatabase.availableFoods.add(newFood)
        Toast.makeText(context, context.getString(R.string.food_created_toast, name), Toast.LENGTH_SHORT).show()
    }

    // показывает диалог с подробной информацией о продукте
    fun showFoodDetailsDialog(food: Food) {
        val binding = DialogFoodBinding.inflate(LayoutInflater.from(context))

        binding.foodIcon.setImageResource(FoodCategory.fromString(context, food.category).iconRes)
        binding.foodName.text = food.name
        binding.foodWeight.text = context.getString(R.string.food_weight, food.weight)
        binding.foodCalories.text = context.getString(R.string.calories_label, food.calories)
        binding.foodKBJU.text = context.getString(R.string.kbju_format, food.protein, food.fat, food.carbs)
        binding.foodCategory.text = context.getString(R.string.category_label, food.category)

        AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton(R.string.dialog_close, null)
            .show()
    }
}