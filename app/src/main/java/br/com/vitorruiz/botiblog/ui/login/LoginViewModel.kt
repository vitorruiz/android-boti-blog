package br.com.vitorruiz.botiblog.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.botiblog.core.CoroutineContextProvider
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.core.SingleLiveEvent
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val loginRepository: LoginRepository,
    private val globalStorage: GlobalStorage
) : ViewModel() {

    private val _loginResult = SingleLiveEvent<ResultWrapper<Any>>()

    val loginResult: LiveData<ResultWrapper<Any>> = _loginResult
    val lastLoggedUser = globalStorage.lastUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.postValue(ResultWrapper.Loading())

            withContext(contextProvider.io) {
                if (loginRepository.login(email, password)) {
                    globalStorage.lastUser = email
                    globalStorage.loggedUser = email
                    _loginResult.postValue(ResultWrapper.Success(Any()))
                } else {
                    _loginResult.postValue(ResultWrapper.Error("Usuário ou senha inválido"))
                }
            }
        }
    }
}