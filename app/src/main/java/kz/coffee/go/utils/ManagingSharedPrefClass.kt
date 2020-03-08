package kz.coffee.go.utils

import android.content.Context

const val SHARED_PREFS_FILE_NAME = "shared_prefs_file_name"
const val SHARED_PREFS_CITY_NAME = "shared_prefs_city_name"

object ManagingSharedPrefClass {
    fun readSharedSetting(
        ctx: Context,
        settingName: String?,
        defaultValue: String?
    ): String? {
        val sharedPref = ctx.getSharedPreferences(
            SHARED_PREFS_FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPref.getString(settingName, defaultValue)
    }

    fun saveSharedSetting(
        ctx: Context,
        settingName: String?,
        settingValue: String?
    ) {
        val sharedPref = ctx.getSharedPreferences(
            SHARED_PREFS_FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString(settingName, settingValue)
        editor.apply()
    }
}