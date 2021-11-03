package br.com.vitorruiz.botiblog.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import br.com.vitorruiz.botiblog.ui.login.LoginViewModel
import br.com.vitorruiz.botiblog.util.TestContextProvider
import br.com.vitorruiz.botiblog.util.testObserver
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: LoginViewModel

    private val loginRepository: LoginRepository = mockk(relaxed = true)
    private val globalStorage: GlobalStorage = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = LoginViewModel(testContextProvider, loginRepository, globalStorage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should login user successfully`() = runBlockingTest {
        val userEmail = "test@test.com"
        val password = "123456"

        coEvery { loginRepository.login(userEmail, password) } returns true

        val loginResultLiveData = viewModel.loginResult.testObserver()

        viewModel.login(userEmail, password)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify {
            loginRepository.login(userEmail, password)
        }

        Assert.assertTrue(loginResultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(loginResultLiveData.observedValues[1] is ResultWrapper.Success)
    }

    @Test
    fun `Should login user failed`() = runBlockingTest {
        val userEmail = "test@test.com"
        val password = "123456"

        coEvery { loginRepository.login(userEmail, password) } returns false

        val loginResultLiveData = viewModel.loginResult.testObserver()

        viewModel.login(userEmail, password)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify {
            loginRepository.login(userEmail, password)
        }

        Assert.assertTrue(loginResultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(loginResultLiveData.observedValues[1] is ResultWrapper.Error)
    }
}