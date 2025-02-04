package com.alimoradi.elitefilebrowser.network

import org.json.JSONObject

interface NetworkUploader {

    fun postUploadSync(
        url: String,
        headers: Map<String, String>,
        jsonObject: JSONObject,
        javaFile: java.io.File,
        listener: NetworkManager.UploadProgressListener
    ): String?
}
