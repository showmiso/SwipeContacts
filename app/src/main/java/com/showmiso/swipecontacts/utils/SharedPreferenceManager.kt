package com.showmiso.swipecontacts.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(
    private val context: Context
) {
    val prefs: SharedPreferences = context.getSharedPreferences(Constants.PREF_ON_BOARDING, 0)
    var onBoardingCheck: Boolean
        get() = prefs.getBoolean(Constants.PREF_ON_BOARDING_CHECK, false)
        set(value) = prefs.edit().putBoolean(Constants.PREF_ON_BOARDING_CHECK, value).apply()
}