package br.com.vitorruiz.botiblog.data.repository

import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.source.local.database.dao.UserDao
import br.com.vitorruiz.botiblog.data.source.local.database.entity.UserEntity

class LoginRepository(private val userDao: UserDao) {

    suspend fun register(email: String, name: String, password: String): ResultWrapper<Boolean> {
        val userAlreadyExists = getUser(email) != null

        if (userAlreadyExists) return ResultWrapper.Error("Já existe um usuário cadastrado com esse e-mail")

        val userId = userDao.insert(UserEntity(email, name, password))

        return userId?.let { ResultWrapper.Success(true) } ?: ResultWrapper.Success(false)
    }

    suspend fun login(email: String, password: String): Boolean {
        return getUser(email)?.password == password
    }

    suspend fun getUser(email: String): UserEntity? {
        return userDao.findByEmail(email)
    }
}