package com.showmiso.swipecontacts

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.showmiso.swipecontacts.model.Contact

class ContactManager(
    private val context: Context
) {
    fun getInfo2(): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val cr = context.contentResolver
        val displayName = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        val filter = "$displayName NOT LIKE '%@%'"
        val order = String.format("%1\$s COLLATE NOCASE", displayName)
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            displayName,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            filter,
            null,
            order
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // get the contact's information
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(displayName))
                val hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                // get the user's email address
                var email: String = ""
                val ce = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                if (ce != null && ce.moveToFirst()) {
                    email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    ce.close()
                }

                // get the user's phone number
                var phone: String = ""
                if (hasPhone > 0) {
                    val cp = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (cp != null && cp.moveToFirst()) {
                        phone =
                            cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        cp.close()
                    }
                }

                // get the user's photo
                var uri: Uri? = null
                val su = ContactsContract.Data.CONTACT_ID + "=" + id + " AND " +
                        ContactsContract.Data.MIMETYPE + "='" +
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'"
                val cu = cr.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    su,
                    null,
                    null
                )
                if (cu != null && cu.moveToFirst())  {
                    val person = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI, id.toLong()
                    )
                    uri = Uri.withAppendedPath(
                        person,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                    )
                    cu.close()
                }

                if (phone.isNotEmpty()) {
                    val contact = Contact(
                        0,
                        0,
                        name,
                        phone,
                        email,
                        uri
                    )
                    contactsList.add(contact)
                }
            } while (cursor.moveToNext())

            // clean up cursor
            cursor.close()
        }

        return contactsList
    }
}