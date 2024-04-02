package com.codebyashish.chatai.utility

import androidx.appcompat.app.AppCompatDelegate

class KeyConstants {

    companion object {
        const val API_KEY = "API_KEY"
    }

    object PREF {
        const val NIGHT_MODE = "night_mode"
        const val THEME = "app_theme"
        const val MODE = "mode"
    }

    object DEF {
        val NIGHT_MODE: Int = NIGHT_MODES.AUTO
        const val THEME = ""
        const val MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    object NIGHT_MODES {
        const val AUTO = -1
        const val ON = 1
        const val OFF = 0
    }

    object THEME {
        const val DYNAMIC = "dynamic"
        const val GREEN = "green"
    }

    object EXTRA {
        const val RUN_AS_SUPER_CLASS = "run_as_super_class"
        const val INSTANCE_STATE = "instance_state"
    }

    object CHAT_TYPE {
        const val AI = "ai"
        const val USER = "user"
        const val CLOSED = "closed"

    }
}
