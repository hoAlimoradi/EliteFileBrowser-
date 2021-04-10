package com.alimoradi.server.event

import com.alimoradi.feature_event_mercan.EventReceiver
import com.alimoradi.feature_event_mercan.EventSecure
import com.alimoradi.feature_aes.AesMode
import com.alimoradi.feature_aes.AesOpMode
import com.alimoradi.feature_aes.AesPadding
import com.alimoradi.server.main.ApplicationGraph
import java.io.File

class EventModule {

    private val eventManager by lazy { createEventManager() }

    fun createEventHandlerGet(): EventHandlerGet {
        val eventRepository = ApplicationGraph.getEventRepository()
        val logManager = ApplicationGraph.getLogManager()
        return EventHandlerGetImpl(
            eventManager,
            eventRepository,
            logManager
        )
    }

    fun createEventHandlerPost(): EventHandlerPost {
        val eventReceiver = createEventReceiver()
        val logManager = ApplicationGraph.getLogManager()
        return EventHandlerPostImpl(
            eventManager,
            eventReceiver,
            logManager
        )
    }

    fun createEventRepository(): EventRepository {
        val timeManager = ApplicationGraph.getTimeManager()
        val rootPath = ApplicationGraph.getRootPath()
        return EventRepositoryImpl(
            timeManager,
            File(rootPath)
        )
    }

    private fun createEventManager(): EventManager {
        val eventRepository = ApplicationGraph.getEventRepository()
        val timeManager = ApplicationGraph.getTimeManager()
        return EventManagerImpl(
            eventRepository,
            timeManager
        )
    }

    private fun createEventReceiver(): EventReceiver {
        val eventSecure = createEventSecure()
        return com.alimoradi.feature_event_mercan.EventModule(
            eventSecure
        ).createEventReceiver()
    }

    private fun createEventSecure(): EventSecure {
        val aesBase64Manager = ApplicationGraph.getAesBase64Manager()
        val mainArgument = ApplicationGraph.getMainArgument()
        return object : EventSecure {
            override fun crypt(message: String): String {
                return aesBase64Manager.getAesCrypter(
                    AesOpMode.CRYPT,
                    AesMode.GCM,
                    AesPadding.NO,
                    mainArgument.getEventSecureKey(),
                    mainArgument.getEventSecureIv()
                ).crypt(message)
            }

            override fun decrypt(message: String): String {
                return aesBase64Manager.getAesCrypter(
                    AesOpMode.DECRYPT,
                    AesMode.GCM,
                    AesPadding.NO,
                    mainArgument.getEventSecureKey(),
                    mainArgument.getEventSecureIv()
                ).decrypt(message)
            }
        }
    }
}
