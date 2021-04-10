package com.alimoradi.elitefilebrowser.update

import android.content.Context
import com.alimoradi.elitefilebrowser.main.ApplicationGraph

class UpdateModule(
    private val context: Context
) {

    fun createUpdateManager(): UpdateManager {
        val versionManager = ApplicationGraph.getVersionManager()
        val sharedPreferences = context.getSharedPreferences(
            UpdateManagerImpl.PREFERENCE_NAME, Context.MODE_PRIVATE)
        return UpdateManagerImpl(
            sharedPreferences,
            versionManager
        )
    }
}
