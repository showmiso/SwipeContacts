package com.showmiso.swipecontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.showmiso.swipecontacts.model.Contact
import kotlinx.android.synthetic.main.view_contact_card.view.*

class TrashAdapter : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {
    private var contactsList = ArrayList<Contact>()

    class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.txt_name.text = contact.name
            itemView.txt_phone.text = contact.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_trash_item, parent, false)
        return TrashViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
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