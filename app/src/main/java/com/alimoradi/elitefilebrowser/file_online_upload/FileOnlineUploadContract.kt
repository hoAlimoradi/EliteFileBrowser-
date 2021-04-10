@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.file_online_upload

interface FileOnlineUploadContract {

    interface UserAction {

        fun onUploadClicked()

        fun onFileSelected(path: String, mime: String?)
    }

    interface Screen {

        fun setPath(text: String)

        fun quit()
    }
}
