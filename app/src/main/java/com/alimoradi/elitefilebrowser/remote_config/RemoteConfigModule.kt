@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.remote_config

import com.alimoradi.elitefilebrowser.main.ApplicationGraph
import com.alimoradi.elitefilebrowser.main_thread.MainThreadPost
import com.alimoradi.elitefilebrowser.update.UpdateManager

/**
 * A module for the remote config .
 */
class RemoteConfigModule {

    fun createRemoteConfig(): RemoteConfig {
        val mainThreadPost = ApplicationGraph.getMainThreadPost()
        val updateManager = ApplicationGraph.getUpdateManager()
        return provideRemoteConfigStatic(
            updateManager,
            mainThreadPost
        )
    }

    companion object {

        private var instance: RemoteConfig? = null

        @JvmStatic
        fun provideRemoteConfigStatic(
            updateManager: UpdateManager,
            mainThreadPost: MainThreadPost
        ): RemoteConfig {
            if (instance == null) {
                instance = RemoteConfigImpl(
                    updateManager,
                    mainThreadPost
                )
            }
            return instance!!
        }
    }
}
