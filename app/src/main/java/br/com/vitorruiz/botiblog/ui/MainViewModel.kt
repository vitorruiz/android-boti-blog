package br.com.vitorruiz.botiblog.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.botiblog.core.CoroutineContextProvider
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import br.com.vitorruiz.botiblog.data.source.local.database.entity.UserEntity
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val loginRepository: LoginRepository,
    private val globalStorage: GlobalStorage
) : ViewModel() {

    private val _userResult = MutableLiveData<UserEntity>()

    val userResult: LiveData<UserEntity> = _userResult

    fun fetchUser() {
        viewModelScope.launch {
            val user = withContext(contextProvider.io) {
                globalStorage.loggedUser?.let { loginRepository.getUser(it) }
            }

            user?.let { _userResult.postValue(it) }
        }
    }

    fun logout() {
        globalStorage.loggedUser = null
    }
}