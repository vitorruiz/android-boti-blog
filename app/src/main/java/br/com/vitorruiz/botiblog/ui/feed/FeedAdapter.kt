package br.com.vitorruiz.botiblog.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.vitorruiz.botiblog.R
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostWithUser
import br.com.vitorruiz.botiblog.databinding.ItemFeedBinding
import java.text.SimpleDateFormat
import java.util.*

class FeedAdapter(
    private var list: List<PostWithUser>,
    private val currentUserEmail: String
) : RecyclerView.Adapter<FeedAdapter.FeedItemViewHolder>() {

    private var _onEditItemClicked: ((item: PostWithUser) -> Unit)? = null
    private var _onDeleteItemClicked: ((item: PostWithUser) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return list[position].post.id
    }

    fun refreshList(list: List<PostWithUser>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun setOnItemEditListener(block: (item: PostWithUser) -> Unit) {
        _onEditItemClicked = block
    }

    fun setOnItemDeleteListener(block: (item: PostWithUser) -> Unit) {
        _onDeleteItemClicked = block
    }

    inner class FeedItemViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val _dateFormatter = SimpleDateFormat("dd-MM-yy HH:mm", Locale.US)

        fun bindView(item: PostWithUser) {
            binding.tvUserName.text = item.user.name
            binding.tvUserEmail.text = item.user.email
            binding.tvPostContent.text = item.post.text
            binding.tvPostDate.text = _dateFormatter.format(Date(item.post.date))

            binding.ivPostOptions.isVisible = item.user.email == currentUserEmail

            binding.ivPostOptions.setOnClickListener { showMenu(it, item) }
        }

        private fun showMenu(view: View, item: PostWithUser) {
            PopupMenu(binding.root.context, view).apply {
                menuInflater.inflate(R.menu.menu_item_post, menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_edit_post -> {
                            _onEditItemClicked?.invoke(item)
                            true
                        }
                        R.id.action_delete_post -> {
                            _onDeleteItemClicked?.invoke(item)
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }
}