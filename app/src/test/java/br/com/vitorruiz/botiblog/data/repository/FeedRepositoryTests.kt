package br.com.vitorruiz.botiblog.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.botiblog.data.source.local.database.dao.PostDao
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostEntity
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
class FeedRepositoryTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var feedRepository: FeedRepository

    private val postDao: PostDao = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        feedRepository = FeedRepository(postDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should create new post`() = runBlockingTest {
        val user = "test@test.com"
        val postContent = "nice post content"

        feedRepository.createNewPost(user, postContent)

        val verifyEntity = PostEntity(0L, postContent, System.currentTimeMillis(), false, user)

        coVerify { postDao.insert(any()) }
    }

    @Test
    fun `Should edit post`() = runBlockingTest {
        val post = PostEntity(
            1,
            "nice post content",
            System.currentTimeMillis(),
            false,
            "test@test.com"
        )

        coEvery { postDao.findById(post.id) } returns post

        val newText = "edited nice post content"

        feedRepository.editPost(post.id, newText)

        val verifyEntity = post.apply {
            edited = true
            text = newText
        }

        coVerify { postDao.update(verifyEntity) }
    }

    @Test
    fun `Should delete post`() = runBlockingTest {
        val post = PostEntity(
            1,
            "nice post content",
            System.currentTimeMillis(),
            false,
            "test@test.com"
        )

        coEvery { postDao.findById(post.id) } returns post

        feedRepository.deletePost(post.id)

        coVerify { postDao.delete(post) }
    }

    @Test
    fun `Should get all posts with live data`() = runBlockingTest {
        feedRepository.getAllPosts()

        coVerify { postDao.findAllLiveData() }
    }

}