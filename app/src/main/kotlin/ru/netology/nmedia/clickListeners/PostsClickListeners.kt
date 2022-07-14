package ru.netology.nmedia.clickListeners

import ru.netology.nmedia.dto.Post

interface PostsClickListeners {
    fun clickedLike(post: Post)
    fun clickedShare(post: Post)
    fun clickedRemove(post: Post)
    fun clickedEdit(post: Post)
    fun clickedPlay(post: Post)
    fun clickedPost(post: Post)
}