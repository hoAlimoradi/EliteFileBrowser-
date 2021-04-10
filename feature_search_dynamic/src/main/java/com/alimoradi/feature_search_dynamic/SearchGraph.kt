@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.feature_search_dynamic

import android.annotation.SuppressLint
import com.alimoradi.elitefilebrowser.main.ApplicationGraph

class SearchGraph {

    private val fileSearchManagerInternal by lazy {
        ApplicationGraph.getFileModule().createFileSearchManager()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var graph: SearchGraph? = null

        fun getFileSearchManager() = getGraph().fileSearchManagerInternal

        private fun getGraph(): SearchGraph {
            if (graph == null) {
                graph = SearchGraph()
            }
            return graph!!
        }
    }
}
