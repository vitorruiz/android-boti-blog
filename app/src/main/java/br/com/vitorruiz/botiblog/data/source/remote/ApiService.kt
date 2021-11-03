package br.com.vitorruiz.botiblog.data.source.remote

import br.com.vitorruiz.botiblog.data.source.remote.model.NewsList
import retrofit2.http.GET

interface ApiService {

    // News
    @GET("data.json")
    suspend fun getNews(): NewsList
}