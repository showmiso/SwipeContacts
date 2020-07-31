package com.showmiso.swipecontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.showmiso.swipecontacts.model.Contact
import kotlinx.android.synthetic.main.view_contact_info.view.*

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contactsList = ArrayList<Contact>()

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.txt_name.text = contact.name
            itemView.txt_phone.text = contact.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_contact_info, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun skipCard() {
        val contact = contactsList[contactsList.size - 1]
        contactsList.remove(contact)
        notifyItemRemoved(contactsList.size - 1)
    }

    fun deleteCard() {

    }

    fun updateContact(list: ArrayList<Contact>) {
        contactsList = list
        notifyDataSetChanged()
    }
}