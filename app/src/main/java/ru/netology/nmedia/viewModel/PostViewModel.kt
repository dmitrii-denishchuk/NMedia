package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.InMemoryPostRepository
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()
    val data get() = repository.data
    fun clickedLike(post: Post) = repository.likeById(post.id)
    fun clickedShare(post: Post) = repository.shareById(post.id)
}