package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val postHeader: String,
    val date: String,
    val message: String,
    val isLiked: Boolean,
    val likes: Int,
    val shares: Int,
    val views: Int,
    val video: String
)