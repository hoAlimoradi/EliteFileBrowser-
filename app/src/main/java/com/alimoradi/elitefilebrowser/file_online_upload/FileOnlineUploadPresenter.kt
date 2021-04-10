@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.file_online_upload

import com.alimoradi.file_api_online_android.FileOnlineUploadManager
import com.alimoradi.file_api.File
import java.util.UUID

class FileOnlineUploadPresenter(
    private val screen: FileOnlineUploadContract.Screen,
    private val fileOnlineUploadManager: FileOnlineUploadManager
) : FileOnlineUploadContract.UserAction {

    private var path: String? = null

    override fun onUploadClicked() {
        if (path == null) {
            return
        }
        val inputJavaFile = java.io.File(path)
        val outputFile = File.create(
            UUID.randomUUID().toString(),
            "/${inputJavaFile.name}",
            "/",
            false,
            inputJavaFile.name,
            inputJavaFile.length(),
            inputJavaFile.lastModified()
        )
        fileOnlineUploadManager.upload(
            inputJavaFile,
            outputFile
        )
        screen.quit()
    }

    override fun onFileSelected(path: String, mime: String?) {
        this.path = path
        screen.setPath(path)
    }
}
