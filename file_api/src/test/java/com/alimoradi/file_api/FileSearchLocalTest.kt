package com.alimoradi.file_api

import com.alimoradi.file_api.FileSearchLocal.searchSync
import org.junit.Assert
import org.junit.Test

class FileSearchLocalTest {

    @Test
    fun searchSyncFindFile() {
        // Given
        val classLoader = javaClass.classLoader
        val resource = classLoader!!.getResource("file.html")
        val resourcePath = resource.path
        val rootPath = java.io.File(resourcePath).parentFile.absolutePath

        // When
        val paths = searchSync("toto", rootPath)

        // Then
        for (path in paths) {
            if (path.contains("toto.json")) {
                return
            }
            if (path.contains("file.html")) {
                Assert.fail()
            }
        }
        val ioFile = java.io.File(rootPath)
        Assert.fail("Fail on root path: ${ioFile.absolutePath}\n$paths")
    }
}
