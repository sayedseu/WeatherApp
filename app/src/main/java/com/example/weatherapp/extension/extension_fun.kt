package com.example.weatherapp.extension

import java.util.*
import kotlin.math.roundToInt

fun String.capitalizeFirstCharacterOfEachWord(): String {
    val words = this.split(" ")
    var newString = ""
    words.forEach { word ->
        newString += word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + " "
    }
    return newString.trimEnd()
}

fun Double.convertKelvinToCelsius() : Double = this - 273.15

fun Double.makeReadableFormat() : String = "${this.roundToInt()} \u2103"