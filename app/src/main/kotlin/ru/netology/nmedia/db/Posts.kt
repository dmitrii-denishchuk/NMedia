package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

internal fun PostEntity.toPost() = Post(
    id = id,
    postHeader = postHeader,
    date = date,
    message = message,
    isLiked = isLiked,
    likes = likes,
    shares = shares,
    views = views,
    video = video
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    postHeader = postHeader,
    date = date,
    message = message,
    isLiked = isLiked,
    likes = likes,
    shares = shares,
    views = views,
    video = video
)