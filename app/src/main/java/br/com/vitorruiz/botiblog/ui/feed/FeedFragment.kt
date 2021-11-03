package br.com.vitorruiz.botiblog.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.vitorruiz.botiblog.core.BaseFragment
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostEntity
import br.com.vitorruiz.botiblog.databinding.FragmentFeedBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : BaseFragment<FragmentFeedBinding>() {

    private val _viewModel: FeedViewModel by viewModel()

    private lateinit var _adapter: FeedAdapter

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeedBinding = FragmentFeedBinding.inflate(inflater, container, false)

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {

        _viewModel.getLoggedUser()?.let {
            _adapter = FeedAdapter(emptyList(), it)
            _adapter.setOnItemEditListener {
                showPostFragment(it.post)
            }
            _adapter.setOnItemDeleteListener {
                _viewModel.deletePost(it.post.id)
            }
        }

        binding.fbNewPost.setOnClickListener {
            showPostFragment()
        }

        binding.rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.fbNewPost.isShown) {
                    binding.fbNewPost.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fbNewPost.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

        })

        binding.rvFeed.layoutManager = object : LinearLayoutManager(context) {
            override fun supportsPredictiveItemAnimations(): Boolean = true
        }

        binding.rvFeed.adapter = _adapter

        _viewModel.getFeed().observe(this) {
            _adapter.refreshList(it.sortedByDescending { it.post.date })
        }
    }

    private fun showPostFragment(postToEdit: PostEntity? = null) {
        NewPostFragment(postToEdit) { postId, postText ->
            postId?.let {
                _viewModel.editPost(it, postText)
            } ?: run {
                _viewModel.newPost(postText)
            }
        }.show(requireActivity().supportFragmentManager, "New Post Fragment")
    }
}