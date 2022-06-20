package ru.netology.nmedia.data

data class Post(
    val postHeader: String,
    val date: String,
    var isLiked: Boolean
)