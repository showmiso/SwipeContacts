package com.showmiso.swipecontacts

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.showmiso.swipecontacts.model.Contact
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val contactAdapter by lazy { ContactAdapter(R.layout.view_contact_card) }
    private val manager by lazy { CardStackLayoutManager(this, onCardStackListener) }
    private var skipCount: Int = 0
    private val contactManager by lazy { ContactManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        checkPermission()
    }

    private fun initUI() {
        card_view.layoutManager = manager
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
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager.setSwipeAnimationSetting(setting)
                card_view.swipe()
            }
            R.id.btn_skip -> {
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager.setSwipeAnimationSetting(setting)
                card_view.swipe()
            }
            R.id.btn_restore -> {
                val setting = RewindAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(DecelerateInterpolator())
                    .build()
                manager.setRewindAnimationSetting(setting)
                card_view.rewind()
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
            if (direction == Direction.Left) {
                deleteCard()
            } else if (direction == Direction.Right) {
                skipCard()
            }
        }

        override fun onCardCanceled() {
        }

        override fun onCardAppeared(view: View?, position: Int) {
        }

        override fun onCardRewound() {
            restoreCard()
        }
    }

    private fun deleteCard() {
        val contact = contactAdapter.deleteContact(skipCount)
        if (contact != null) {
            TrashActivity.deleteList.add(contact)
            Toast.makeText(applicationContext, "delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun skipCard() {
        skipCount += 1
        Toast.makeText(applicationContext, "skip $skipCount", Toast.LENGTH_SHORT).show()
    }

    private fun restoreCard() {
        skipCount -= 1
        Toast.makeText(applicationContext, "restore $skipCount", Toast.LENGTH_SHORT).show()
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
        contactAdapter.updateContact(contactManager.getAllContacts())
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
