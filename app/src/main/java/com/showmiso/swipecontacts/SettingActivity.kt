package com.showmiso.swipecontacts

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initUI()
    }

    /**
     * 카드에 표시할 사항 제한?
     * 지금까지 삭제한 연락처 개수
     */
    private fun initUI() {
        btn_back.setOnClickListener(onClickListener)
        btn_license.setOnClickListener(onClickListener)

        val ss = SpannableString(btn_license.text)
        ss.setSpan(UnderlineSpan(), 0, ss.length, 0)
        btn_license.text = ss
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
            R.id.btn_license -> {

            }
        }
    }
}
