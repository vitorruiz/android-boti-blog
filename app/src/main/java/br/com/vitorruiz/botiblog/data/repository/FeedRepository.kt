package br.com.vitorruiz.botiblog.data.repository

import br.com.vitorruiz.botiblog.data.source.local.database.dao.PostDao
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostEntity

class FeedRepository(private val postDao: PostDao) {

    fun getAllPosts() = postDao.findAllLiveData()

    suspend fun createNewPost(userEmail: String, text: String) {
        val post = PostEntity(
            text = text,
            ownerEmail = userEmail,
            date = System.currentTimeMillis()
        )

        postDao.insert(post)
    }

    suspend fun editPost(postId: Long, newText: String) {
        val post = postDao.findById(postId)

        post?.apply {
            text = newText
            edited = true
        }

        post?.let { postDao.update(it) }
    }

    suspend fun deletePost(postId: Long) {
        val post = postDao.findById(postId)

        post?.let { postDao.delete(it) }
    }
}