package br.com.vitorruiz.botiblog.data.source.remote

interface ApiClient {
    fun getApi(): ApiService
}