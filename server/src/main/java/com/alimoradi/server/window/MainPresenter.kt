package com.alimoradi.server.window

import com.alimoradi.server.server.ServerManager
import com.alimoradi.server.shell.ShellManager

class MainPresenter(
    private val screen: MainContract.Screen,
    private val serverManager: ServerManager,
    private val shellManager: ShellManager,
    private val rootPath: String,
    private val pullSubRepositoryShellFile: java.io.File
) : MainContract.UserAction {

    override fun onStartClicked() {
        Thread {
            serverManager.start()
        }.start()
    }

    override fun onStopClicked() {
        serverManager.stop()
        System.exit(0)
    }

    override fun onPullSubFoldersClicked() {
        val absolutePath = pullSubRepositoryShellFile.absolutePath
        shellManager.execute("bash $absolutePath") {

        }
    }
}
