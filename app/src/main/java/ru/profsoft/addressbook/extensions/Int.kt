package ru.profsoft.addressbook.extensions

import android.content.res.Resources

val Int.dpToPixels: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()