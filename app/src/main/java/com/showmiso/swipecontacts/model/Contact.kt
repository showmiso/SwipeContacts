package com.showmiso.swipecontacts.model

import android.net.Uri

data class Contact(
    val Id: String,
    val name: String,
    val hasPhone: Boolean,
    val phone: String = "",
    val email: String? = "",
    val uri: Uri? = null,
    val deleted: Boolean = false
) {
}