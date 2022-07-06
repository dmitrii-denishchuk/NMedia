package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.clickListeners.PostsClickListeners
import ru.netology.nmedia.data.InFilePostRepository
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import java.util.*

class PostViewModel(
    application: Application
) : AndroidViewModel(
    application
), PostsClickListeners {

    private val repository: PostRepository = InFilePostRepository(application)

    val data get() = repository.data
    val shareEvent = SingleLiveEvent<Post>()
    val playEvent = SingleLiveEvent<Post>()

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
            views = 0,
            video = "https://youtu.be/hBTNyJ33LWI"
        )
        repository.createPost(somePost)
        currentPost.value = null
    }

    override fun clickedLike(post: Post) = repository.likeById(post.id)

    override fun clickedRemove(post: Post) = repository.removeById(post.id)

    override fun clickedPlay(post: Post) {
        playEvent.value = post
        repository.views(post.id)
    }

    override fun clickedShare(post: Post) {
        shareEvent.value = post
        repository.shareById(post.id)
    }

    override fun clickedEdit(post: Post) {
        currentPost.value = post
    }
}