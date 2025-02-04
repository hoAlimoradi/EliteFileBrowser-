package com.alimoradi.file_api

interface FileSizeManager {

    fun loadSize(
        path: String,
        forceRefresh: Boolean = false
    ): FileSizeResult

    fun setSize(
        path: String,
        size: Long
    )

    fun getSize(
        path: String
    ): FileSizeResult

    fun registerFileSizeResultListener(listener: FileSizeResultListener)

    fun unregisterFileSizeResultListener(listener: FileSizeResultListener)

    interface FileSizeResultListener {

        fun onFileSizeResultChanged(path: String)
    }
}
