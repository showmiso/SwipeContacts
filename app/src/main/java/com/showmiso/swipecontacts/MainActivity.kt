package com.showmiso.swipecontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.showmiso.swipecontacts.presenter.ContactPresenter
import com.showmiso.swipecontacts.utils.Constants
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val contactAdapter by lazy { ContactAdapter(R.layout.view_contact_card) }
    private val manager by lazy { CardStackLayoutManager(this, onCardStackListener) }
    private var skipCount: Int = 0
    private val contactPresenter by lazy {
        ContactPresenter(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        checkPermission()
        contactPresenter.onCreate()
    }

    override fun onDestroy() {
        contactPresenter.onDestroy()
        super.onDestroy()
    }

    private fun initUI() {
        manager.setStackFrom(StackFrom.Top)
        card_view.layoutManager = manager
        card_view.adapter = contactAdapter

        btn_setting.setOnClickListener(onClickListener)
        btn_delete.setOnClickListener(onClickListener)
        btn_skip.setOnClickListener(onClickListener)
        btn_restore.setOnClickListener(onClickListener)
        btn_trash.setOnClickListener(onClickListener)
        btn_try_again.setOnClickListener(onClickListener)
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
            R.id.btn_trash -> {
                startActivity(Intent(this@MainActivity, TrashActivity::class.java))
            }
            R.id.btn_try_again -> {
                if (contactAdapter.itemCount == skipCount) {
                    skipCount = 0
                    contactAdapter.clear()
                }
                getAllContacts()
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
//            Toast.makeText(applicationContext, "delete", Toast.LENGTH_SHORT).show()
            if (TrashActivity.deleteList.size > 0) {
                btn_trash.visibility = View.VISIBLE
            }
        }
        checkCards()
    }

    private fun skipCard() {
        skipCount += 1
//        Toast.makeText(applicationContext, "skip $skipCount", Toast.LENGTH_SHORT).show()
        checkCards()
    }

    private fun restoreCard() {
        skipCount -= 1
//        Toast.makeText(applicationContext, "restore $skipCount", Toast.LENGTH_SHORT).show()
        checkRestoreCards()
    }

    private fun checkCards() {
        // 남은 카드가 없으면
        Log.d("checkCard", "")

        if (contactAdapter.itemCount == skipCount) {
            card_view.visibility = View.GONE
        }
    }

    private fun checkRestoreCards() {
        card_view.visibility = View.VISIBLE
    }

    private fun checkPermission() {
        val strPermission =
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
        if (checkSelfPermission(strPermission[0]) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(strPermission[1]) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(strPermission, Constants.REQUEST_CODE_PERMISSION)
        } else {
            getAllContacts()
        }
    }

    private fun getAllContacts() {
        card_view.visibility = View.VISIBLE
        contactPresenter.getContactAll(contactAdapter)
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
                    // TODO: 퍼미션 거부시 설정에서 퍼미션 허용해 달라는 화면을 띄울 것
                }
            }
        }
    }
}
