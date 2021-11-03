package br.com.vitorruiz.botiblog.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.source.local.database.dao.UserDao
import br.com.vitorruiz.botiblog.data.source.local.database.entity.UserEntity
import br.com.vitorruiz.botiblog.util.TestContextProvider
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
class LoginRepositoryTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var loginRepository: LoginRepository

    private val userDao: UserDao = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        loginRepository = LoginRepository(userDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should return true when user login`() = runBlockingTest {
        val email = "test@test.com"
        val password = "123456"

        coEvery { userDao.findByEmail(email) } returns UserEntity(email, "Test", password)

        val result = loginRepository.login(email, password)

        coVerify { userDao.findByEmail(email) }

        Assert.assertTrue(result)
    }

    @Test
    fun `Should return false when user login`() = runBlockingTest {
        val email = "test@test.com"
        val password = "123456"

        coEvery { userDao.findByEmail(email) } returns UserEntity(email, "Test", "654321")

        val result = loginRepository.login(email, password)

        coVerify { userDao.findByEmail(email) }

        Assert.assertFalse(result)
    }

    @Test
    fun `Should return user with e-mail`() = runBlockingTest {
        val email = "test@test.com"

        coEvery { userDao.findByEmail(email) } returns UserEntity("test@test.com", "Test", "654321")

        val result = loginRepository.getUser(email)

        coVerify { userDao.findByEmail(email) }

        Assert.assertEquals(email, result?.email)
    }

    @Test
    fun `Should register user successfully`() = runBlockingTest {
        val user = UserEntity(
            "test@test.com",
            "Test",
            "123456"
        )

        coEvery { userDao.findByEmail(user.email) } returns null
        coEvery { userDao.insert(any()) } returns 1L

        val result = loginRepository.register(user.email, user.name, user.password)

        coVerify {
            userDao.findByEmail(user.email)
            userDao.insert(user)
        }

        Assert.assertTrue(result is ResultWrapper.Success)
        Assert.assertTrue((result as ResultWrapper.Success).data)
    }

    @Test
    fun `Should register user with fail`() = runBlockingTest {
        val user = UserEntity(
            "test@test.com",
            "Test",
            "123456"
        )

        coEvery { userDao.findByEmail(user.email) } returns null
        coEvery { userDao.insert(any()) } returns null

        val result = loginRepository.register(user.email, user.name, user.password)

        coVerify {
            userDao.findByEmail(user.email)
            userDao.insert(user)
        }

        Assert.assertTrue(result is ResultWrapper.Success)
        Assert.assertFalse((result as ResultWrapper.Success).data)
    }

    @Test
    fun `Should try register with already exists user`() = runBlockingTest {
        val user = UserEntity(
            "test@test.com",
            "Test",
            "123456"
        )

        coEvery { userDao.findByEmail(user.email) } returns user
        coEvery { userDao.insert(any()) } returns 1L

        val result = loginRepository.register(user.email, user.name, user.password)

        coVerify {
            userDao.findByEmail(user.email)
        }

        Assert.assertTrue(result is ResultWrapper.Error)
        Assert.assertEquals((result as ResultWrapper.Error).message, "Já existe um usuário cadastrado com esse e-mail")
    }
}