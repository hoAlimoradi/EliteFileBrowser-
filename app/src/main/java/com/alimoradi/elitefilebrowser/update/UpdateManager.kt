package com.alimoradi.elitefilebrowser.update

interface UpdateManager {

    fun isFirstRunAfterUpdate(): Boolean

    fun isFirstRun(): Boolean
}
