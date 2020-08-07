package com.showmiso.swipecontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.showmiso.swipecontacts.model.Contact
import com.showmiso.swipecontacts.utils.DrawableManager
import kotlinx.android.synthetic.main.view_contact_card.view.*

class ContactAdapter(
    private val layoutId: Int
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contactsList = ArrayList<Contact>()

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.txt_name.text = contact.name
            itemView.txt_phone.text = contact.phone
            if (contact.email == "") {
                itemView.txt_email.text = itemView.context.getString(R.string.contact_email_default)
                itemView.txt_email.setTextColor(itemView.context.getColor(R.color.colorSilver))
            } else {
                itemView.txt_email.text = contact.email
                itemView.txt_email.setTextColor(itemView.context.getColor(R.color.colorWhite))
            }

            val randomColor = DrawableManager.randomColor()
            if (contact.uri == null) {
                itemView.img_thumbnail.background.setTint(
                    itemView.context.getColor(randomColor[1])
                )
            } else {
                Glide.with(itemView.context)
                    .load(contact.uri)
                    .transform(CircleCrop())
                    .into(itemView.img_thumbnail)
            }

            itemView.layout_card.background.setTint(itemView.context.getColor(randomColor[0]))
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

    fun clear() {
        contactsList.clear()
        notifyDataSetChanged()
    }

    fun addList(list: ArrayList<Contact>) {
        val beforeSize = contactsList.size
        contactsList.addAll(list)
        notifyItemInserted(beforeSize)
    }
}