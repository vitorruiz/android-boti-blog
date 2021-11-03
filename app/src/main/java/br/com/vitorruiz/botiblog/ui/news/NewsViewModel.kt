package br.com.vitorruiz.botiblog.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.botiblog.core.CoroutineContextProvider
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.repository.NewsRepository
import br.com.vitorruiz.botiblog.data.source.remote.model.NewsList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NewsViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsResult = MutableLiveData<ResultWrapper<NewsList>>()

    val newsResult: LiveData<ResultWrapper<NewsList>> = _newsResult

    fun fetchNews() {
        viewModelScope.launch {
            _newsResult.postValue(ResultWrapper.Loading())

            withContext(contextProvider.io) {
                try {
                    val newsList = newsRepository.getNews()
                    _newsResult.postValue(ResultWrapper.Success(newsList))
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> _newsResult.postValue(
                            ResultWrapper.Error(
                                throwable.message ?: "", isConnectionError = true
                            )
                        )
                        else -> _newsResult.postValue(
                            ResultWrapper.Error(throwable.message ?: "")
                        )
                    }
                }
            }
        }
    }
}