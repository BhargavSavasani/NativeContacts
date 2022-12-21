package com.nativecontacts

import android.database.Cursor


fun Cursor.getStringValue(key: String) = getString(getColumnIndexOrThrow(key)).orEmpty()

fun Cursor.getIntValue(key: String) = getInt(getColumnIndexOrThrow(key))

fun Int.toBoolean(): Boolean {
    return this != 0
}