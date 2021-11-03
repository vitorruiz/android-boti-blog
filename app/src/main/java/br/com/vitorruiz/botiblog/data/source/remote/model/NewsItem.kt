package br.com.vitorruiz.botiblog.data.source.remote.model

import com.google.gson.annotations.SerializedName


data class NewsList(
    @SerializedName("news")
    val news: List<NewsItem>
)

data class NewsItem(
    @SerializedName("message")
    val message: Message,
    @SerializedName("user")
    val user: User
)

data class Message(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_picture")
    val profilePicture: String
)