@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.settings_full_version

import androidx.annotation.ColorRes

interface SettingsFullVersionContract {

    interface Screen {

        fun setSectionColor(@ColorRes colorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setPromotionGradient(dark: Boolean)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()
    }
}
