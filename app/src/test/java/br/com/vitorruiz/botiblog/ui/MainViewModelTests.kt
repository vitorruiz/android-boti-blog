package br.com.vitorruiz.botiblog.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import br.com.vitorruiz.botiblog.data.source.local.database.entity.UserEntity
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import br.com.vitorruiz.botiblog.util.TestContextProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: MainViewModel

    private val loginRepository: LoginRepository = mockk(relaxed = true)
    private val globalStorage: GlobalStorage = mockk(relaxed = true)

    private val userResultEventObserver: Observer<UserEntity> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = MainViewModel(testContextProvider, loginRepository, globalStorage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should logout user`() = runBlockingTest {
        viewModel.logout()

        coVerify { globalStorage.loggedUser = null }
    }

    @Test
    fun `Should fetch user`() = runBlockingTest {
        val userEntity = UserEntity(
            "test@test.com",
            "Test",
            "123456"
        )
        coEvery { loginRepository.getUser("test@test.com") } returns userEntity
        coEvery { globalStorage.loggedUser } returns "test@test.com"

        viewModel.userResult.observeForever(userResultEventObserver)

        viewModel.fetchUser()
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { userResultEventObserver.onChanged(userEntity) }
    }

}