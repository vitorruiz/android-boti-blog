package br.com.vitorruiz.botiblog.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import br.com.vitorruiz.botiblog.ui.register.RegisterViewModel
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
class RegisterViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: RegisterViewModel

    private val loginRepository: LoginRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = RegisterViewModel(testContextProvider, loginRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should register user successfully`() = runBlockingTest {
        val name = "Test"
        val email = "test@test.com"
        val password = "123456"

        coEvery { loginRepository.register(email, name, password) } returns
                ResultWrapper.Success(true)

        val registerResultLiveData = viewModel.registerResult.testObserver()

        viewModel.register(name, email, password)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify {
            loginRepository.register(email, name, password)
        }

        Assert.assertTrue(registerResultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(registerResultLiveData.observedValues[1] is ResultWrapper.Success)
    }

    @Test
    fun `Should register user fail`() = runBlockingTest {
        val name = "Test"
        val email = "test@test.com"
        val password = "123456"

        coEvery { loginRepository.register(email, name, password) } returns
                ResultWrapper.Error("Já existe um usuário cadastrado com esse e-mail")

        val registerResultLiveData = viewModel.registerResult.testObserver()

        viewModel.register(name, email, password)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify {
            loginRepository.register(email, name, password)
        }

        Assert.assertTrue(registerResultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(registerResultLiveData.observedValues[1] is ResultWrapper.Error)
    }
}