package br.com.vitorruiz.botiblog.data.source.local.storage

interface LocalStorage {
    fun <T> putSerialized(
        key: String,
        obj: T,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun <T> getSerialized(
        key: String,
        default: T?,
        clazz: Class<T>
    ): T?

    fun putString(
        key: String,
        value: String?,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun getString(
        key: String,
        default: String?
    ): String?

    fun putStringList(
        key: String,
        value: List<String>,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun getStringList(
        key: String
    ): MutableList<String>

    fun putInt(
        key: String,
        value: Int,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun getInt(key: String, default: Int): Int

    fun putLong(
        key: String,
        value: Long,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun getLong(key: String, default: Long): Long

    fun putDouble(
        key: String,
        value: Double,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun getDouble(key: String, default: Double): Double

    fun putBoolean(
        key: String,
        value: Boolean,
        level: PersistenceLevel = PersistenceLevel.ERASABLE
    )

    fun getBoolean(
        key: String,
        default: Boolean
    ): Boolean

    fun remove(key: String, level: PersistenceLevel = PersistenceLevel.ERASABLE)

    fun clear()

    fun hasKey(key: String, level: PersistenceLevel = PersistenceLevel.ERASABLE): Boolean

    sealed class PersistenceLevel {
        object ERASABLE : PersistenceLevel()
        object SURVIVE_SESSIONS : PersistenceLevel()
    }
}