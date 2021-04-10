package com.alimoradi.elitefilebrowser.developer

interface DeveloperManager {

    fun isDeveloperMode(): Boolean

    fun setDeveloperMode(enable: Boolean)

    fun registerDeveloperModeListener(listener: DeveloperModeListener)

    fun unregisterDeveloperModeListener(listener: DeveloperModeListener)

    interface DeveloperModeListener {

        fun onDeveloperModeChanged()
    }
}
