package com.alimoradi.server.window

interface MainContract {

    interface UserAction {

        fun onStartClicked()

        fun onStopClicked()

        fun onPullSubFoldersClicked()
    }

    interface Screen
}
