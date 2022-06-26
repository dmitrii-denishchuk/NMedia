package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val postHeader: String,
    val date: String,
    val isLiked: Boolean,
    var likes: Int,
    var shares: Int,
    val views: Int
)