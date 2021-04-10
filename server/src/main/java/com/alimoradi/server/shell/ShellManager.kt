package com.alimoradi.server.shell

interface ShellManager {

    fun execute(command: String, block: (result: String) -> Unit)

    fun isWindows(): Boolean
}