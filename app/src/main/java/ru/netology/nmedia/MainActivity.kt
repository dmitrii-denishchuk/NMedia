package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.data.slicer
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
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

        binding.likeButton.setOnClickListener {
            viewModel.clickedLike()
        }

        binding.shareButton.setOnClickListener {
            viewModel.clickedShare()
        }
    }
}