@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.file_list_row

import android.os.Build
import android.os.Environment
import android.util.Log
import com.alimoradi.elitefilebrowser.audio.AudioManager
import com.alimoradi.elitefilebrowser.file_provider.FileProvider
import com.alimoradi.elitefilebrowser.screen.ScreenManager
import com.alimoradi.elitefilebrowser.theme.ThemeManager
import com.alimoradi.elitefilebrowser.toast.ToastManager
import com.alimoradi.file_api.*

class FileListRowPresenter(
    private val screen: FileListRowContract.Screen,
    private var fileProvider: FileProvider,
    private var fileCopyCutManager: FileCopyCutManager,
    private var fileZipManager: FileZipManager,
    private var fileDeleteManager: FileDeleteManager,
    private var fileRenameManager: FileRenameManager,
    private var fileSizeManager: FileSizeManager,
    private val audioManager: AudioManager,
    private val screenManager: ScreenManager,
    private val themeManager: ThemeManager,
    private val toastManager: ToastManager,
    private val fileString: String,
    private val directoryString: String
) : FileListRowContract.UserAction {

    private val playListener = createPlayListener()
    private val themeListener = createThemeListener()
    private val fileSizeResultListener = createFileSizeResultListener()
    private var file: File? = null

    override fun onAttached() {
        audioManager.registerPlayListener(playListener)
        synchronizeRightIcon()
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
        fileSizeManager.registerFileSizeResultListener(fileSizeResultListener)
        syncSubtitle()
    }

    override fun onDetached() {
        audioManager.unregisterPlayListener(playListener)
        themeManager.unregisterThemeListener(themeListener)
        fileSizeManager.unregisterFileSizeResultListener(fileSizeResultListener)
    }

    override fun onFileChanged(file: File, selectedPath: String?) {
        this.file = file
        screen.setTitle(file.name)
        val directory = file.directory
        screen.setSoundIconVisibility(directory)
        screen.setIcon(directory)
        val fileSizeResult = fileSizeManager.loadSize(file.path)
        syncSubtitle(file, fileSizeResult)
    }

    override fun onRowClicked() {
        screen.notifyRowClicked(file!!)
    }

    override fun onRowLongClicked() {
        screen.showOverflowPopupMenu()
        screen.notifyRowLongClicked(file!!)
    }

    override fun onCopyClicked() {
        fileCopyCutManager.copy(file!!.path)
    }

    override fun onCutClicked() {
        fileCopyCutManager.cut(file!!.path)
    }

    override fun onDeleteClicked() {
        screen.showDeleteConfirmation(file!!.name)
    }

    override fun onDeleteConfirmedClicked() {
        fileDeleteManager.delete(file!!.path)
    }

    override fun onRenameClicked() {
        screen.showRenamePrompt(file!!.name)
    }

    override fun onZipClicked() {


        /*file?.let {
            if (fileZipManager.isZip(it.path)) {
                toastManager.toast("File is zip now!")
            } else {
                val parentPath: String = java.io.File(it.path).absoluteFile.parent
                fileZipManager.zip(path= it.path, outputPath = parentPath, filename = it.name)
                toastManager.toast("File zip")
            }
        }*/


        val docsFolder =
            java.io.File(Environment.getExternalStorageDirectory().toString() + "/Documents")

        if (!docsFolder.exists()) {
             docsFolder.mkdir()
        }
        var destinationPath = ""

        file?.let {
            destinationPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                docsFolder.absolutePath
            } else {
                java.io.File(it.path).absoluteFile.parent
            }

            if (fileZipManager.isZip(it.path)) {
                toastManager.toast("File is zip now!")
            } else {
                fileZipManager.zip(path= it.path, outputPath = destinationPath, filename = it.name)
                toastManager.toast("File zip")
            }
        }

    }

    override fun onUnZipClicked() {

        val docsFolder =
            java.io.File(Environment.getExternalStorageDirectory().toString() + "/Documents")
        var isPresent = true
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir()
        }
        var destinationPath = ""

         file?.let {

            destinationPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                docsFolder.absolutePath
            } else {
                java.io.File(it.path).absoluteFile.parent

            }

            if ( (fileZipManager.isZip(it.path))) {

                fileZipManager.unzip(path= it.path, outputPath = destinationPath)
                toastManager.toast("File un zip ready")
            } else {
                toastManager.toast("File is not zip !")
            }
        }

    }

    override fun onRenameConfirmedClicked(fileName: String) {
        if (fileName.contains("/")) {
            toastManager.toast("File name should not contain /")
            return
        }
        fileRenameManager.rename(file!!.path, fileName)
    }

    override fun onDetailsClicked() {
        screenManager.startFileDetails(
            file!!.path,
            fileProvider
        )
    }

    override fun onOverflowClicked() {
        screen.showOverflowPopupMenu()
    }

    override fun onSetFileManagers(
        fileProvider: FileProvider,
        fileCopyCutManager: FileCopyCutManager,
        fileDeleteManager: FileDeleteManager,
        fileRenameManager: FileRenameManager,
        fileSizeManager: FileSizeManager
    ) {
        this.fileProvider = fileProvider
        this.fileDeleteManager = fileDeleteManager
        this.fileCopyCutManager = fileCopyCutManager
        this.fileRenameManager = fileRenameManager
        this.fileSizeManager = fileSizeManager
    }

    private fun synchronizeRightIcon() {
        if (file == null) {
            return
        }
        if (file!!.directory) {
            screen.setSoundIconVisibility(false)
            return
        }
        val sourcePath = audioManager.getSourcePath()
        if (sourcePath == null || file!!.path != sourcePath) {
            screen.setSoundIconVisibility(false)
            return
        }
        val playing = audioManager.isPlaying()
        if (playing) {
            screen.setSoundIconVisibility(true)
        } else {
            screen.setSoundIconVisibility(false)
        }
    }

    private fun syncSubtitle() {
        val subtitle = file?.let {
            val sizeResult = fileSizeManager.getSize(it.path)
            syncSubtitle(it, sizeResult)
            return
        } ?: "No file"
        screen.setSubtitle(subtitle)
    }

    private fun syncSubtitle(
        file: File,
        fileSizeResult: FileSizeResult
    ) {
        val subtitle = createSubtitle(file, fileSizeResult)
        screen.setSubtitle(subtitle)
    }

    private fun createSubtitle(
        file: File,
        fileSizeResult: FileSizeResult
    ): String {
        val fileTypeString = if (file.directory) {
            directoryString
        } else {
            fileString
        }
        return if (fileSizeResult.status != FileSizeResult.Status.LOADED_SUCCEEDED) {
            fileTypeString
        } else {
            val sizeLong = fileSizeResult.size
            val sizeString = FileSizeUtils.humanReadableByteCount(sizeLong)
            "$fileTypeString - $sizeString"
        }
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setTitleTextColorRes(theme.textPrimaryColorRes)
        screen.setSubtitleTextColorRes(theme.textSecondaryColorRes)
        screen.setCardBackgroundColorRes(theme.cardBackgroundColorRes)
    }

    private fun createPlayListener() = object : AudioManager.PlayListener {
        override fun onPlayPauseChanged() {
            synchronizeRightIcon()
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    private fun createFileSizeResultListener() = object : FileSizeManager.FileSizeResultListener {
        override fun onFileSizeResultChanged(path: String) {
            syncSubtitle()
        }
    }

    companion object {

        @JvmStatic
        private fun isSelected(filePath: String, selectedPath: String?): Boolean {
            val startWith = selectedPath?.startsWith(filePath) ?: false
            if (startWith && selectedPath != null) {
                val removePrefix = selectedPath.removePrefix(filePath)
                if (removePrefix != "" && !removePrefix.startsWith('/')) {
                    return false
                }
            }
            return startWith
        }
    }
}
