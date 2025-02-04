package com.alimoradi.file_api_online

import com.alimoradi.file_api_online.utils.HashUtils

internal class FileOnlineLoginManagerImpl(
    private val fileOnlineLoginRepository: FileOnlineLoginRepository?,
    private val fileOnlineTokenCreator: FileOnlineTokenCreator
) : FileOnlineLoginManager {

    private var login: String? = null
    private var passwordSha1: String? = null
    private val listeners = ArrayList<FileOnlineLoginManager.LoginListener>()

    init {
        login = fileOnlineLoginRepository?.load("login")
        passwordSha1 = fileOnlineLoginRepository?.load("passwordSha1")
    }

    override fun setLogin(login: String) {
        this.login = login
        fileOnlineLoginRepository?.save("login", login)
        for (listener in listeners) {
            listener.onOnlineLogChanged()
        }
    }

    override fun setPassword(password: String) {
        this.passwordSha1 = HashUtils.sha1(password)
        fileOnlineLoginRepository?.save("passwordSha1", passwordSha1!!)
        for (listener in listeners) {
            listener.onOnlineLogChanged()
        }
    }

    override fun getLogin() = login

    override fun isLogged() = login != null && passwordSha1 != null

    override fun createToken() = fileOnlineTokenCreator.createToken(login!!, passwordSha1!!)

    override fun registerLoginListener(listener: FileOnlineLoginManager.LoginListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterLoginListener(listener: FileOnlineLoginManager.LoginListener) {
        listeners.remove(listener)
    }
}
