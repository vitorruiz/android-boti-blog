package br.com.vitorruiz.botiblog

import android.app.Application
import br.com.vitorruiz.botiblog.core.CoroutineContextProvider
import br.com.vitorruiz.botiblog.data.repository.FeedRepository
import br.com.vitorruiz.botiblog.data.repository.LoginRepository
import br.com.vitorruiz.botiblog.data.repository.NewsRepository
import br.com.vitorruiz.botiblog.data.source.local.database.buildAppDatabase
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorageImpl
import br.com.vitorruiz.botiblog.data.source.local.storage.LocalStorage
import br.com.vitorruiz.botiblog.data.source.local.storage.LocalStorageImpl
import br.com.vitorruiz.botiblog.data.source.remote.ApiClient
import br.com.vitorruiz.botiblog.data.source.remote.ApiClientImpl
import br.com.vitorruiz.botiblog.ui.MainViewModel
import br.com.vitorruiz.botiblog.ui.feed.FeedViewModel
import br.com.vitorruiz.botiblog.ui.login.LoginViewModel
import br.com.vitorruiz.botiblog.ui.news.NewsViewModel
import br.com.vitorruiz.botiblog.ui.register.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(module {
                single { CoroutineContextProvider() }

                // Storage
                single<LocalStorage> { LocalStorageImpl(this@App) }
                single<GlobalStorage> { GlobalStorageImpl(get()) }

                // Database
                single { buildAppDatabase(this@App).userDao() }
                single { buildAppDatabase(this@App).postDao() }

                // Repositories
                single { LoginRepository(get()) }
                single { FeedRepository(get()) }
                single { NewsRepository(get()) }

                // Networking
                single<ApiClient> { ApiClientImpl() }

                // View Models
                viewModel { MainViewModel(get(), get(), get()) }
                viewModel { LoginViewModel(get(), get(), get()) }
                viewModel { RegisterViewModel(get(), get()) }
                viewModel { FeedViewModel(get(), get(), get()) }
                viewModel { NewsViewModel(get(), get()) }
            })
        }
    }
}