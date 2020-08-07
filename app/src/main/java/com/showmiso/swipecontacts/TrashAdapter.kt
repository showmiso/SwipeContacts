package com.showmiso.swipecontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.showmiso.swipecontacts.model.Contact
import com.showmiso.swipecontacts.utils.DrawableManager
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
            if (contact.email == "") {
                itemView.txt_email.text = itemView.context.getString(R.string.contact_email_default)
                itemView.txt_email.setTextColor(itemView.context.getColor(R.color.colorGray))
            } else {
                itemView.txt_email.text = contact.email
                itemView.txt_email.setTextColor(itemView.context.getColor(R.color.colorBlack))
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

            itemView.btn_restore.setOnClickListener(this@TrashViewHolder)
            itemView.btn_delete.setOnClickListener(this@TrashViewHolder)
        }

        override fun onClick(v: View) {
            // restore or delete
            val contact: Contact = itemView.tag as Contact
            contactsList.remove(contact)
            notifyItemRemoved(adapterPosition)
            // 연락처에서 삭제
            listener.onClickItem(contact, v.id)

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

    fun updateContact(list: ArrayList<Contact>) {
        contactsList = list
        notifyDataSetChanged()
    }

    fun deleteAll() {
        contactsList.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClickItem(contact: Contact, layoutId: Int)
    }
}