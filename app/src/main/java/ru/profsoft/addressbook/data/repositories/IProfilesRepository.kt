package ru.profsoft.addressbook.data.repositories

import androidx.lifecycle.LiveData
import ru.profsoft.addressbook.data.models.Profile

interface IProfilesRepository {
    fun getContacts()
    fun getContactList(): LiveData<List<Profile>>
}