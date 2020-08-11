package com.showmiso.swipecontacts.utils

import android.app.Activity
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.showmiso.swipecontacts.BuildConfig
import com.showmiso.swipecontacts.R
import com.showmiso.swipecontacts.SettingActivity
import com.showmiso.swipecontacts.TrashActivity

class AdMobManager(
    private val activity: Activity,
    private val layoutContainer: ViewGroup,
    private val strName: String
) {
    private lateinit var adView: AdView
    private val adSize: AdSize
        get() {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val density = outMetrics.density
            var adWidthPixels = layoutContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }

    fun initBanner() {
        MobileAds.initialize(activity) {}
        adView = AdView(activity)
        layoutContainer.addView(adView)
        adView.adUnitId = getBannerId()
        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()
        adView.loadAd(adRequest)
    }

    private fun getBannerId(): String {
        if (BuildConfig.DEBUG) {
            return activity.getString(R.string.google_ad_mob_banner_test_id)
        } else {
            when (strName) {
                SettingActivity::class.simpleName -> {
                    return activity.getString(R.string.google_ad_mob_banner_setting_id)
                }
                TrashActivity::class.simpleName -> {
                    return activity.getString(R.string.google_ad_mob_banner_trash_id)
                }
            }
        }
        return ""
    }
}