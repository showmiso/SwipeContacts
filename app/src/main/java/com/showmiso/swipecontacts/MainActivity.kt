package com.showmiso.swipecontacts

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.showmiso.swipecontacts.model.Contact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        checkPermission()
    }

    private fun initUI() {
        contactAdapter = ContactAdapter()
        list_contact.adapter = contactAdapter
        list_contact.layoutManager = LinearLayoutManager(this)
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
            getAllContacts()
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
        val contactsList = ArrayList<Contact>()

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
        contactAdapter.updateContact(contactsList)
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
                getAllContacts()
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
