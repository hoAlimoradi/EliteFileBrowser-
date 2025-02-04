@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.elitefilebrowser.file_storage_stats

import android.os.Build
import android.os.Environment
import android.os.StatFs
import kotlin.math.absoluteValue

class FileStorageStatsManagerImpl : FileStorageStatsManager {

    private val statFs by lazy {
        val rootAbsolutePath = Environment.getExternalStorageDirectory().absolutePath
        StatFs(rootAbsolutePath)
    }

    private val blockCount: Long by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.blockCountLong
        } else {
            @Suppress("DEPRECATION")
            statFs.blockCount.toLong()
        }
    }

    private val blockSize: Long by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.blockSizeLong
        } else {
            @Suppress("DEPRECATION")
            statFs.blockSize.toLong()
        }
    }

    private val availableBlocks: Long by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.availableBlocksLong
        } else {
            @Suppress("DEPRECATION")
            statFs.availableBlocks.toLong()
        }
    }

    override fun getTotalMemory(): Long {
        return (blockCount * blockSize).absoluteValue
    }

    override fun getFreeMemory(): Long {
        return (availableBlocks * blockSize).absoluteValue
    }

    override fun getBusyMemory(): Long {
        return getTotalMemory() - getFreeMemory()
    }
}
