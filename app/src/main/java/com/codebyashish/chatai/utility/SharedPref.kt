package com.codebyashish.chatai.utility

import android.content.Context
import android.content.SharedPreferences
import com.codebyashish.chatai.BuildConfig

open class SharedPref {
    private lateinit var sharedPreferences : SharedPreferences

    companion object {
        fun setString(context: Context, key: String, value: String) {
            val editor: SharedPreferences.Editor = getSharedPreference(context).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getString(context: Context, key: String, value: String) : String {

            return getSharedPreference(context).getString(key, value).toString()
        }

        private fun getSharedPreference(context: Context) : SharedPreferences
        {
            return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        }
    }



}