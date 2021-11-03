package br.com.vitorruiz.botiblog.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.vitorruiz.botiblog.data.source.remote.model.NewsItem
import br.com.vitorruiz.botiblog.databinding.ItemNewsBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(private var list: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun refreshList(list: List<NewsItem>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class NewsItemViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val _dateFormatter = SimpleDateFormat("dd-MM-yy HH:mm", Locale.US)
        private val _sourceDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

        fun bindView(item: NewsItem) {
            binding.tvUserName.text = item.user.name
            binding.tvPostContent.text = item.message.content

            Glide.with(binding.root.context)
                .load(item.user.profilePicture)
                .into(binding.ivProfile)

            _sourceDateFormatter.parse(item.message.createdAt)?.let {
                binding.tvPostDate.text = _dateFormatter.format(it)
            }
        }
    }
}