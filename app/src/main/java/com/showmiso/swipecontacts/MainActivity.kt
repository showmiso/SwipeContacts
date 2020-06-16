package com.showmiso.swipecontacts

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.view.View

class MainActivity : AppCompatActivity() {
    private val contactsList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {

        }
    }

    private fun checkPermission() {
        val strPermission =
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CONTACTS)
        if (checkSelfPermission(strPermission[0]) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(strPermission[1]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(strPermission, Constants.REQUEST_CODE_PERMISSION)
        } else {

        }
    }

    private fun getAllContacts() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.DATA
        )
        val cursor = contentResolver.query(uri, projection,
            null, null, null)

        if (cursor!!.moveToFirst()) {
            do {
                val Id = cursor.getLong(0)
                val thumbnailId = cursor.getLong(1)
                val fullName = cursor.getString(3)
                val phoneNumber = cursor.getString(2)
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

        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_PERMISSION) {

            }
        }
    }

    private fun removeContact() {

    }

    private fun saveContact() {

    }

    private fun restoreContact() {

    }


}
