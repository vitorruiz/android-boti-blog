package br.com.vitorruiz.botiblog.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.data.repository.NewsRepository
import br.com.vitorruiz.botiblog.data.source.remote.model.NewsList
import br.com.vitorruiz.botiblog.ui.news.NewsViewModel
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
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NewsViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: NewsViewModel

    private val newsRepository: NewsRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = NewsViewModel(testContextProvider, newsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should fetch news`() = runBlockingTest {
        coEvery { newsRepository.getNews() } returns NewsList(emptyList())

        val resultLiveData = viewModel.newsResult.testObserver()

        viewModel.fetchNews()
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { newsRepository.getNews() }

        Assert.assertTrue(resultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(resultLiveData.observedValues[1] is ResultWrapper.Success)
    }

    @Test
    fun `Should fetch news failed with IOException`() = runBlockingTest {
        coEvery { newsRepository.getNews() } throws IOException()

        val resultLiveData = viewModel.newsResult.testObserver()

        viewModel.fetchNews()
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { newsRepository.getNews() }

        Assert.assertTrue(resultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(resultLiveData.observedValues[1] is ResultWrapper.Error)
    }

    @Test
    fun `Should fetch news failed with generic exception`() = runBlockingTest {
        coEvery { newsRepository.getNews() } throws Exception()

        val resultLiveData = viewModel.newsResult.testObserver()

        viewModel.fetchNews()
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { newsRepository.getNews() }

        Assert.assertTrue(resultLiveData.observedValues[0] is ResultWrapper.Loading)
        Assert.assertTrue(resultLiveData.observedValues[1] is ResultWrapper.Error)
    }
}