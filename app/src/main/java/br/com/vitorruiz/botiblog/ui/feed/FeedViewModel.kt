package br.com.vitorruiz.botiblog.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.botiblog.core.CoroutineContextProvider
import br.com.vitorruiz.botiblog.data.repository.FeedRepository
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import kotlinx.coroutines.launch

class FeedViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val feedRepository: FeedRepository,
    private val globalStorage: GlobalStorage
) : ViewModel() {

    fun getFeed() = feedRepository.getAllPosts()

    fun getLoggedUser() = globalStorage.loggedUser

    fun newPost(text: String) {
        viewModelScope.launch(contextProvider.io) {
            globalStorage.loggedUser?.let { userEmail ->
                feedRepository.createNewPost(userEmail, text)
            }
        }
    }

    fun editPost(postId: Long, postText: String) {
        viewModelScope.launch(contextProvider.io) {
            feedRepository.editPost(postId, postText)
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch(contextProvider.io) {
            feedRepository.deletePost(postId)
        }
    }
}