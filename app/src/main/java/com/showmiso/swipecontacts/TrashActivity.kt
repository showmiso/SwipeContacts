package com.showmiso.swipecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.showmiso.swipecontacts.dialog.TwoButtonDialogFragment
import com.showmiso.swipecontacts.model.Contact
import com.showmiso.swipecontacts.presenter.ContactPresenter
import com.showmiso.swipecontacts.utils.AdMobManager
import kotlinx.android.synthetic.main.activity_trash.*

class TrashActivity : AppCompatActivity(), TrashAdapter.OnItemClickListener {
    companion object {
        val deleteList = ArrayList<Contact>()
    }

    private val contactPresenter by lazy {
        ContactPresenter(
            this
        )
    }
    private val trashAdapter by lazy {
        TrashAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)
        initUI()
    }

    override fun onDestroy() {
        contactPresenter.onDestroy()
        super.onDestroy()
    }

    private fun initUI() {
        list_contact.adapter = trashAdapter
        list_contact.layoutManager = LinearLayoutManager(this)
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        getDrawable(R.drawable.bg_divider)?.let { itemDecorator.setDrawable(it) }
        list_contact.addItemDecoration(itemDecorator)
        trashAdapter.updateContact(deleteList)
        btn_delete_all.setOnClickListener(onClickListener)
        btn_back.setOnClickListener(onClickListener)

        val banner = AdMobManager(
            this,
            layout_ad_view_container,
            TrashActivity::class.simpleName!!
        )
        banner.initBanner()
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_delete_all -> {
                val dialog = TwoButtonDialogFragment(
                    getString(R.string.trash_delete_all),
                    getString(R.string.dialog_message),
                    object : TwoButtonDialogFragment.OnDialogClickListener {
                        override fun clickOk() {
                            contactPresenter.deleteContactList(deleteList)
                            trashAdapter.deleteAll()
                        }
                    }
                )
                dialog.show(supportFragmentManager, "")
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onClickItem(contact: Contact, layoutId: Int) {
        if (layoutId == R.id.btn_delete) {
            contactPresenter.deleteContact(contact)
            Toast.makeText(this, "연락처 ${contact.name} 이(가) 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
        deleteList.remove(contact)
    }
}
