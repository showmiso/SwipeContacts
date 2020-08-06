package com.showmiso.swipecontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.showmiso.swipecontacts.model.Contact
import kotlinx.android.synthetic.main.view_trash_item.view.*

class TrashAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {
    private var contactsList = ArrayList<Contact>()

    inner class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        fun bind(contact: Contact) {
            itemView.txt_name.text = contact.name
            itemView.txt_phone.text = contact.phone
            itemView.btn_restore.setOnClickListener(this@TrashViewHolder)
            itemView.btn_delete.setOnClickListener(this@TrashViewHolder)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_restore -> {
                    // 그냥 list에서 삭제
                }
                R.id.btn_delete -> {
                    // 연락처에서 삭제
                    val contact: Contact = itemView.tag as Contact
                    listener.onDeleteItem(contact)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_trash_item, parent, false)
        return TrashViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.itemView.tag = contact
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

    interface OnItemClickListener {
        fun onDeleteItem(contact: Contact)
    }
}