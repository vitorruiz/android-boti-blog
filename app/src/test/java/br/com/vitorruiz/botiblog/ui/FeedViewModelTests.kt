package br.com.vitorruiz.botiblog.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.botiblog.data.repository.FeedRepository
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import br.com.vitorruiz.botiblog.ui.feed.FeedViewModel
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
class FeedViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: FeedViewModel

    private val feedRepository: FeedRepository = mockk(relaxed = true)
    private val globalStorage: GlobalStorage = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = FeedViewModel(testContextProvider, feedRepository, globalStorage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should get feed`() = runBlockingTest {
        viewModel.getFeed()

        coVerify { feedRepository.getAllPosts() }
    }

    @Test
    fun `Should get logged user`() = runBlockingTest {
        val userEmail = "test@test.com"

        coEvery { globalStorage.loggedUser } returns userEmail

        val result = viewModel.getLoggedUser()

        coVerify { globalStorage.loggedUser }

        Assert.assertEquals(userEmail, result)
    }

    @Test
    fun `Should create new post`() = runBlockingTest {
        val text = "nice post content"
        val userEmail = "test@test.com"

        coEvery { globalStorage.loggedUser } returns userEmail

        viewModel.newPost(text)

        coVerify {
            globalStorage.loggedUser
            feedRepository.createNewPost(userEmail, text)
        }
    }

    @Test
    fun `Should edit post`() = runBlockingTest {
        val text = "edited nice post content"
        val postId = 1L

        viewModel.editPost(postId, text)

        coVerify { feedRepository.editPost(postId, text) }
    }

    @Test
    fun `Should delete post`() = runBlockingTest {
        val postId = 1L

        viewModel.deletePost(postId)

        coVerify { feedRepository.deletePost(postId) }
    }
}