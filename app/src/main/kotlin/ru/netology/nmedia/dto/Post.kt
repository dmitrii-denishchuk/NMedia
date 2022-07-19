package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable
import ru.netology.nmedia.data.postRepository.PostRepository
import java.util.*

@Serializable
data class Post(
    val id: Int = PostRepository.NEW_POST_ID,
    val postHeader: String = "Я",
    val date: String = Date().toString(),
    val message: String = "",
    val isLiked: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val video: String = "https://youtu.be/hBTNyJ33LWI"
)