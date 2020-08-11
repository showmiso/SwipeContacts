package com.showmiso.swipecontacts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.showmiso.swipecontacts.utils.AdMobManager
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initUI()
    }

    /**
     * 카드에 표시할 사항 제한
     * 지금까지 삭제한 연락처 개수
     *
     * 리스트 형태
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
        layout_license.setOnClickListener(onClickListener)
        txt_version.text = BuildConfig.VERSION_NAME
//        val ss = SpannableString(btn_license.text)
//        ss.setSpan(UnderlineSpan(), 0, ss.length, 0)
//        btn_license.text = ss

        val banner = AdMobManager(
            this,
            layout_ad_view_container,
            SettingActivity::class.simpleName!!
        )
        banner.initBanner()
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
            R.id.layout_license -> {
                startActivity(Intent(this, LicenseActivity::class.java))
            }
        }
    }
}
