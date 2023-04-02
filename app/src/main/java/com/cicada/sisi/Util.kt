package com.cicada.sisi

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun <T> joinToString(
    collection: Collection<T>,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    transform: ((T) -> CharSequence)? = null
): String {

    val sb = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) sb.append(separator)
        val str = transform?.invoke(element) ?: element.toString()
        sb.append(str)
    }

    sb.append(postfix)
    return sb.toString()
}


fun removePartFromString(str: String, startIndex: Int, endIndex: Int): String {

    if (startIndex < 0 || endIndex >= str.length || startIndex > endIndex) {

        // invalid input, return the original string
        return str
    }
    return str.substring(0, startIndex) + str.substring(endIndex + 1)
}


fun removeCharsFromString(inputString: String, charsToRemove: List<Char>): String {

    var result = inputString
    charsToRemove.forEach { charToRemove ->
        result = result.replace(charToRemove.toString(), "")
    }
    return result
}

fun String.isDouble(): Boolean {

    return this.toDoubleOrNull() != null
}