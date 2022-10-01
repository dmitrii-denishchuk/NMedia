package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.clickListeners.PostsClickListeners
import ru.netology.nmedia.data.postRepository.impl.slicer
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

            binding.playButton.setOnClickListener {
                clickListener.clickedPlay(post)
            }

            binding.previewPostButton.setOnClickListener {
                clickListener.clickedPost(post)
            }

            binding.menuButton.setOnClickListener {
                popupMenu.show()
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                if (post.video.isNotEmpty()) {
                    videoGroupPreview.visibility = View.VISIBLE
                    Glide.with(preview.context)
                        .load("https://img.youtube.com/vi/hBTNyJ33LWI/0.jpg")
                        .into(preview)
                } else videoGroupPreview.visibility = View.GONE
                if (post.message.isNotEmpty()) groupMessage.visibility =
                    View.VISIBLE
                else groupMessage.visibility = View.GONE
                postHeader.text = post.postHeader
                postDate.text = post.date
                message.text = post.message
                likeButton.text = slicer(post.likes)
                shareButton.text = slicer(post.shares)
                views.text = post.views.toString()
                likeButton.isChecked = post.isLiked
            }
        }
    }
}