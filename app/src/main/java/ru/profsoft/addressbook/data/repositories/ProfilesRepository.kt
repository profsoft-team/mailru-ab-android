package ru.profsoft.addressbook.data.repositories

import android.app.Application
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.profsoft.addressbook.data.models.Profile
import ru.profsoft.addressbook.extensions.checkReadContactsPermission
import java.io.IOException

class ProfilesRepository(
    private val application: Application
) : IProfilesRepository {

    private val contacts = MutableLiveData<List<Profile>>()

    override fun getContactList(): LiveData<List<Profile>> = contacts

    override fun getContacts(){
        if (application.checkReadContactsPermission()) {
            val contentUrl = ContactsContract.Contacts.CONTENT_URI
            val profiles = mutableListOf<Profile>()

            val phones = hashMapOf<Long, MutableList<String>>()
            val contentResolver = application.contentResolver
            var cursor: Cursor? = contentResolver.query(
                Phone.CONTENT_URI,
                arrayOf(Phone.CONTACT_ID, Phone.NUMBER),
                null,
                null,
                null
            )

            while (cursor != null && cursor.moveToNext()) {
                val contactId = cursor.getLong(0)
                val phone = cursor.getString(1)
                var phoneList: MutableList<String>
                if (phones.contains(contactId)) {
                    phoneList = phones[contactId]!!
                } else {
                    phoneList = mutableListOf()
                    phones[contactId] = phoneList
                }
                phoneList.add(phone)
                Log.d("Contacts", "getContacts:$contactId $phoneList")
            }
            cursor?.close()

            cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME),
                null,
                null,
                null
            )

            while (cursor != null && cursor.moveToNext()) {
                val contactId = cursor.getLong(0)
                val name = cursor.getString(1)
                val contactPhones = phones[contactId]

                var  photo: Bitmap? = null
                try {
                    val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,
                        ContentUris.withAppendedId(contentUrl, contactId))
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream)
                        inputStream.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                profiles.add(
                    Profile(
                        name,
                        contactPhones,
                        photo
                    )
                )
            }
            cursor?.close()
            contacts.value = profiles
        }
    }
}