package ru.profsoft.addressbook.data.repositories

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.profsoft.addressbook.data.models.Profile
import ru.profsoft.addressbook.extensions.checkReadContactsPermission

class ProfilesRepository (
    private val application: Application
) : IProfilesRepository {

    private val contacts = MutableLiveData<List<Profile>>()

    override fun getContactList(): LiveData<List<Profile>> = contacts

    override fun getContacts(){
        if (application.checkReadContactsPermission()) {
            val contentUrl = ContactsContract.Contacts.CONTENT_URI
            val contactId = ContactsContract.Contacts._ID
            val displayName = ContactsContract.Contacts.DISPLAY_NAME
            val hasPhoneNumber = ContactsContract.Contacts.HAS_PHONE_NUMBER

            val phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val phoneContactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            val contactPhoneNumber = ContactsContract.CommonDataKinds.Phone.NUMBER

            val profiles: MutableList<Profile> = mutableListOf()
            val contentResolver = application.contentResolver

            val cursor = contentResolver?.query(contentUrl, null, null, null, null)

            if (cursor != null) {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {

                        val currentContactId = cursor.getString(cursor.getColumnIndex(contactId))
                        val phoneNumber: MutableList<String>? = mutableListOf()

                        val name: String = if (cursor.getString(cursor.getColumnIndex(displayName)).isNullOrEmpty().not()) {
                            cursor.getString(cursor.getColumnIndex(displayName))
                        } else {
                            ""
                        }

                        val currentCasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(hasPhoneNumber)))
                        if (currentCasPhoneNumber > 0) {
                            val phoneCursor = contentResolver.query(phoneContentUri, null,
                                "$phoneContactId = ?", arrayOf(currentContactId), null)
                            if (phoneCursor != null) {
                                while (phoneCursor.moveToNext()) {
                                    phoneNumber?.add(phoneCursor.getString(phoneCursor.getColumnIndex(contactPhoneNumber)))
                                }
                                phoneCursor.close()
                            }

                            val profile = Profile(name = name, phones = (phoneNumber ?: emptyList()).toList(), image = null)
                            profiles.add(profile)
                            Log.d("Repo", "getContacts: $profile")
                        }
                    }
                }
            }
            cursor?.close()
            contacts.value = profiles
        }
    }
}