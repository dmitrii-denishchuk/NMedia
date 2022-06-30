package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.clickListeners.PostsClickListeners
import ru.netology.nmedia.data.InMemoryPostRepository
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import java.util.*

class PostViewModel : ViewModel(), PostsClickListeners {

    private val repository: PostRepository = InMemoryPostRepository()

    val data get() = repository.data
    val currentPost = MutableLiveData<Post?>(null)

    fun clickedCreate(message: String) {
        if (message.isBlank()) return
        val somePost = currentPost.value?.copy(
            message = message
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            postHeader = "Я",
            date = Date().toString(),
            message = message,
            isLiked = false,
            likes = 0,
            shares = 0,
            views = 0
        )
        repository.createPost(somePost)
        currentPost.value = null
    }

    override fun clickedLike(post: Post) = repository.likeById(post.id)
    override fun clickedShare(post: Post) = repository.shareById(post.id)
    override fun clickedRemove(post: Post) = repository.removeById(post.id)
    override fun clickedEdit(post: Post) { currentPost.value = post }
}