package br.com.vitorruiz.botiblog.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.vitorruiz.botiblog.core.BaseFragment
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.databinding.FragmentNewsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private val _viewModel: NewsViewModel by viewModel()

    private val _adapter = NewsAdapter(emptyList())

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsBinding = FragmentNewsBinding.inflate(inflater, container, false)

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {
        setupViews()
        setupLiveData()

        _viewModel.fetchNews()
    }

    private fun setupViews() {
        binding.rvFeed.layoutManager = LinearLayoutManager(context)
        binding.rvFeed.adapter = _adapter
    }

    private fun setupLiveData() {
        _viewModel.newsResult.observe(this) {
            when (it) {
                is ResultWrapper.Success -> {
                    binding.includeLoading.loadingContainer.isVisible = false
                    _adapter.refreshList(it.data.news.sortedByDescending { it.message.createdAt })
                }
                is ResultWrapper.Loading -> binding.includeLoading.loadingContainer.isVisible = true
                is ResultWrapper.Error -> {
                    binding.includeLoading.loadingContainer.isVisible = false
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}