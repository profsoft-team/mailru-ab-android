package ru.profsoft.addressbook.data.models

import android.graphics.Bitmap

data class Profile(
    val name: String,
    val phones: List<String>,
    val image: Bitmap?
)