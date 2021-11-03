package br.com.vitorruiz.botiblog.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.botiblog.core.CoroutineContextProvider
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _registerResult = MutableLiveData<ResultWrapper<Boolean>>()

    val registerResult: LiveData<ResultWrapper<Boolean>> = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.postValue(ResultWrapper.Loading())

            withContext(contextProvider.io) {
                _registerResult.postValue(loginRepository.register(email, name, password))
            }
        }
    }
}