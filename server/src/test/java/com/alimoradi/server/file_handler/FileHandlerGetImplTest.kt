@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.server.file_handler

import com.alimoradi.server.authorization.AuthorizationManager
import com.alimoradi.server.file_repository.FileRepository
import com.alimoradi.server.log.LogManager
import io.ktor.http.Headers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FileHandlerGetImplTest {

    @Mock
    private lateinit var fileRepository: FileRepository
    @Mock
    private lateinit var logManager: LogManager
    @Mock
    private lateinit var authorizationManager: AuthorizationManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getLogCall() {
        // Given
        val fileHandlerGet = createInstanceToTest()

        // When
        fileHandlerGet.get(Headers.Empty)

        // Then
        Mockito.verify(logManager).d("FileHandlerGet", "get()")
    }

    private fun createInstanceToTest(): FileHandlerGet {
        return FileHandlerGetImpl(
            fileRepository,
            logManager,
            authorizationManager
        )
    }
}
