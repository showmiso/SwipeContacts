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
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
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
        card_view.layoutManager = CardStackLayoutManager(this, onCardStackListener)
        card_view.adapter = contactAdapter

        btn_setting.setOnClickListener(onClickListener)
        btn_delete.setOnClickListener(onClickListener)
        btn_skip.setOnClickListener(onClickListener)
        btn_restore.setOnClickListener(onClickListener)
        layout_trash.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_setting -> {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            R.id.btn_delete -> {

            }
            R.id.btn_skip -> {

            }
            R.id.btn_restore -> {

            }
            R.id.layout_trash -> {
                startActivity(Intent(this@MainActivity, TrashActivity::class.java))
            }
        }
    }

    private val onCardStackListener = object : CardStackListener {
        override fun onCardDisappeared(view: View?, position: Int) {
        }

        override fun onCardDragging(direction: Direction?, ratio: Float) {
        }

        override fun onCardSwiped(direction: Direction?) {
        }

        override fun onCardCanceled() {
        }

        override fun onCardAppeared(view: View?, position: Int) {
        }

        override fun onCardRewound() {
        }
    }

    private fun deleteContact() {

    }

    private fun skipContact() {

    }

    private fun restoreContact() {

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
                val thumbnailImage = cursor.getLong(1)
                val fullName = cursor.getString(3)
                val phoneNumber = cursor.getString(2)
                val email = cursor.getString(4)
                val contact = Contact(
                    Id,
                    thumbnailImage,
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    getAllContacts()
                } else {
                    // 연락처 허용 거부
                }
            }
        }
    }


}
