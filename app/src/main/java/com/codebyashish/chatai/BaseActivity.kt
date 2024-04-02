package com.codebyashish.chatai

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.codebyashish.chatai.utility.KeyConstants
import com.codebyashish.chatai.utility.PrefsUtil
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.google.android.material.color.HarmonizedColors
import com.google.android.material.color.HarmonizedColorsOptions

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        val runAsSuperClass = savedInstanceState != null && savedInstanceState.getBoolean(
            KeyConstants.EXTRA.RUN_AS_SUPER_CLASS,
            false
        )
        if (runAsSuperClass) {
            super.onCreate(savedInstanceState)
            return
        }
        val sharedPrefs: SharedPreferences = PrefsUtil(this).checkForMigrations().sharedPrefs
        val modeNight = sharedPrefs.getInt(KeyConstants.PREF.MODE, KeyConstants.DEF.MODE)
        var uiMode = resources.configuration.uiMode
        when (modeNight) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                uiMode = Configuration.UI_MODE_NIGHT_NO
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            AppCompatDelegate.MODE_NIGHT_YES -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                uiMode = Configuration.UI_MODE_NIGHT_YES
            }
        }
        AppCompatDelegate.setDefaultNightMode(modeNight)
        val resBase = baseContext.resources
        val configBase = resBase.configuration
        configBase.uiMode = uiMode
        resBase.updateConfiguration(configBase, resBase.displayMetrics)
        val resApp = applicationContext.resources
        val configApp = resApp.configuration
        resApp.updateConfiguration(configApp, resources.displayMetrics)
        when (sharedPrefs.getString(KeyConstants.PREF.THEME, KeyConstants.DEF.THEME)) {
            KeyConstants.THEME.GREEN -> setTheme(R.style.Theme_ChatGPT_Green)
            else -> if (DynamicColors.isDynamicColorAvailable()) {
                DynamicColors.applyToActivityIfAvailable(
                    this,
                    DynamicColorsOptions.Builder().setOnAppliedCallback { activity: Activity? ->
                        HarmonizedColors.applyToContextIfAvailable(
                            this, HarmonizedColorsOptions.createMaterialDefaults()
                        )
                    }.build()
                )
            } else {
                setTheme(R.style.Theme_ChatGPT_Green)
            }
        }


        val bundleInstanceState = intent.getBundleExtra(KeyConstants.EXTRA.INSTANCE_STATE)
        super.onCreate(bundleInstanceState ?: savedInstanceState)
    }

     open fun toolbarInit(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_segment_24)
    }
    open fun toolbarInit2(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowTitleEnabled(false)

    }


}