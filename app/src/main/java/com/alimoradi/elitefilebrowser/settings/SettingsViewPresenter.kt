package com.alimoradi.elitefilebrowser.settings

import com.alimoradi.elitefilebrowser.developer.DeveloperManager
import com.alimoradi.elitefilebrowser.product.ProductManager
import com.alimoradi.elitefilebrowser.remote_config.RemoteConfig
import com.alimoradi.file_api_android.FileScopedStorageManager

class SettingsViewPresenter(
    private val screen: SettingsViewContract.Screen,
    private val developerManager: DeveloperManager,
    private val fileScopedStorageManager: FileScopedStorageManager,
    private val productManager: ProductManager,
    private val remoteConfig: RemoteConfig
) : SettingsViewContract.UserAction {

    private val developerModeListener = createDeveloperModeListener()
    private val remoteConfigListener = createRemoteConfigListener()
    private val productListener = createProductListener()

    override fun onAttached() {
        developerManager.registerDeveloperModeListener(developerModeListener)
        productManager.registerProductListener(productListener)
        remoteConfig.registerListener(remoteConfigListener)
        populate()
    }

    override fun onDetached() {
        developerManager.unregisterDeveloperModeListener(developerModeListener)
        productManager.unregisterProductListener(productListener)
        remoteConfig.unregisterListener(remoteConfigListener)
    }

    private fun populate() {
        val developerMode = developerManager.isDeveloperMode()
        val searchEnabled = remoteConfig.getSearchEnabled()
        val list = ArrayList<Any>()
        list.add(SettingsAdapter.SettingsTheme())
        list.add(SettingsAdapter.SettingsStorage())

        if (searchEnabled || developerMode) {
            list.add(SettingsAdapter.SettingsDynamic())
        }
        if (developerMode) {
            if (fileScopedStorageManager.isScopedStorage()) {
                list.add(SettingsAdapter.SettingsAndroidQ())
            }
            list.add(SettingsAdapter.SettingsDeveloper())
        }
        list.add(SettingsAdapter.SettingsAbout())
        screen.populate(list)
    }

    private fun createDeveloperModeListener() = object : DeveloperManager.DeveloperModeListener {
        override fun onDeveloperModeChanged() {
            populate()
        }
    }

    private fun createRemoteConfigListener() = object : RemoteConfig.RemoteConfigListener {
        override fun onInitialized() {
            populate()
        }
    }

    private fun createProductListener() = object : ProductManager.ProductListener {
        override fun onProductChanged() {
            populate()
        }
    }
}
