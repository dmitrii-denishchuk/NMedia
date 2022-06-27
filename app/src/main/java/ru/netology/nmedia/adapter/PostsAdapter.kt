package ru.netology.nmedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.data.slicer
import ru.netology.nmedia.databinding.PostCardLayoutBinding
import ru.netology.nmedia.dto.Post

typealias OnClickListener = (Post) -> Unit

class PostsAdapter(
    private val clickedLike: OnClickListener,
    private val clickedShare: OnClickListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("PostAdapter", "onCreateViewHolder")
        val binding = PostCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, clickedLike, clickedShare)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("PostAdapter", "onBindViewHolder $position")
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }

    class ViewHolder(
        private val binding: PostCardLayoutBinding,
        private val clickedLike: OnClickListener,
        private val clickedShare: OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likeButton.setOnClickListener {
                clickedLike(post)
            }
            binding.shareButton.setOnClickListener {
                clickedShare(post)
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                postHeader.text = post.postHeader
                postDate.text = post.date
                likesCount.text = slicer(post.likes)
                sharesCount.text = slicer(post.shares)
                viewsCount.text = post.views.toString()
                likeButton.setImageResource(
                    if (post.isLiked) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
            }
        }
    }
}