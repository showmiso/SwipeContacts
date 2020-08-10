package com.showmiso.swipecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_license.*

class LicenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)

        initView()
    }

    private fun initView() {
        btn_back.setOnClickListener(onClickListener)
        cbt_license_1.setOnClickListener(onClickListener)
        cbt_license_2.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
            R.id.cbt_license_1 -> {
                layout_license_1.visibility = if (cbt_license_1.isChecked) View.VISIBLE else View.GONE
            }
            R.id.cbt_license_2 -> {
                layout_license_2.visibility = if (cbt_license_2.isChecked) View.VISIBLE else View.GONE
            }
        }
    }
}