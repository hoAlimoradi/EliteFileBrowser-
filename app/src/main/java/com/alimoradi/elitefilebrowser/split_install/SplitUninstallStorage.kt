@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.split_install

interface SplitUninstallStorage {

    fun isUninstallTrigger(name: String): Boolean

    fun setUninstallTrigger(name: String, trigger: Boolean)
}
