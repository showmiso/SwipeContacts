package com.showmiso.swipecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        initView()
    }

    override fun onDestroy() {
        contactPresenter.onDestroy()
        super.onDestroy()
    }

    private fun initView() {
        val trashAdapter = TrashAdapter(this)
        list_contact.adapter = trashAdapter
        list_contact.layoutManager = LinearLayoutManager(this)
        trashAdapter.updateContact(deleteList)
    }

    override fun onDeleteItem(contact: Contact) {
        contactPresenter.deleteContact(contact)
    }
}
