package com.showmiso.swipecontacts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.showmiso.swipecontacts.model.Contact
import kotlinx.android.synthetic.main.view_contact_card.view.*

class ContactAdapter(
    private val layoutId: Int
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contactsList = ArrayList<Contact>()

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.txt_name.text = contact.name
            itemView.txt_phone.text = contact.phone
            itemView.txt_email.text = contact.email

            itemView.img_thumbnail.setImageURI(contact.uri)
            if (contact.thumbnail != null) {
                Log.d("TAG", "TEST")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun deleteContact(position: Int): Contact? {
        if (position < contactsList.size) {
            val contact = contactsList[position]
            contactsList.remove(contact)
            notifyItemRemoved(position)
            return contact
        }
        return null
    }

    fun updateContact(list: ArrayList<Contact>) {
        contactsList = list
        notifyDataSetChanged()
    }
}