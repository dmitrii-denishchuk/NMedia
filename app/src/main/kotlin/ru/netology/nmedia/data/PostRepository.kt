package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun createPost(post: Post)
    fun views(id: Int)

    companion object {
        const val NEW_POST_ID = 0
    }
}