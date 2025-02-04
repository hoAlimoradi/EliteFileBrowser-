package com.alimoradi.elitefilebrowser.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Request
import okhttp3.Headers
import okhttp3.MediaType
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

internal class NetworkDownloaderImpl(
    private val okHttpClientLazy: Lazy<OkHttpClient>
) : NetworkDownloader {

    override fun postDownloadSync(
        url: String,
        headers: Map<String, String>,
        jsonObject: JSONObject,
        outputJavaFile: File,
        listener: NetworkManager.DownloadProgressListener
    ): String? {
        val requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString())
        val request = Request.Builder()
            .url(url)
            .headers(Headers.of(headers))
            .post(requestBody)
            .build()
        val call = okHttpClientLazy.value.newCall(request)
        try {
            val response = call.execute()
            val code = response.code()
            if (code != 200 && code != 201) {
                return null
            }
            if (outputJavaFile.exists()) {
                outputJavaFile.delete()
            }
            val fileCreated = outputJavaFile.createNewFile()
            if (!fileCreated) {
                Log.e("jm/debug", "Download error 1/3 - File creation failed")
                return null
            }
            var inputStream: InputStream? = null
            try {
                val body = response.body() ?: return null
                inputStream = body.byteStream()
                val buff = ByteArray(1_024 * 4)
                var downloaded: Long = 0
                val target = body.contentLength()
                val output = FileOutputStream(outputJavaFile)
                listener.onDownloadProgress(
                    0L,
                    target
                )
                while (true) {
                    val contentRead = inputStream!!.read(buff)
                    if (contentRead == -1) {
                        break
                    }
                    output.write(buff, 0, contentRead)
                    downloaded += contentRead.toLong()
                    listener.onDownloadProgress(
                        downloaded,
                        target
                    )
                }
                output.flush()
                output.close()
                val succeeded = downloaded == target
                if (!succeeded) {
                    Log.e("jm/debug", "downloaded != target: $downloaded != $target")
                }
                val responseJson = JSONObject()
                responseJson.put("succeeded", succeeded)
                return responseJson.toString()
            } catch (e: IOException) {
                Log.e("jm/debug", "Download error 2/3", e)
                return null
            } finally {
                inputStream?.close()
            }
        } catch (e: IOException) {
            Log.e("jm/debug", "Download error 3/3", e)
            return null
        }
    }

    companion object {

        private val MEDIA_TYPE_JSON = MediaType.parse(
            "application/json; charset=utf-8"
        )
    }
}
