package ru.profsoft.addressbook.data.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val name: String?,
    val phones: List<String>?,
    val image: Bitmap?
) : Parcelable