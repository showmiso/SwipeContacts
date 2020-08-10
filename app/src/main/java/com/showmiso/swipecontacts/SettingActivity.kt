package com.showmiso.swipecontacts

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    private lateinit var adView: AdView

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val density = outMetrics.density
            var adWidthPixels = layout_ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initUI()
        initAdMob()
    }

    private fun initAdMob() {
        MobileAds.initialize(this) {}
        adView = AdView(this)
        layout_ad_view_container.addView(adView)
        adView.adUnitId = getString(R.string.google_ad_mob_test_id)
        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()
        adView.loadAd(adRequest)
    }

    /**
     * 카드에 표시할 사항 제한?
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
