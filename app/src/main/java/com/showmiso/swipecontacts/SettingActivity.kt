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
     *
     * 리스트 형태
     *
     * 후원하기
     * - 개발자를 응원해주세요! 1달러
     * - 개발자에게 커피한잔을 후원해주세요! 4달러
     * - 개발자에게 치킨한마리를 후원해주세요! 18달러
     *
     * 오류문의
     * - showmiso@gmail.com
     *
     * 오픈소스 라이브러리
     *
     *
     *
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
