package com.showmiso.swipecontacts.model

import android.net.Uri

data class Contact(
    val Id: Long?,
    val thumbnail: Long?,
    val name: String,
    val phone: String,
    val email: String? = "",
    val uri: Uri? = null,
    val deleted: Boolean = false
) {
}