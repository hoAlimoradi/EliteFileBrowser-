@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.server.file_repository

import com.alimoradi.file_api.File
import com.alimoradi.file_api.FileTest
import org.junit.Test
import org.junit.Assert

class FileRepositoryImplTest {

    @Test
    fun loadOneFolder() {
        // Given
        val classLoader = FileRepositoryImplTest::class.java.classLoader
        val resource = classLoader!!.getResource("file-repository-metadata-one-folder.json")
        Assert.assertNotNull(resource)
        val fileRepositoryMetadata = java.io.File(resource.path)
        val folderContainer = fileRepositoryMetadata.parentFile
        val fileRepository = FileRepositoryImpl(
            fileRepositoryMetadata,
            folderContainer
        )

        // When
        val files = fileRepository.get()

        // Then
        Assert.assertEquals(1, files.size)
        val file = files[0]
        FileTest.areEquals(
            File.create(
                "7136454a-291d-450d-a5eb-1ef8bf5850fc",
                "/music",
                "/",
                true,
                "music",
                0,
                0
            ),
            file
        )
    }
}
