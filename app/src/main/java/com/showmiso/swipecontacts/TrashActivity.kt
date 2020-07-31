package com.showmiso.swipecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.showmiso.swipecontacts.model.Contact
import kotlinx.android.synthetic.main.activity_trash.*

class TrashActivity : AppCompatActivity() {
    companion object {
        val deleteList = ArrayList<Contact>()
    }
    private val trashAdapter by lazy { TrashAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)
        initView()
    }

    private fun initView() {
        list_contact.adapter = trashAdapter
        list_contact.layoutManager = LinearLayoutManager(this)
        trashAdapter.updateContact(deleteList)
    }

}
