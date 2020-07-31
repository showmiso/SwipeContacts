package com.showmiso.swipecontacts

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.showmiso.swipecontacts.model.Contact
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URI

class ContactManager(
    private val context: Context
) {
    fun getAllContacts(): ArrayList<Contact> {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
        )
        val cursor = context.contentResolver.query(uri, projection,
            null, null, null)
        val contactsList = ArrayList<Contact>()

        if (cursor!!.moveToFirst()) {
            do {
                val Id = cursor.getLong(0)
                val fullName = cursor.getString(1)
                val thumbnailId = cursor.getLong(2)
                val phoneNumber = cursor.getString(3)
                val email = cursor.getString(4)

                val contact = Contact(
                    Id,
                    thumbnailId,
                    fullName,
                    phoneNumber,
                    email
                )
                contactsList.add(contact)
            } while (cursor.moveToNext())
        }
        contactsList.shuffle()
        cursor.close()

        return contactsList
    }

    fun openPhoto(contactId: Long): InputStream? {
        val contactUri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        val photoUri =
            Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        val cursor: Cursor = context.contentResolver.query(
            photoUri,
            arrayOf<String>(ContactsContract.Contacts.Photo.PHOTO),
            null,
            null,
            null
        )
            ?: return null
        try {
            if (cursor.moveToFirst()) {
                val data = cursor.getBlob(0)
                if (data != null) {
                    return ByteArrayInputStream(data)
                }
            }
        } finally {
            cursor.close()
        }
        return null
    }

    private fun getPhotoUriFromID(id: String): Uri? {
        try {
            val cur: Cursor? = context.contentResolver
                .query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                            + "'", null, null
                )
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null // no photo
                }
            } else {
                return null // error in cursor process
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val person: Uri = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI, id.toLong()
        )
        return Uri.withAppendedPath(
            person,
            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
        )
    }
}