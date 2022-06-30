package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.clickListeners.PostsClickListeners
import ru.netology.nmedia.data.slicer
import ru.netology.nmedia.databinding.PostCardLayoutBinding
import ru.netology.nmedia.dto.Post

class PostsAdapter(
    private val clickListener: PostsClickListeners
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }

    class ViewHolder(
        private val binding: PostCardLayoutBinding,
        clickListener: PostsClickListeners
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menuButton).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.removeButton -> {
                            clickListener.clickedRemove(post)
                            true
                        }
                        R.id.editButton -> {
                            clickListener.clickedEdit(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likeButton.setOnClickListener {
                clickListener.clickedLike(post)
            }
            binding.shareButton.setOnClickListener {
                clickListener.clickedShare(post)
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                postHeader.text = post.postHeader
                postDate.text = post.date
                message.text = post.message
                likesCount.text = slicer(post.likes)
                sharesCount.text = slicer(post.shares)
                viewsCount.text = post.views.toString()
                likeButton.setImageResource(
                    if (post.isLiked) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
                menuButton.setOnClickListener { popupMenu.show() }
            }
        }
    }
}