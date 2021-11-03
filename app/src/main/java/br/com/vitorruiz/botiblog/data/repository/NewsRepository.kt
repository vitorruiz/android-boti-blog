package br.com.vitorruiz.botiblog.data.repository

import br.com.vitorruiz.botiblog.data.source.remote.ApiClient
import br.com.vitorruiz.botiblog.data.source.remote.model.NewsList

class NewsRepository(private val apiClient: ApiClient) {

    suspend fun getNews(): NewsList {
        return apiClient.getApi().getNews()
    }
}