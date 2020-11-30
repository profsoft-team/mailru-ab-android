package ru.profsoft.addressbook.data.repositories

import androidx.lifecycle.LiveData
import ru.profsoft.addressbook.data.models.Profile

interface IProfilesRepository {
    suspend fun getContacts(): List<Profile>
    fun getContactList(): LiveData<List<Profile>>
}