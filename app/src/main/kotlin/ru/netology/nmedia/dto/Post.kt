package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val postHeader: String,
    val date: String,
    val message: String,
    val isLiked: Boolean,
    var likes: Int,
    var shares: Int,
    var views: Int,
    val video: String
)