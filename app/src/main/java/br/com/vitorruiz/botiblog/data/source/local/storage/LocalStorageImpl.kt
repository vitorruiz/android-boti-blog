package br.com.vitorruiz.botiblog.data.source.local.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class LocalStorageImpl(context: Context) : LocalStorage {

    private val erasablePreferences: SharedPreferences
    private val persistedPreferences: SharedPreferences
    private val preferences: List<SharedPreferences>
    private val serializer = Gson()

    init {
        erasablePreferences = context.getSharedPreferences(SESSION_PREFS_NAME, Context.MODE_PRIVATE)
        persistedPreferences =
            context.getSharedPreferences(PERSISTED_PREFS_NAME, Context.MODE_PRIVATE)
        preferences = listOf(erasablePreferences, persistedPreferences)
    }

    override fun <T> putSerialized(key: String, obj: T, level: LocalStorage.PersistenceLevel) {
        putString(key, serializer.toJson(obj), level)
    }

    override fun <T> getSerialized(key: String, default: T?, clazz: Class<T>): T? {
        val value = getString(key, "")
            .takeIf { it != null && it.isNotEmpty() } ?: return default
        return try {
            serializer.fromJson(value, clazz)
        } catch (e: Exception) {
            remove(key)
            null
        }
    }

    override fun putString(key: String, value: String?, level: LocalStorage.PersistenceLevel) {
        putStringInternal(key, value, definePreference(level))
    }

    override fun getString(key: String, default: String?): String? {
        val value = preferences.firstOrNull { it.contains(key) }?.getString(key, default)

        if (value == null || value.isEmpty() || value == default) {
            return default
        }

        return value
    }

    override fun putStringList(
        key: String,
        value: List<String>,
        level: LocalStorage.PersistenceLevel
    ) {
        putString(key, serializer.toJson(value), level)
    }

    override fun getStringList(key: String): MutableList<String> {
        val value = getString(key, "")
            .takeIf { it != null && it.isNotEmpty() } ?: return ArrayList()
        val listType = object : TypeToken<MutableList<String>>() {}.type
        return serializer.fromJson(value, listType)
    }

    override fun putInt(key: String, value: Int, level: LocalStorage.PersistenceLevel) {
        putString(key, value.toString(), level)
    }

    override fun getInt(key: String, default: Int): Int {
        val value = getString(key, "")
        return if (value == null || value == "") default else value.toInt()
    }

    override fun putLong(key: String, value: Long, level: LocalStorage.PersistenceLevel) {
        putString(key, value.toString(), level)
    }

    override fun getLong(key: String, default: Long): Long {
        val value = getString(key, "")
        return if (value == null || value == "") default else value.toLong()
    }

    override fun putBoolean(key: String, value: Boolean, level: LocalStorage.PersistenceLevel) {
        putString(key, value.toString(), level)
    }

    override fun putDouble(key: String, value: Double, level: LocalStorage.PersistenceLevel) {
        putString(key, value.toString(), level)
    }

    override fun getDouble(key: String, default: Double): Double {
        val value = getString(key, "")
        return if (value == null || value == "") default else value.toDouble()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        val value = getString(key, "")
        return if (value == null || value == "") default else value.toBoolean()
    }

    override fun remove(key: String, level: LocalStorage.PersistenceLevel) {
        definePreference(level).edit().remove(key).apply()
    }

    override fun clear() {
        erasablePreferences.edit().clear().apply()
    }

    override fun hasKey(key: String, level: LocalStorage.PersistenceLevel): Boolean {
        return definePreference(level).contains(key)
    }

    private fun putStringInternal(
        key: String,
        value: String?,
        preferences: SharedPreferences
    ) {
        if (value == null || value.isEmpty()) {
            preferences.edit().putString(key, value).apply()
            return
        }
        preferences.edit().putString(key, value).apply()
    }

    private fun definePreference(level: LocalStorage.PersistenceLevel) =
        when (level) {
            LocalStorage.PersistenceLevel.ERASABLE -> erasablePreferences
            LocalStorage.PersistenceLevel.SURVIVE_SESSIONS -> persistedPreferences
        }

    companion object {
        private const val SESSION_PREFS_NAME = "session_prefs"
        private const val PERSISTED_PREFS_NAME = "persisted_prefs"
    }
}