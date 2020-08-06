package com.showmiso.swipecontacts.presenter

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.showmiso.swipecontacts.ContactAdapter
import com.showmiso.swipecontacts.model.Contact
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.Contract

class ContactPresenter(
    private val context: Context
) : Presenter {
    private val cr: ContentResolver = context.contentResolver
    private val disposables = CompositeDisposable()
    private val UPDATE_SIZE = 10

    override fun onCreate() {}

    override fun onDestroy() {
        disposables.clear()
    }

    fun deleteContact(contact: Contact) {
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts._ID + "=" + contact.id,
            null,
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                val uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                    lookupKey
                )
                cr.delete(uri, ContactsContract.Contacts._ID + "=" + contact.id, null)
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    fun deleteContactList(contactList: ArrayList<Contact>) {

    }

    fun getContactAll(contactAdapter: ContactAdapter) {
        getContactObservable(contactAdapter)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                contactAdapter.addList(it)
                Log.d("getContactAll", "contactAdapter SIZE : ${contactAdapter.itemCount}")
            }
            .subscribe({}, {
                Log.d("Failed", "ERROR " + it.localizedMessage)
            }, {
                Toast.makeText(context, "${contactAdapter.itemCount} 개의 연락처를 가져왔습니다.", Toast.LENGTH_SHORT).show()
            }
            )
            .addTo(disposables)
    }

    private fun getContactObservable(contactAdapter: ContactAdapter): Observable<ArrayList<Contact>> {
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
                    val hasPhone =
                        cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val contact = Contact(id, name, hasPhone > 0)
                    getContactOfPhoneEmailUriObservable(contact)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map {
                            contactsList.add(it)
                            if (contactsList.size > UPDATE_SIZE) {
                                contactsList.shuffle()
                                contactAdapter.addList(contactsList)
                                Log.d("getInfoObservable", "contactAdapter SIZE : ${contactAdapter.itemCount}")
                                contactsList.clear()
                            }
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

    private fun getContactOfPhoneEmailUriObservable(contact: Contact): Observable<Contact> {
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

        val resultFunction3 =
            Function3<String, String, Uri?, Contact> { resultPhone, resultEmail, resultUri ->
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
}