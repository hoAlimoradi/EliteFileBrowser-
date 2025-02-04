package com.alimoradi.file_api_online_android

import com.alimoradi.file_api.File
import com.alimoradi.file_api_online.FileOnlineLoginManager
import com.alimoradi.file_api_online.response_json.ServerResponse
import com.alimoradi.file_api_online.response_json.ServerResponseFile
import com.alimoradi.file_api_online.response_json.ServerResponseFiles
import org.json.JSONObject
import kotlin.collections.HashMap

internal class FileOnlineApiImpl(
    private val fileOnlineApiNetwork: FileOnlineApiNetwork,
    private val fileOnlineLoginManager: FileOnlineLoginManager
) : FileOnlineApi {

    override fun get(): ServerResponseFiles? {
        val headers = createHeaders()
        val body = fileOnlineApiNetwork.getSync(
            "$API_DOMAIN/file",
            headers
        ) ?: return null
        val jsonObject = JSONObject(body)
        return ServerResponseFiles.fromJson(jsonObject)
    }

    override fun get(path: String): ServerResponseFile? {
        val headers = createHeaders()
        val body = fileOnlineApiNetwork.getSync(
            "$API_DOMAIN/file?path=$path",
            headers
        ) ?: return null
        val jsonObject = JSONObject(body)
        return ServerResponseFile.fromJson(jsonObject)
    }

    override fun getChildren(
        parentPath: String
    ): ServerResponseFiles? {
        val headers = createHeaders()
        val body = fileOnlineApiNetwork.getSync(
            "$API_DOMAIN/file?parent_path=$parentPath",
            headers
        ) ?: return null
        val jsonObject = JSONObject(body)
        return ServerResponseFiles.fromJson(jsonObject)
    }

    override fun getSize(
        path: String
    ): ServerResponse? {
        val headers = createHeaders()
        val body = fileOnlineApiNetwork.getSync(
            "$API_DOMAIN/file/size?path=$path",
            headers
        ) ?: return null
        val jsonObject = JSONObject(body)
        return ServerResponse.fromJson(jsonObject)
    }

    override fun post(
        file: File
    ) {
        val headers = createHeaders()
        val fileJsonObject = File.toJson(file)
        fileOnlineApiNetwork.postSync(
            "$API_DOMAIN/file",
            headers,
            fileJsonObject
        )
    }

    override fun postDownload(
        inputFilePath: String,
        outputJavaFile: java.io.File,
        listener: FileOnlineApi.DownloadProgressListener
    ) {
        val headers = createHeaders()
        val fileJsonObject = JSONObject()
        fileJsonObject.put(File.JSON_KEY_PATH, inputFilePath)
        fileOnlineApiNetwork.postDownloadSync(
            "$API_DOMAIN/file/download",
            headers,
            fileJsonObject,
            outputJavaFile,
            object : FileOnlineApiNetwork.DownloadProgressListener {
                override fun onDownloadProgress(current: Long, size: Long) {
                    listener.onDownloadProgress(inputFilePath, current, size)
                }
            }
        )
    }

    override fun postUpload(
        inputJavaFile: java.io.File,
        outputFile: File,
        listener: FileOnlineApi.UploadProgressListener
    ) {
        val headers = createHeaders()
        val fileJsonObject = File.toJson(outputFile)
        fileOnlineApiNetwork.postUploadSync(
            "$API_DOMAIN/file/upload",
            headers,
            fileJsonObject,
            inputJavaFile,
            object : FileOnlineApiNetwork.UploadProgressListener {
                override fun onUploadProgress(current: Long, size: Long) {
                    listener.onUploadProgress(outputFile, current, size)
                }
            }
        )
    }

    override fun delete(
        path: String
    ): Boolean {
        val headers = createHeaders()
        val fileJsonObject = JSONObject()
        fileJsonObject.put(File.JSON_KEY_PATH, path)
        val json = fileOnlineApiNetwork.deleteSync(
            "$API_DOMAIN/file",
            headers,
            fileJsonObject
        ) ?: return false
        val serverResponse = ServerResponse.fromJson(JSONObject(json))
        return serverResponse.succeeded
    }

    override fun rename(
        path: String,
        name: String
    ): Boolean {
        val headers = createHeaders()
        val fileJsonObject = JSONObject()
        fileJsonObject.put(File.JSON_KEY_PATH, path)
        fileJsonObject.put(File.JSON_KEY_NAME, name)
        val json = fileOnlineApiNetwork.postSync(
            "$API_DOMAIN/file/rename",
            headers,
            fileJsonObject
        )
        val serverResponse = ServerResponse.fromJson(JSONObject(json))
        return serverResponse.succeeded
    }

    override fun copy(pathInput: String, pathDirectoryOutput: String): Boolean {
        val headers = createHeaders()
        val fileJsonObject = JSONObject()
        fileJsonObject.put(File.JSON_KEY_PATH, pathInput)
        fileJsonObject.put("path_directory_output", pathDirectoryOutput)
        val json = fileOnlineApiNetwork.postSync(
            "$API_DOMAIN/file/copy",
            headers,
            fileJsonObject
        )
        val serverResponse = ServerResponse.fromJson(JSONObject(json))
        return serverResponse.succeeded
    }

    override fun cut(pathInput: String, pathDirectoryOutput: String): Boolean {
        val headers = createHeaders()
        val fileJsonObject = JSONObject()
        fileJsonObject.put(File.JSON_KEY_PATH, pathInput)
        fileJsonObject.put("path_directory_output", pathDirectoryOutput)
        val json = fileOnlineApiNetwork.postSync(
            "$API_DOMAIN/file/cut",
            headers,
            fileJsonObject
        )
        val serverResponse = ServerResponse.fromJson(JSONObject(json))
        return serverResponse.succeeded
    }

    private fun createHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        val token = fileOnlineLoginManager.createToken()
        headers["User-Agent"] = USER_AGENT
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Basic $token"
        return headers
    }

    companion object {

        private const val API_DOMAIN = "http://mercandalli.com/file-api"

        private const val USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"
    }
}
