package com.alimoradi.elitefilebrowser.network

import org.json.JSONObject

interface NetworkDownloader {

    fun postDownloadSync(
        url: String,
        headers: Map<String, String>,
        jsonObject: JSONObject,
        outputJavaFile: java.io.File,
        listener: NetworkManager.DownloadProgressListener
    ): String?
}
