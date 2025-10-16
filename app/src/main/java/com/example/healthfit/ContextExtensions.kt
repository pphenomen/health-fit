package com.example.healthfit

import android.content.Context
import android.util.TypedValue

// функция-расширение для получения id ресурса из атрибута текущей темы
fun Context.resolveThemeAttr(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.resourceId
}