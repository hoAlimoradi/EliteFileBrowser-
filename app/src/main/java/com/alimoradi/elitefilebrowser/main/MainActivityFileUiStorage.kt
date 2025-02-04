package com.alimoradi.elitefilebrowser.main

import androidx.annotation.IntDef

interface MainActivityFileUiStorage {

    @Section
    fun getCurrentFileUi(): Int

    fun setCurrentFileUi(@Section section: Int)

    companion object {

        @IntDef(
            SECTION_FILE_LIST,
            SECTION_FILE_COLUMN
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Section

        const val SECTION_FILE_LIST = 1
        const val SECTION_FILE_COLUMN = 2
    }
}
