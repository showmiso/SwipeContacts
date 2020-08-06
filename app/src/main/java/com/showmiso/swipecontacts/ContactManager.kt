package com.showmiso.swipecontacts

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.showmiso.swipecontacts.model.Contact
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ContactManager (
    private val context: Context
) : Presenter {
    private val cr: ContentResolver = context.contentResolver
    private val disposables = CompositeDisposable()

    override fun onCreate() {
    }

    override fun onDestroy() {
        disposables.clear()
    }

    fun getContactAll(contactAdapter: ContactAdapter) {
        getInfoObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Log.d("getContactAll", "List LENGTH " + it.size)
                contactAdapter.addList(it)
            }
            .subscribe( {

            }, {
                Log.d("Failed", "ERROR " + it.localizedMessage)
            }
            )
            .addTo(disposables)
    }

    fun getContactAll(contactListCallback: (ArrayList<Contact>) -> Unit) {
        getInfoObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    contactListCallback(it)
                },{
                    Log.d("getContactAll", "ERROR " + it.localizedMessage)
                }
            )
            .addTo(disposables)
    }

    private fun getInfoObservable(): Observable<ArrayList<Contact>> {
        val displayName = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        val filter = "$displayName NOT LIKE '%@%'"
        val order = String.format("%1\$s COLLATE NOCASE", displayName)
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            displayName,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        val contactsList = ArrayList<Contact>()
        return Observable.just(
            cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                filter,
                null,
                order
            )
        )
            .map { cursor ->
                cursor.moveToFirst()
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(displayName))
                    val hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val contact = Contact(id, name, hasPhone > 0)
                    val contactObservable = getContactInfoOfPhoneEmailUri(contact)
                        .map {
                            contactsList.add(it)
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                        .addTo(disposables)
                } while (cursor.moveToNext())
                cursor.close()
                contactsList
            }
            .subscribeOn(Schedulers.io())
    }

    private fun getContactInfoOfPhoneEmailUri(contact: Contact): Observable<Contact> {
        val id = contact.id
        val phoneCursorObservable = Observable.just(
            cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )
        )
            .map { cp ->
                cp.moveToFirst()
                val phone =
                    cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                cp.close()
                phone
            }
            .onErrorReturn {
                ""
            }
            .subscribeOn(Schedulers.io())

        val emailCursorObservable = Observable.just(
            cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )
        )
            .map { ce ->
                ce.moveToFirst()
                val email =
                    ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                ce.close()
                email
            }
            .onErrorReturn {
                ""
            }
            .subscribeOn(Schedulers.io())

        val uriCursorObservable: Observable<Uri?> = Observable.just(
            cr.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + "=" + id + " AND " +
                        ContactsContract.Data.MIMETYPE + "='" +
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
                null,
                null
            )
        )
            .map { cu ->
                cu.moveToFirst()
                val person = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI, id.toLong()
                )
                val uri = Uri.withAppendedPath(
                    person,
                    ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                )
                cu.close()
                uri
            }
            .onErrorReturn {
                null
            }
            .subscribeOn(Schedulers.io())

        val resultFunction3 = Function3<String, String, Uri?, Contact> {
                resultPhone, resultEmail, resultUri ->
            contact.phone = resultPhone
            contact.email = resultEmail
            contact.uri = resultUri
            contact
        }

        return Observable.zip(
            phoneCursorObservable,
            emailCursorObservable,
            uriCursorObservable,
            resultFunction3
        )
            .subscribeOn(Schedulers.io())
    }

    fun getInfo2(): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val cr = context.contentResolver
        val displayName = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        val filter = "$displayName NOT LIKE '%@%'"
        val order = String.format("%1\$s COLLATE NOCASE", displayName)
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            displayName,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            filter,
            null,
            order
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // get the contact's information
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(displayName))
                val hasPhone =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                // get the user's email address
                var email: String = ""
                val ce = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                if (ce != null && ce.moveToFirst()) {
                    email =
                        ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    ce.close()
                }

                // get the user's phone number
                var phone: String = ""
                if (hasPhone > 0) {
                    val cp = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (cp != null && cp.moveToFirst()) {
                        phone =
                            cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        cp.close()
                    }
                }

                // get the user's photo
                var uri: Uri? = null
                val su = ContactsContract.Data.CONTACT_ID + "=" + id + " AND " +
                        ContactsContract.Data.MIMETYPE + "='" +
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'"
                val cu = cr.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    su,
                    null,
                    null
                )
                if (cu != null && cu.moveToFirst()) {
                    val person = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI, id.toLong()
                    )
                    uri = Uri.withAppendedPath(
                        person,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                    )
                    cu.close()
                }

//                if (phone.isNotEmpty()) {
                val contact = Contact(
                    // TODO. 함수 수정중
                    id,
                    name,
                    phone,
                    email,
                    uri
                )
                contactsList.add(contact)
//                }
            } while (cursor.moveToNext())

            // clean up cursor
            cursor.close()
        }

        return contactsList
    }

}