package com.showmiso.swipecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.showmiso.swipecontacts.model.Contact
import com.showmiso.swipecontacts.presenter.ContactPresenter
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
        val trashAdapter = TrashAdapter(this)
        list_contact.adapter = trashAdapter
        list_contact.layoutManager = LinearLayoutManager(this)
        trashAdapter.updateContact(deleteList)

        btn_delete_all.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_delete_all -> {
                contactPresenter.deleteContactList(deleteList)
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onDeleteItem(contact: Contact) {
        contactPresenter.deleteContact(contact)
    }
}
