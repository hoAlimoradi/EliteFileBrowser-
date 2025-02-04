package com.alimoradi.file_api_online_android

import com.alimoradi.file_api.FileResult
import com.alimoradi.file_api.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileOnlineManagerAndroid(
    private val fileOnlineApi: FileOnlineApi
) : FileManager {

    private val fileResultMap = HashMap<String, FileResult>()
    private val fileResultListeners = ArrayList<FileManager.FileResultListener>()

    override fun loadFile(path: String, forceRefresh: Boolean): FileResult {
        if (fileResultMap.contains(path)) {
            val status = fileResultMap[path]!!.status
            if (status == FileResult.Status.LOADING) {
                return getFile(path)
            }
            if (status == FileResult.Status.LOADED_SUCCEEDED && !forceRefresh) {
                return getFile(path)
            }
        }
        fileResultMap[path] = FileResult.createLoading(path)
        GlobalScope.launch(Dispatchers.Default) {
            val fileResult = loadFileSync(path)
            GlobalScope.launch(Dispatchers.Main) {
                fileResultMap[path] = fileResult
                for (listener in fileResultListeners) {
                    listener.onFileResultChanged(path)
                }
            }
        }
        return getFile(path)
    }

    override fun getFile(path: String): FileResult {
        if (fileResultMap.contains(path)) {
            return fileResultMap[path]!!
        }
        val fileResultUnloaded = FileResult.createUnloaded(path)
        fileResultMap[path] = fileResultUnloaded
        return fileResultUnloaded
    }

    override fun registerFileResultListener(listener: FileManager.FileResultListener) {
        if (fileResultListeners.contains(listener)) {
            return
        }
        fileResultListeners.add(listener)
    }

    override fun unregisterFileResultListener(listener: FileManager.FileResultListener) {
        fileResultListeners.remove(listener)
    }

    fun refresh(path: String) {
        if (!fileResultMap.containsKey(path)) {
            return
        }
        loadFile(path, true)
    }

    private fun loadFileSync(path: String): FileResult {
        val serverResponseFile = fileOnlineApi.get(path)
            ?: return FileResult.createErrorNetwork(path)
        return FileResult.createLoaded(path, serverResponseFile.file)
    }
}
