package br.com.vitorruiz.botiblog.data.source.local.storage

interface GlobalStorage {
    var loggedUser: String?
    var lastUser: String?

    fun clear()
}