package ru.profsoft.addressbook.data.models

data class Profile(
    val name: String,
    val phones: List<String>,
    val image: String?
)