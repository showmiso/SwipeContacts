package com.showmiso.swipecontacts.model

data class Contact(
    val Id: Long?,
    val thumbnail: Long?,
    val name: String,
    val phone: String,
    val email: String? = "",
    val deleted: Boolean = false
    ) {
}