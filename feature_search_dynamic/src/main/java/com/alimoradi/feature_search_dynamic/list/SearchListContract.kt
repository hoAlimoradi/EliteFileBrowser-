@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.feature_search_dynamic.list

import com.alimoradi.file_api.File

interface SearchListContract {

    interface UserAction {

        fun onFileClicked(file: File)
    }

    interface Screen
}
