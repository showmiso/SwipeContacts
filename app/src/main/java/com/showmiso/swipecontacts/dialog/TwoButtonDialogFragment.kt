package com.showmiso.swipecontacts.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.showmiso.swipecontacts.R
import kotlinx.android.synthetic.main.dialog_two_button.*

class TwoButtonDialogFragment(
    private val strTitle: String = "",
    private val strMessage: String = "",
    private val listener: OnDialogClickListener
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_two_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (strTitle.isNotEmpty()) {
            txt_title.text = strTitle
        }
        if (strMessage.isNotEmpty()) {
            txt_message.text = strMessage
        }
        btn_cancel.setOnClickListener {
            dismiss()
        }
        btn_ok.setOnClickListener {
            listener.clickOk()
            dismiss()
        }
    }

    interface OnDialogClickListener {
        fun clickOk()
    }
}