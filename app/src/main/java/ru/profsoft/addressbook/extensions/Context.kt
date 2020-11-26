package ru.profsoft.addressbook.extensions

import android.content.Context
import android.content.pm.PackageManager

fun Context.checkReadContactsPermission(): Boolean =
    this.checkCallingOrSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED