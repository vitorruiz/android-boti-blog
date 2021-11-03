package br.com.vitorruiz.botiblog.data.source.local.storage

class GlobalStorageImpl(private val localStorage: LocalStorage) : GlobalStorage {

    override var loggedUser: String?
        get() = localStorage.getString(KEY_LOGGED_USER, null)
        set(value) {
            localStorage.putString(KEY_LOGGED_USER, value)
        }

    override var lastUser: String?
        get() = localStorage.getString(KEY_LAST_USER, null)
        set(value) {
            localStorage.putString(
                KEY_LAST_USER,
                value,
                LocalStorage.PersistenceLevel.SURVIVE_SESSIONS
            )
        }

    override fun clear() {
        localStorage.clear()
    }

    companion object {
        private const val KEY_LOGGED_USER = "logged_user"
        private const val KEY_LAST_USER = "last_user"
    }
}