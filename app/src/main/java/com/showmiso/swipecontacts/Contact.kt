package com.showmiso.swipecontacts

data class Contact(
    val Id: Long?,
    val thumbnailId: Long?,
    val fullName: String,
    val phoneNumber: String,
    val email: String? = "",
    val deleted: Boolean = false
    ) {
}