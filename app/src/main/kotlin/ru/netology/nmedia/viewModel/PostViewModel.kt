package ru.netology.nmedia.viewModel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.clickListeners.PostsClickListeners
import ru.netology.nmedia.data.postRepository.PostRepository
import ru.netology.nmedia.data.postRepository.impl.PostRepositoryImpl
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import java.util.*

class PostViewModel(application: Application) : AndroidViewModel(application), PostsClickListeners {

    private val repository: PostRepository = PostRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).postDao
    )

    val data get() = repository.data
    val shareEvent = SingleLiveEvent<Post>()
    val playEvent = SingleLiveEvent<Post>()
    val currentPost = MutableLiveData<Post?>(null)

    fun clickedSave(message: String) {
        if (message.isBlank()) return
        val somePost = currentPost.value?.copy(
            message = message
        ) ?: Post(message = message)
        repository.save(somePost)
        currentPost.value = null
    }

    override fun clickedLike(post: Post) = repository.likeById(post.id)

    override fun clickedRemove(post: Post) = repository.removeById(post.id)

    override fun clickedShare(post: Post) {
        shareEvent.value = post
        repository.shareById(post.id)
    }

    override fun clickedEdit(post: Post) {
        currentPost.value = post
    }

    override fun clickedPlay(post: Post) {
        playEvent.value = post
        repository.views(post.id)
    }

    override fun clickedPost(post: Post) {
        currentPost.value = post
    }
}