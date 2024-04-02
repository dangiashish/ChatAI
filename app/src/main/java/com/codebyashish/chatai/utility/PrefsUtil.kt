package com.codebyashish.chatai.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.codebyashish.chatai.utility.KeyConstants.DEF
import com.codebyashish.chatai.utility.KeyConstants.PREF

class PrefsUtil(private val context: Context) {
    lateinit var sharedPrefs: SharedPreferences
        private set

    init {
        migrateToStorageContext()
    }

    private fun migrateToStorageContext() {
        val storageContext: Context
        val deviceContext = context.createDeviceProtectedStorageContext()
        val success = deviceContext.moveSharedPreferencesFrom(
            context, context.packageName + "_theme"
        )
        if (!success) {
            Log.w(TAG, "Failed to migrate shared preferences to storage context")
        }
        storageContext = deviceContext
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(storageContext)
    }

    fun checkForMigrations(): PrefsUtil {
        if (sharedPrefs!!.contains(PREF.NIGHT_MODE)) {
            try {
                sharedPrefs!!.getInt(PREF.NIGHT_MODE, DEF.NIGHT_MODE)
            } catch (e: ClassCastException) {
                removePreference(PREF.NIGHT_MODE)
            }
        }
        return this
    }

    private fun migrateString(keyOld: String, keyNew: String, def: String) {
        if (sharedPrefs!!.contains(keyOld)) {
            val editor = sharedPrefs!!.edit()
            try {
                val current = sharedPrefs!!.getString(keyOld, def)
                if (current != def) {
                    editor.remove(keyOld)
                    editor.putString(keyNew, current)
                }
            } catch (ignored: ClassCastException) {
                editor.remove(keyOld)
            }
            editor.apply()
        }
    }

    private fun migrateBoolean(keyOld: String, keyNew: String, def: Boolean) {
        if (sharedPrefs!!.contains(keyOld)) {
            val editor = sharedPrefs!!.edit()
            try {
                val current = sharedPrefs!!.getBoolean(keyOld, def)
                if (current != def) {
                    editor.remove(keyOld)
                    editor.putBoolean(keyNew, current)
                }
            } catch (ignored: ClassCastException) {
                editor.remove(keyOld)
            }
            editor.apply()
        }
    }

    private fun migrateInteger(keyOld: String, keyNew: String, def: Int) {
        if (sharedPrefs!!.contains(keyOld)) {
            val editor = sharedPrefs!!.edit()
            try {
                val current = sharedPrefs!!.getInt(keyOld, def)
                if (current != def) {
                    editor.remove(keyOld)
                    editor.putInt(keyNew, current)
                }
            } catch (ignored: ClassCastException) {
                editor.remove(keyOld)
            }
            editor.apply()
        }
    }

    private fun migrateFloat(keyOld: String, keyNew: String, def: Float) {
        if (sharedPrefs!!.contains(keyOld)) {
            val editor = sharedPrefs!!.edit()
            try {
                val current = sharedPrefs!!.getFloat(keyOld, def)
                if (current != def) {
                    editor.remove(keyOld)
                    editor.putFloat(keyNew, current)
                }
            } catch (ignored: ClassCastException) {
                editor.remove(keyOld)
            }
            editor.apply()
        }
    }

    private fun removePreference(key: String) {
        if (sharedPrefs!!.contains(key)) {
            sharedPrefs!!.edit().remove(key).apply()
        }
    }

    companion object {
        private val TAG = PrefsUtil::class.java.simpleName
    }
}
