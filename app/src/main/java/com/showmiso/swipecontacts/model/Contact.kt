package com.showmiso.swipecontacts.model

import android.net.Uri

class Contact {
    var id: String = ""
    var name: String = ""
    var hasPhone: Boolean = false
    var phone: String = ""
    var email: String? = ""
    var uri: Uri? = null
    var deleted: Boolean = false

    constructor(
        id: String,
        name: String,
        hasPhone: Boolean
    ) {
        this.id = id
        this.name = name
        this.hasPhone = hasPhone
    }

    constructor(
        phone: String,
        email: String,
        uri: Uri?
    ) {
        this.phone = phone
        this.email = email
        this.uri = uri
    }

    constructor(
        id: String,
        name: String,
        phone: String,
        email: String,
        uri: Uri?
    ) {
        this.id = id
        this.name = name
        this.phone = phone
        this.email = email
        this.uri = uri
    }
}