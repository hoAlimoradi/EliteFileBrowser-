@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.file_details

import com.alimoradi.file_api.FileChildrenManager
import com.alimoradi.file_api.FileManager
import com.alimoradi.file_api.FileSizeManager
import com.alimoradi.file_api.FileShareManager
import com.alimoradi.file_api.FileSizeUtils

class FileDetailsPresenter(
    private val screen: FileDetailsContract.Screen,
    private var fileManager: FileManager,
    private var fileChildrenManager: FileChildrenManager,
    private var fileShareManager: FileShareManager,
    private var fileSizeManager: FileSizeManager
) : FileDetailsContract.UserAction {

    private var path: String? = null
    private val fileResultListener = createFileResultListener()
    private val fileSizeResultListener = createFileSizeResultListener()

    override fun onCreate(path: String) {
        fileManager.registerFileResultListener(fileResultListener)
        fileSizeManager.registerFileSizeResultListener(fileSizeResultListener)

        this.path = path
        fileManager.loadFile(path)
        fileSizeManager.loadSize(path)
        syncFile()
    }

    override fun onDestroy() {
        fileManager.unregisterFileResultListener(fileResultListener)
        fileSizeManager.unregisterFileSizeResultListener(fileSizeResultListener)
    }

    override fun onSetFileManagers(
        fileManager: FileManager,
        fileChildrenManager: FileChildrenManager,
        fileShareManager: FileShareManager,
        fileSizeManager: FileSizeManager
    ) {
        this.fileManager = fileManager
        this.fileChildrenManager = fileChildrenManager
        this.fileShareManager = fileShareManager
        this.fileSizeManager = fileSizeManager
    }

    override fun onSharedClicked() {
        fileShareManager.share(path!!)
    }

    private fun syncFile() {
        val path = if (path == null) {
            screen.hideShareButton()
            return
        } else {
            path!!
        }
        screen.setPathText(path)
        val fileResult = fileManager.getFile(path)
        fileResult.file?.let {
            screen.setNameText(it.name)
        }
        val fileSizeResult = fileSizeManager.getSize(path)
        val sizeLong = fileSizeResult.size
        val sizeString = FileSizeUtils.humanReadableByteCount(sizeLong)
        screen.setSizeText(sizeString)
        val shareSupported = fileShareManager.isShareSupported(path)
        if (shareSupported) {
            screen.showShareButton()
        } else {
            screen.hideShareButton()
        }
    }

    private fun createFileResultListener() = object : FileManager.FileResultListener {
        override fun onFileResultChanged(path: String) {
            if (path != this@FileDetailsPresenter.path) {
                return
            }
            syncFile()
        }
    }

    private fun createFileSizeResultListener() = object : FileSizeManager.FileSizeResultListener {
        override fun onFileSizeResultChanged(path: String) {
            if (path != this@FileDetailsPresenter.path) {
                return
            }
            syncFile()
        }
    }
}
