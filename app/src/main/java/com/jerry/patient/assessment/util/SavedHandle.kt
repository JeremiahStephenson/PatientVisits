package com.jerry.patient.assessment.util

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

data class SavedHandle<T>(private val handle: SavedStateHandle,
                          private val tag: String,
                          private val initial: T? = null) {
    init { initial?.let { handle[tag] = it } }
    operator fun getValue(thisRef: Any, property: KProperty<*>) = handle.get<T>(tag)
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T?) = handle.set(tag, value)
}