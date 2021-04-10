@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.feature_search_dynamic.list

import com.alimoradi.file_api.File
import com.alimoradi.file_api.FileOpenManager

class SearchListPresenter(
    private val screen: SearchListContract.Screen,
    private val fileOpenManager: FileOpenManager
) : SearchListContract.UserAction {

    override fun onFileClicked(file: File) {
        fileOpenManager.open(file.path)
    }
}
